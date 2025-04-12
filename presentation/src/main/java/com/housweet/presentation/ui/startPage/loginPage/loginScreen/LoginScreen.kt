package com.housweet.presentation.ui.startPage.loginPage.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.housweet.presentaion.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.BackgroundColor
import com.housweet.presentation.ui.theme.IndicatorOff
import com.housweet.presentation.ui.theme.IndicatorOn
import com.housweet.presentation.ui.theme.LoginBtnColor
import com.housweet.presentation.ui.theme.TextColorBlack
import com.housweet.presentation.ui.theme.TextColorBrown
import com.housweet.presentation.ui.theme.TextColorPurple

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Spacer(modifier = Modifier.height(height = 136.dp))

        GuideImg()

        Spacer(modifier = Modifier.height(height = 20.dp))

        GuideText(
            modifier = Modifier.fillMaxWidth(),
            text = "룸메이트를 위한 하우스잇",
            color = TextColorPurple,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(height = 9.dp))

        GuideText(
            modifier = Modifier.fillMaxWidth(),
            color = TextColorBlack,
            text = "하우스잇을 통해 룸메이트를 구하고",
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center
        )

        GuideText(
            modifier = Modifier.fillMaxWidth(),
            color = TextColorBlack,
            text = "룸메이트와 함께 집을 관리해요!",
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(height = 30.dp))

        KakaoLoginButton { }
    }
}

@Composable
fun GuideImg() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val guideImgList =
        listOf(R.drawable.guideimage1, R.drawable.guideimage1, R.drawable.guideimage1)
    GuideImgPagerIndicator(pagerState.pageCount, pagerState.currentPage)

    Spacer(modifier = Modifier.height(height = 33.dp))

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.height(266.dp)
    ) { page ->
        Image(
            painter = painterResource(id = guideImgList[page]),
            contentDescription = "guideImg",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GuideImgPagerIndicator(pageCount: Int, currentPage: Int) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            repeat(pageCount) { iteration ->
                val color = if (currentPage == iteration) IndicatorOn else IndicatorOff
                val paddingStart = when (iteration) {
                    0 -> 0.dp
                    else -> 7.5.dp
                }

                val paddingEnd = when (iteration) {
                    pageCount - 1 -> 0.dp
                    else -> 7.5.dp
                }

                Box(
                    modifier = Modifier
                        .padding(start = paddingStart, end = paddingEnd)
                        .clip(CircleShape)
                        .background(color)
                        .size(4.dp)
                )
            }
        }
    }
}

@Composable
fun KakaoLoginButton(
    onKakaoLoginClick: () -> Unit
) {
    Button(
        onClick = { onKakaoLoginClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LoginBtnColor,
            contentColor = TextColorBrown
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.kakao_logo),
            contentDescription = "kakaoicon"
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        GuideText(
            color = TextColorBrown,
            text = "카카오 계정으로 계속하기",
            fontWeight = FontWeight.W700,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}