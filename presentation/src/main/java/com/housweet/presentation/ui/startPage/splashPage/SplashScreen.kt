package com.housweet.presentation.ui.startPage.splashPage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.debug.DebugConfigActivity
import com.housweet.presentation.ui.theme.Purple_4B3AAC
import com.housweet.presentation.ui.theme.White

@Composable
fun SplashScreen(
    modifier: Modifier,
    splashViewModel: SplashViewModel = hiltViewModel(),
    onNextScreen: (isAutoLogin: Boolean, isAgreeTermsOfService: Boolean, isSetProfile: Boolean, isBelongToRoom: Boolean) -> Unit,
) {
    val uiState: SplashState by splashViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val debugConfigLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // DebugConfigActivity에서 돌아왔을 때 로그인 체크 시작
        splashViewModel.checkLogin()
    }

    // 초기 상태 체크 - 한 번만 실행
    LaunchedEffect(Unit) {
        when (uiState) {
            is SplashState.ShowDebugConfig -> {
                splashViewModel.onDebugConfigShown()
                val intent = Intent(context, DebugConfigActivity::class.java)
                debugConfigLauncher.launch(intent)
            }
            is SplashState.Idle -> {
                splashViewModel.checkLogin()
            }
        }
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            splashViewModel.event.collect { event ->
                when (event) {
                    is SplashEvent.IsAutoLogin -> {
                        onNextScreen(
                            true,
                            event.isAgreeTermsOfService,
                            event.isSetProfile,
                            event.isBelongToRoom
                        )
                    }

                    is SplashEvent.IsNotAutoLogin -> {
                        onNextScreen(false, false, false, false)
                    }

                    SplashEvent.Error -> {
                        val snackBarResult = snackBarHostState.showSnackbar(
                            message = "오류가 발생했습니다.",
                            actionLabel = "재시도"
                        )

                        when (snackBarResult) {
                            SnackbarResult.Dismissed -> {}
                            SnackbarResult.ActionPerformed -> {
                                splashViewModel.checkLogin()
                            }
                        }
                    }
                }
            }
        }
    }

    when (uiState) {
        SplashState.Idle, SplashState.ShowDebugConfig -> {
            SplashContent(
                modifier = modifier,
                snackBarHostState = snackBarHostState
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
        containerColor = Purple_4B3AAC
    ) {
        Column(
            modifier = Modifier
                .padding(top = 120.dp, start = 20.dp)
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(40.dp)
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