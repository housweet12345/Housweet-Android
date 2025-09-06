package com.housweet.presentation.ui.roomNoticePage.writeRuleOfRoomPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
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
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily
import com.vane.badwordfiltering.BadWordFiltering

@Composable
fun WriteRuleOfRoomScreen(
    onBackBtnClick: () -> Unit,
    onSuccessPostRule: () -> Unit,
    writeRuleOfRoomViewModel: WriteRuleOfRoomViewModel = hiltViewModel()
) {
    val uiState by writeRuleOfRoomViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    var contentValue by remember { mutableStateOf(TextFieldValue("")) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            writeRuleOfRoomViewModel.event.collect { event ->
                when (event) {
                    WriteRuleOfRoomEvent.CurseFiltering -> {
                        snackBarHostState.showSnackbar(
                            message = "부적절한 내용이 포함되어있습니다!",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }

                    WriteRuleOfRoomEvent.Error -> {

                    }
                }
            }
        }
    }

    when (uiState) {
        WriteRuleOfRoomUiState.Idle -> {
            WriteRuleOfRoomContent(
                contentValue = contentValue,
                snackBarHostState = snackBarHostState,
                onContentTextChanged = {
                    if (it.text.length > 100) return@WriteRuleOfRoomContent
                    contentValue = it
                },
                onBackBtnClick = onBackBtnClick,
                onFinishBtnClick = {
                    if (BadWordFiltering().blankCheck(contentValue.text)) {
                        writeRuleOfRoomViewModel.curseError()
                        return@WriteRuleOfRoomContent
                    }
                    onSuccessPostRule()
                }
            )
        }

        WriteRuleOfRoomUiState.Loading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun WriteRuleOfRoomContent(
    contentValue: TextFieldValue,
    snackBarHostState: SnackbarHostState,
    onContentTextChanged: (TextFieldValue) -> Unit,
    onBackBtnClick: () -> Unit,
    onFinishBtnClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            WriteRuleOfRoomTopBar(
                onBackBtnClick = onBackBtnClick,
                onFinishBtnClick = onFinishBtnClick
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = White
    ) { innerPadding ->
        RuleOfRoomContent(
            modifier = Modifier.padding(innerPadding),
            contentValue = contentValue
        ) {
            onContentTextChanged(it)
        }
    }
}

@Composable
private fun WriteRuleOfRoomTopBar(
    onBackBtnClick: () -> Unit,
    onFinishBtnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 17.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onBackBtnClick() },
            tint = Black
        )

        GuideText(
            color = Black,
            text = "방의 규칙",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )

        GuideText(
            modifier = Modifier.clickable { onFinishBtnClick() },
            color = Black,
            text = "완료",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RuleOfRoomContent(
    modifier: Modifier,
    contentValue: TextFieldValue,
    onContentTextChanged: (TextFieldValue) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 20.dp)
    ) {
        BasicTextField(
            value = contentValue,
            onValueChange = {
                onContentTextChanged(it)
            },
            modifier = Modifier.padding(start = 10.dp),
            textStyle = TextStyle(
                color = Black,
                fontFamily = nanumSquareFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                ),
                letterSpacing = 0.sp,
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (contentValue.text.isEmpty()) {
                    GuideText(
                        color = Gray_7E7E7E,
                        text = "규칙을 입력해주세요.",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.Start
                    )
                }
                innerTextField()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteRuleOfRoomScreenPreview() {
    WriteRuleOfRoomContent(
        contentValue = TextFieldValue(""),
        snackBarHostState = remember { SnackbarHostState() },
        onContentTextChanged = { },
        onBackBtnClick = { },
        onFinishBtnClick = { }
    )
}