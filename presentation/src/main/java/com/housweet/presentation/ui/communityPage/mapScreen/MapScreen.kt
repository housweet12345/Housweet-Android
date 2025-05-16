package com.housweet.presentation.ui.communityPage.mapScreen

import android.view.Gravity
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.domain.model.Coordinate
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    modifier: Modifier,
    searchRegion: Coordinate?,
    onViewPostBtnClick: () -> Unit,
    onSearchBtnClick: () -> Unit,
    onWritePostBtnClick: () -> Unit
) {
    val cameraPositionState: CameraPositionState = rememberCameraPositionState()

    if (searchRegion != null) {
        LaunchedEffect(Unit) {
            cameraPositionState.position =
                CameraPosition(LatLng(searchRegion.y, searchRegion.x), 13.0)
        }
    }

    MapContent(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onViewPostBtnClick = onViewPostBtnClick,
        onSearchBtnClick = onSearchBtnClick,
        onWritePostBtnClick = onWritePostBtnClick
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun MapContent(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    onViewPostBtnClick: () -> Unit,
    onSearchBtnClick: () -> Unit,
    onWritePostBtnClick: () -> Unit
) {
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isRotateGesturesEnabled = false,
                isZoomControlEnabled = false,
                isScaleBarEnabled = false,
                logoGravity = Gravity.END or Gravity.TOP
            )
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            MapTopBar()
        },
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
                uiSettings = mapUiSettings
            )

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
                onViewPostBtnClick()
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
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White
        ),
        elevation = ButtonDefaults.buttonElevation(2.dp),
        border = BorderStroke(0.5.dp, Purple),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "icon",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(24.dp),
                tint = Purple,
            )
            GuideText(
                modifier = Modifier.padding(end = 12.dp),
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

@Preview
@Composable
private fun MapScreenPreview() {
    TestMapContent(
        modifier = Modifier
    )
}