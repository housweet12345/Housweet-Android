package com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.presentation.ui.startPage.BottomButton
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.startPage.LoadingBar
import com.housweet.presentation.ui.startPage.WarningText
import com.housweet.presentation.ui.startPage.WriteTextFiled
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateRoomScreen(
    modifier: Modifier,
    createRoomViewModel: CreateRoomViewModel = hiltViewModel()
) {
    val uiState: CreateRoomState by createRoomViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
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
                    snackBarHostState.showSnackbar(
                        message = "방을 만들었습니다.",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    when (uiState) {
        CreateRoomState.Idle -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) {
                CreateRoomContent(
                    modifier = modifier
                ) {
                    createRoomViewModel.createRoom(it)
                }
            }
        }

        CreateRoomState.IsLoading -> {
            LoadingBar()
        }
    }
}

@Composable
private fun CreateRoomContent(
    modifier: Modifier,
    onBtnClick: (name: String) -> Unit
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(White)
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

@Preview
@Composable
private fun CreateRoomScreenPreview() {
    CreateRoomContent(
        modifier = Modifier
    ) {

    }
}