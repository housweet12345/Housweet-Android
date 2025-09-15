package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.BottomButton
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.common.WarningText
import com.housweet.presentation.ui.common.WriteTextFiled
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.White

@Composable
fun CreateRoomScreen(
    modifier: Modifier,
    createRoomViewModel: CreateRoomViewModel = hiltViewModel(),
    onBackBtnClick: () -> Unit,
    onSuccessCreateRoom: () -> Unit
) {
    val uiState: CreateRoomState by createRoomViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            createRoomViewModel.event.collect { event ->
                when (event) {
                    CreateRoomEvent.Error -> {
                        snackBarHostState.showSnackbar(
                            message = "방을 만드는 데 실패했습니다.",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }

                    CreateRoomEvent.Success -> {
                        onSuccessCreateRoom()
                    }
                }
            }
        }
    }

    when (uiState) {
        CreateRoomState.Idle -> {
            CreateRoomContent(
                modifier = modifier,
                snackBarHostState = snackBarHostState,
                onBackBtnClick = onBackBtnClick,
                onBtnClick = {
                    createRoomViewModel.createRoom(it)
                }
            )
        }

        CreateRoomState.IsLoading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun CreateRoomContent(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    onBackBtnClick: () -> Unit,
    onBtnClick: (name: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = { TopBar(text = "하우스 생성", onBackBtnClick = onBackBtnClick) },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(White)
                .consumeWindowInsets(WindowInsets.navigationBars)
                .imePadding()
        ) {
            var roomName by remember { mutableStateOf("") }
            Spacer(modifier = Modifier.height(92.dp))

            GuideText(
                modifier = Modifier.padding(start = 20.dp),
                color = Black,
                text = "방 이름을 작성해주세요.",
                fontWeight = FontWeight.W800,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            WriteTextFiled(
                text = roomName,
                textLength = 8,
                onValueChange = {
                    roomName = it
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            WarningText(
                text = "방 이름은 8자 제한이 있습니다."
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            BottomButton(text = "만들기") {
                onBtnClick(roomName)
            }
        }
    }
}

@Preview
@Composable
private fun CreateRoomScreenPreview() {
    CreateRoomContent(
        modifier = Modifier,
        snackBarHostState = remember { SnackbarHostState() },
        onBackBtnClick = {},
        onBtnClick = {}
    )
}