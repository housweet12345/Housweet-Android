package com.housweet.presentation.ui.communityPage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.BottomButton
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.Purple_4B3AAC
import com.housweet.presentation.ui.theme.White

@Composable
fun GuideToCreateRoomScreen(
    onGuideClick: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    BackHandler {
        onBackBtnClick()
    }

    Scaffold(
        topBar = {
            GuideToCreateRoomTopBar(
                onBackBtnClick = onBackBtnClick
            )
        },
        containerColor = White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.dead_house),
                    contentDescription = "deadHouse",
                    modifier = Modifier.padding(top = 129.dp)
                )

                GuideText(
                    modifier = Modifier.padding(top = 31.dp),
                    color = Purple,
                    text = "앗! 마이하우스가 없어요!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

                GuideText(
                    modifier = Modifier.padding(top = 10.dp),
                    color = Black,
                    text = "하우스 올리기는 마이하우스를 생성한\n" +
                            "방장만 가능해요!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(weight = 1f))

                BottomButton(text = "마이하우스 생성하기") {
                    onGuideClick()
                }
            }
        }
    }
}

@Composable
private fun GuideToCreateRoomTopBar(
    onBackBtnClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .clickable { onBackBtnClick() },
            tint = Black
        )

        GuideText(
            modifier = Modifier.align(Alignment.Center),
            color = Black,
            text = "하우스 올리기",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun GuideToCreateRoomScreenPreview() {
    GuideToCreateRoomScreen(
        onGuideClick = {},
        onBackBtnClick = {}
    )
}