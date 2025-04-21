package com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import com.housweet.presentation.ui.theme.Red
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun SearchRoomScreen(
    modifier: Modifier,
    searchRoomViewModel: SearchRoomViewModel = hiltViewModel()
) {
    val uiState: SearchRoomUiState by searchRoomViewModel.uiState.collectAsState()
    var code by remember { mutableStateOf("") }
    when (uiState) {
        SearchRoomUiState.IDlE -> {
            SearchRoomContent(
                modifier = modifier,
                code = code,
                onValueChange = {
                    code = it
                },
                onBtnClick = {

                }
            )
        }

        SearchRoomUiState.IsLoading -> {
            LoadingBar()
        }

        SearchRoomUiState.Error -> {
            SearchRoomContent(
                modifier = modifier,
                code = code,
                isWarning = true,
                onValueChange = {
                    code = it
                },
                onBtnClick = {

                }
            )
        }
    }
}

@Composable
private fun SearchRoomContent(
    modifier: Modifier,
    code: String,
    isWarning: Boolean = false,
    onBtnClick: (name: String) -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(White)
    ) {
        var warning by remember { mutableStateOf(isWarning) }
        Spacer(modifier = Modifier.height(92.dp))
        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Black,
            text = "초대코드를 입력해주세요.",
            fontWeight = FontWeight.W800,
            fontSize = 12.sp,
            fontFamily = nanumSquareFontFamily,
            lineHeight = 18.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        WriteTextFiled(
            text = code,
            textColor = if (warning) Red else Black,
            onValueChange = {
                if (warning) {
                    warning = false
                }
                onValueChange(it)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (warning) {
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

@Preview
@Composable
private fun SearchRoomScreenPreview() {
    SearchRoomContent(
        modifier = Modifier,
        code = "asdasas",
        isWarning = true,
        onValueChange = {},
        onBtnClick = {}
    )
}