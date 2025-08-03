package com.housweet.presentation.ui.communityPage.mapScreen

import android.view.Gravity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.housweet.domain.model.NearByPostCountModel
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
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    modifier: Modifier,
    searchRegion: Coordinate?,
    mapViewModel: MapViewModel = hiltViewModel(),
    onMarkerClick: (region: String) -> Unit,
    onViewPostBtnClick: (regions: String) -> Unit,
    onSearchBtnClick: () -> Unit,
    onWritePostBtnClick: () -> Unit,
    onChatClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onMyPageClick: () -> Unit,
) {
    val uiState by mapViewModel.uiState.collectAsState()
    val mapState by mapViewModel.mapState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState { position = CameraPosition(LatLng(37.5666805, 126.9784147), MapConstants.MAX_ZOOM_LEVEL) }

    LaunchedEffect(Unit) {
        searchRegion?.let {
            cameraPositionState.position =
                CameraPosition(LatLng(it.y, it.x), MapConstants.MAX_ZOOM_LEVEL)
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
            mapViewModel.getDongPostInfo(
                latitude = cameraPositionState.position.target.latitude,
                longitude = cameraPositionState.position.target.longitude,
                zoomLevel = cameraPositionState.position.zoom
            )
        } else {
            mapViewModel.freeMarkers()
        }
    }
    when (uiState) {
        MapUiState.Idle -> {
            MapContent(
                modifier = modifier,
                cameraPositionState = cameraPositionState,
                snackBarHostState = snackBarHostState,
                markerStates =  mapState.markerStates,
                onMarkerClick = {
                    onMarkerClick(it)
                },
                onViewPostBtnClick = { range ->
                    if (range == null) return@MapContent
                    val filteredPostRegion = mapState.markerData
                        .filter { MapUtils.isPositionVisible(LatLng(it.latitude, it.longitude), range) }
                        .map { "${it.siName} ${it.guName} ${it.dongName}" }

                    onViewPostBtnClick(filteredPostRegion.joinToString(","))
                },
                onSearchBtnClick = onSearchBtnClick,
                onWritePostBtnClick = onWritePostBtnClick,
                onChatClick = onChatClick,
                onNotificationClick = onNotificationClick,
                onMyPageClick = onMyPageClick,
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
    markerStates: MutableMap<NearByPostCountModel, MarkerState>,
    onMarkerClick: (String) -> Unit,
    onViewPostBtnClick: (LatLngBounds?) -> Unit,
    onSearchBtnClick: () -> Unit,
    onWritePostBtnClick: () -> Unit,
    onChatClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onMyPageClick: () -> Unit,
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
            maxZoom = MapConstants.MAX_ZOOM_LEVEL,
            minZoom = MapConstants.MIN_ZOOM_LEVEL
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            MapTopBar(
                onChatClick = onChatClick,
                onNotificationClick = onNotificationClick,
                onMyPageClick = onMyPageClick
            )
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
                markerStates.forEach { (markerInfo, markerState) ->
                    RoomMarker(
                        postRegion = "${markerInfo.siName} ${markerInfo.guName} ${markerInfo.dongName}",
                        postNum = markerInfo.roomCount,
                        markerState = markerState,
                        onClick = { onMarkerClick("${markerInfo.siName} ${markerInfo.guName} ${markerInfo.dongName}") }
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
private fun MapTopBar(
    onChatClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onMyPageClick: () -> Unit
) {
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
                    .clickable {
                        onChatClick()
                    },
                tint = White
            )

            Icon(
                painter = painterResource(id = R.drawable.alarm),
                contentDescription = "alarm",
                modifier = Modifier
                    .size(24.dp)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .clickable {
                        onNotificationClick()
                    },
                tint = White
            )

            Icon(
                painter = painterResource(id = R.drawable.mypage),
                contentDescription = "mypage",
                modifier = Modifier
                    .size(24.dp)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .clickable {
                        onMyPageClick()
                    },
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
                .size(40.dp)
                .background(color = Purple.copy(alpha = 0.9f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            GuideText(
                color = White_F8F8F8,
                text = "$postNum",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TestMapContent(
    modifier: Modifier,
    onChatClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onMyPageClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            MapTopBar(
                onChatClick = onChatClick,
                onNotificationClick = onNotificationClick,
                onMyPageClick = onMyPageClick
            )
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
        modifier = Modifier,
        onChatClick = {
            println("채팅 버튼 클릭됨 (Preview용)")
        },
        onNotificationClick = {
            println("알림창 버튼 클릭됨 (Preview용)")
        },
        onMyPageClick = {
            println("마이페이지 버튼 클릭됨 (Preview용)")
        }
    )
}