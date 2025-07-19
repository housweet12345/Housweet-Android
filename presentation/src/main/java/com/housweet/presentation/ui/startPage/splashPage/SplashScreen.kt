package com.housweet.presentation.ui.startPage.splashPage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.Purple

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    onNextScreen: (isAutoLogin: Boolean, isAgreeTermsOfService: Boolean) -> Unit,
) {
    val uiState: SplashState by splashViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        splashViewModel.event.collect { event ->
            when (event) {
                is SplashEvent.IsAutoLogin -> {
                    onNextScreen(true, event.isAgreeTermsOfService)
                }

                is SplashEvent.IsNotAutoLogin -> {
                    onNextScreen(false, false)
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
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                containerColor = Purple
            ) {
                SplashContent()
            }
        }
    }
}

@Composable
private fun SplashContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.big_house),
            contentDescription = "Splash Image",
            modifier = Modifier
                .size(100.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashContent()
}