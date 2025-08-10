package com.housweet.presentation.ui.startPage.splashPage

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Purple_7342DD
import com.housweet.presentation.ui.theme.White

@Composable
fun SplashScreen(
    modifier: Modifier,
    splashViewModel: SplashViewModel = hiltViewModel(),
    onNextScreen: (isAutoLogin: Boolean, isAgreeTermsOfService: Boolean, isBelongToRoom: Boolean) -> Unit,
) {
    val uiState: SplashState by splashViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        splashViewModel.event.collect { event ->
            when (event) {
                is SplashEvent.IsAutoLogin -> {
                    onNextScreen(true, event.isAgreeTermsOfService, event.isBelongToRoom)
                }

                is SplashEvent.IsNotAutoLogin -> {
                    onNextScreen(false, false, false)
                }

                SplashEvent.Error -> {
                    val snackBarResult = snackBarHostState.showSnackbar(
                        message = "오류가 발생했습니다.",
                        actionLabel = "재시도"
                    )

                    when (snackBarResult) {
                        SnackbarResult.Dismissed -> { }
                        SnackbarResult.ActionPerformed -> { splashViewModel.checkLogin() }
                    }
                }
            }
        }
    }

    when (uiState) {
        SplashState.Idle -> {
            SplashContent(
                modifier = modifier,
                snackBarHostState = snackBarHostState,
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun SplashContent(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("motion.json"))
    val progress by animateLottieCompositionAsState(composition)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = Purple_7342DD
    ) {
        Column(
            modifier = Modifier
                .padding(top = 120.dp, start = 20.dp)
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
            
            GuideText(
                color = White,
                text = "룸메이트 찾을 땐,",
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 38.sp,
                textAlign = TextAlign.Center
            )

            GuideText(
                color = White,
                text = "하우스잇",
                fontWeight = FontWeight(1000),
                fontSize = 24.sp,
                lineHeight = 38.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashContent(
        modifier = Modifier,
        snackBarHostState = remember { SnackbarHostState() },
    )
}