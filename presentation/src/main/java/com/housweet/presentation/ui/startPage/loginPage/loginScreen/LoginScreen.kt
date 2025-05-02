package com.housweet.presentation.ui.startPage.loginPage.loginScreen

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.presentaion.R
import com.housweet.presentation.ui.startPage.BackOnPressed
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.startPage.LoadingBar
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Brown
import com.housweet.presentation.ui.theme.Gray_E7E7E7
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.Yellow
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    modifier: Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNextScreen: () -> Unit,
) {
    val uiState: LoginUiState by loginViewModel.loginUiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    BackOnPressed()

    when (uiState) {
        LoginUiState.SignUp -> {
            LaunchedEffect(key1 = true) {
                onNextScreen()
            }
        }

        LoginUiState.SignIn -> {
            LaunchedEffect(key1 = true) {
                onNextScreen()
            }
        }

        LoginUiState.IsLoading -> {
            LoadingBar()
        }

        LoginUiState.LoginError -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) {
                LoginContent(modifier = modifier) {
                    kakaoLogin(
                        viewModel = loginViewModel,
                        context = context
                    )
                }

                LaunchedEffect(snackBarHostState) {
                    snackBarHostState.showSnackbar(
                        message = "로그인에 실패했습니다.",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Short
                    )

                }
            }
        }

        LoginUiState.IDlE -> {
            LoginContent(modifier = modifier) {
                kakaoLogin(
                    viewModel = loginViewModel,
                    context = context
                )
            }
        }
    }
}

@Composable
private fun LoginContent(modifier: Modifier, onKakaoLoginClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Spacer(modifier = Modifier.height(height = 136.dp))

        GuideImg()

        Spacer(modifier = Modifier.height(height = 20.dp))

        GuideText(
            modifier = Modifier.fillMaxWidth(),
            text = "룸메이트를 위한 하우스잇",
            color = Purple,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(height = 9.dp))

        GuideText(
            modifier = Modifier.fillMaxWidth(),
            color = Black,
            text = "하우스잇을 통해 룸메이트를 구하고\n룸메이트와 함께 집을 관리해요!",
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(height = 30.dp))

        KakaoLoginButton { onKakaoLoginClick() }
    }
}

@Composable
private fun GuideImg() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val guideImgList =
        listOf(R.drawable.guide_image1, R.drawable.guide_image1, R.drawable.guide_image1)
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
private fun GuideImgPagerIndicator(pageCount: Int, currentPage: Int) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            repeat(pageCount) { iteration ->
                val color = if (currentPage == iteration) Purple else Gray_E7E7E7
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
private fun KakaoLoginButton(
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
            containerColor = Yellow,
            contentColor = Brown
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.kakao_logo),
            contentDescription = "kakaoicon"
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        GuideText(
            color = Brown,
            text = "카카오 계정으로 계속하기",
            fontWeight = FontWeight.W700,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun kakaoLogin(viewModel: LoginViewModel, context: Context) {
    viewModel.isLoading()
    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            viewModel.loginFail()
        } else if (token != null) {
            viewModel.signIn()
        }
    }

    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                viewModel.loginFail()
                return@loginWithKakaoTalk
            }
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
        } else if (token != null) {
            viewModel.signIn()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginContent(Modifier) { }
}