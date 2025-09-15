package com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen

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
import com.housweet.presentation.ui.theme.Red
import com.housweet.presentation.ui.theme.White

@Composable
fun SearchRoomScreen(
    modifier: Modifier,
    searchRoomViewModel: SearchRoomViewModel = hiltViewModel(),
    onBackBtnClick: () -> Unit,
    onSuccessSearchRoom: () -> Unit
) {
    val uiState: SearchRoomState by searchRoomViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var isWarning by remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            searchRoomViewModel.event.collect { event ->
                when (event) {
                    SearchRoomEvent.Error -> {
                        isWarning = true
                    }

                    SearchRoomEvent.Success -> {
                        onSuccessSearchRoom()
                    }
                }
            }
        }
    }

    when (uiState) {
        SearchRoomState.Idle -> {
            SearchRoomContent(
                modifier = modifier,
                code = code,
                isWarning = isWarning,
                onValueChange = {
                    if (isWarning) {
                        isWarning = false
                    }
                    code = it
                },
                onBackBtnClick = onBackBtnClick,
                onBtnClick = {
                    searchRoomViewModel.accessRoomWithInviteCode(it)
                }
            )
        }

        SearchRoomState.IsLoading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun SearchRoomContent(
    modifier: Modifier,
    code: String,
    isWarning: Boolean,
    onBackBtnClick: () -> Unit,
    onBtnClick: (name: String) -> Unit,
    onValueChange: (String) -> Unit
) {
    Scaffold (
        modifier = modifier,
        topBar = { TopBar(text = "하우스 찾기", onBackBtnClick = onBackBtnClick) },
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
            Spacer(modifier = Modifier.height(92.dp))
            GuideText(
                modifier = Modifier.padding(start = 20.dp),
                color = Black,
                text = "초대코드를 입력해주세요.",
                fontWeight = FontWeight.W800,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            WriteTextFiled(
                text = code,
                textColor = if (isWarning) Red else Black,
                onValueChange = {
                    onValueChange(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isWarning) {
                WarningText(
                    text = "없는 초대코드 입니다."
                )
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            BottomButton(text = "찾기") {
                onBtnClick(code)
            }
        }
    }
}

@Preview
@Composable
private fun SearchRoomScreenPreview() {
    SearchRoomContent(
        modifier = Modifier,
        code = "asdasas",
        isWarning = true,
        onValueChange = {},
        onBackBtnClick = {},
        onBtnClick = {}
    )
}