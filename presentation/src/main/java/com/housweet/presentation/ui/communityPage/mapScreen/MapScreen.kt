package com.housweet.presentation.ui.communityPage.mapScreen

import android.util.Log
import android.view.Gravity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.domain.model.Coordinate
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.White_F8F8F8
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberMarkerState

@Composable
fun MapScreen(
    modifier: Modifier,
    searchRegion: Coordinate?,
    mapViewModel: MapViewModel = hiltViewModel(),
    onMarkerClick: () -> Unit,
    onViewPostBtnClick: () -> Unit,
    onSearchBtnClick: () -> Unit,
    onWritePostBtnClick: () -> Unit,
) {
    val uiState by mapViewModel.uiState.collectAsState()
    val mapState by mapViewModel.mapState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    val isDongZoom by remember { derivedStateOf { cameraPositionState.position.zoom >= MapConstants.ZOOM_THRESHOLD } }
    val animationProgress = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(Unit) {
        searchRegion?.let {
            cameraPositionState.position =
                CameraPosition(LatLng(it.y, it.x), MapConstants.DEFAULT_ZOOM_LEVEL)
        }
    }
    
    LaunchedEffect(Unit) {
        mapViewModel.event.collect { event ->
            when (event) {
                MapEvent.Error -> {
                    snackBarHostState.showSnackbar(
                        message = "방 정보를 제대로 불러오지 못했어요.",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            mapViewModel.getDongPostInfo()
        } else {
            mapViewModel.freeMarkers()
        }
    }

    LaunchedEffect(isDongZoom) {
        if (mapState.prevZoomState != isDongZoom && !mapState.isMarkerAnimating) {
            mapViewModel.updateZoomState(isDongZoom = isDongZoom, isMarkerAnimating = true)

            animationProgress.snapTo(0f)
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = MapConstants.ANIMATION_DURATION_MILLIS,
                    easing = FastOutSlowInEasing
                )
            ) {
                mapViewModel.updateMarkerAnimation(animationProgress.value, isDongZoom, cameraPositionState.contentBounds)
            }
        }
        mapViewModel.updateZoomState(isDongZoom = isDongZoom, isMarkerAnimating = false)
    }

    when (uiState) {
        MapUiState.Idle -> {
            MapContent(
                modifier = modifier,
                cameraPositionState = cameraPositionState,
                snackBarHostState = snackBarHostState,
                renderMarkers = mapState.renderMarkers,
                markerStates =  mapState.markerStates,
                onMarkerClick = {
                    if (!mapState.isMarkerAnimating) {
                        onMarkerClick()
                    }
                },
                onViewPostBtnClick = { range ->
                    if (range == null) return@MapContent
                    onViewPostBtnClick()
                },
                onSearchBtnClick = onSearchBtnClick,
                onWritePostBtnClick = onWritePostBtnClick
            )
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun MapContent(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    snackBarHostState: SnackbarHostState,
    renderMarkers: Map<String, List<LatLng>>,
    markerStates: MutableMap<String, MarkerState>,
    onMarkerClick: (String) -> Unit,
    onViewPostBtnClick: (LatLngBounds?) -> Unit,
    onSearchBtnClick: () -> Unit,
    onWritePostBtnClick: () -> Unit,
) {
    val mapUiSettings = remember {
        MapUiSettings(
            isRotateGesturesEnabled = false,
            isZoomControlEnabled = false,
            isScaleBarEnabled = false,
            logoGravity = Gravity.END or Gravity.TOP
        )
    }

    val mapProperties = remember {
        MapProperties(
            minZoom = MapConstants.MIN_ZOOM_LEVEL
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            MapTopBar()
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NaverMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings
            ) {
                renderMarkers.forEach { (region, postList) ->
                    RoomMarker(
                        postRegion = region,
                        postNum = postList.size,
                        markerState = markerStates[region]
                            ?: rememberMarkerState(position = postList[0]),
                        onClick = onMarkerClick
                    )
                }
            }

            MapOptionButton(
                modifier = Modifier
                    .padding(start = 15.dp, top = 14.dp)
                    .align(Alignment.TopStart),
                icon = R.drawable.small_house,
                text = "방 올리기",
            ) {
                onWritePostBtnClick()
            }

            MapOptionButton(
                modifier = Modifier
                    .padding(start = 15.dp, bottom = 21.dp)
                    .align(Alignment.BottomStart),
                icon = R.drawable.three_lines,
                text = "게시글로 보기",
            ) {
                onViewPostBtnClick(cameraPositionState.contentBounds)
            }

            MapOptionButton(
                modifier = Modifier
                    .padding(end = 22.dp, bottom = 21.dp)
                    .align(Alignment.BottomEnd),
                icon = R.drawable.search,
                text = "검색하기",
            ) {
                onSearchBtnClick()
            }
        }
    }
}

@Composable
private fun MapTopBar() {
    Row(
        modifier = Modifier
            .background(Purple)
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.big_house),
            contentDescription = "house",
            modifier = Modifier
                .padding(start = 20.dp)
                .clip(CircleShape)
                .clickable { },
        )

        Row(
            modifier = Modifier
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = "chat",
                modifier = Modifier
                    .size(24.dp)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .clickable { },
                tint = White
            )

            Icon(
                painter = painterResource(id = R.drawable.alarm),
                contentDescription = "alarm",
                modifier = Modifier
                    .size(24.dp)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .clickable { },
                tint = White
            )

            Icon(
                painter = painterResource(id = R.drawable.mypage),
                contentDescription = "mypage",
                modifier = Modifier
                    .size(24.dp)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .clickable { },
                tint = White
            )
        }
    }
}

@Composable
private fun MapOptionButton(
    modifier: Modifier,
    icon: Int,
    text: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Gray_A5A5A5,
                ambientColor = Gray_CBCBCB
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        color = White,
        border = BorderStroke(0.5.dp, Purple),
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "icon",
                modifier = Modifier.size(24.dp),
                tint = Purple,
            )
            GuideText(
                color = Purple,
                text = text,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun RoomMarker(
    postRegion: String,
    postNum: Int,
    markerState: MarkerState,
    onClick: (String) -> Unit
) {
    MarkerComposable(
        keys = arrayOf(postRegion),
        state = markerState,
        onClick = {
            onClick(it.tag.toString())
            true
        },
        tag = postRegion
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color = Purple.copy(alpha = 0.9f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            GuideText(
                color = White_F8F8F8,
                text = "$postNum",
                fontWeight = FontWeight.Medium,
                fontSize = 30.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TestMapContent(
    modifier: Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MapTopBar()
        },
        containerColor = White
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            MapOptionButton(
                modifier = Modifier
                    .padding(start = 15.dp, top = 14.dp)
                    .align(Alignment.TopStart),
                icon = R.drawable.small_house,
                text = "방 올리기",
            ) { }

            MapOptionButton(
                modifier = Modifier
                    .padding(start = 15.dp, bottom = 21.dp)
                    .align(Alignment.BottomStart),
                icon = R.drawable.three_lines,
                text = "게시글로 보기",
            ) { }

            MapOptionButton(
                modifier = Modifier
                    .padding(end = 22.dp, bottom = 21.dp)
                    .align(Alignment.BottomEnd),
                icon = R.drawable.search,
                text = "검색하기",
            ) { }
        }
    }
}

@Preview
@Composable
private fun MapScreenPreview() {
    TestMapContent(
        modifier = Modifier
    )
}