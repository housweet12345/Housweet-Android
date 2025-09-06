package com.housweet.presentation.ui.roomNoticePage.writePostOfRoomNoticePage

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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily
import com.vane.badwordfiltering.BadWordFiltering

@Composable
fun WritePostOfNoticeScreen(
    onBackBtnClick: () -> Unit,
    onSuccessPostNotice: () -> Unit,
    writePostOfNoticeViewModel: WritePostOfRoomNoticeViewModel = hiltViewModel()
) {
    val uiState by writePostOfNoticeViewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var titleValue by remember { mutableStateOf(TextFieldValue("")) }
    var contentValue by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        writePostOfNoticeViewModel.event.collect { event ->
            when (event) {
                WritePostOfNoticeEvent.CurseFiltering -> {
                    snackBarHostState.showSnackbar(
                        message = "부적절한 내용이 포함되어있습니다!",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Short
                    )
                }

                WritePostOfNoticeEvent.Error -> {

                }
            }
        }
    }

    when(uiState) {
        WritePostOfNoticeState.Idle -> {
            WritePostOfNoticeContent(
                titleValue = titleValue,
                contentValue = contentValue,
                snackBarHostState = snackBarHostState,
                onTitleTextChanged = {
                    if (it.text.length > 20) return@WritePostOfNoticeContent
                    titleValue = it
                },
                onContentTextChanged = {
                    if (it.text.length > 100) return@WritePostOfNoticeContent
                    contentValue = it
                },
                onBackBtnClick = onBackBtnClick,
                onFinishBtnClick = {
                    if (BadWordFiltering().blankCheck(titleValue.text) || BadWordFiltering().blankCheck(contentValue.text)) {
                        writePostOfNoticeViewModel.curseError()
                        return@WritePostOfNoticeContent
                    }
                    onSuccessPostNotice()
                }
            )
        }

        WritePostOfNoticeState.Loading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun WritePostOfNoticeContent(
    titleValue: TextFieldValue,
    contentValue: TextFieldValue,
    snackBarHostState: SnackbarHostState,
    onTitleTextChanged: (TextFieldValue) -> Unit,
    onContentTextChanged: (TextFieldValue) -> Unit,
    onBackBtnClick: () -> Unit,
    onFinishBtnClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            WritePostOfNoticeTopBar(
                onBackBtnClick = onBackBtnClick,
                onFinishBtnClick = onFinishBtnClick
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = White
    ) { innerPadding ->
        PostOfNoticeContent(
            modifier = Modifier.padding(innerPadding),
            titleValue = titleValue,
            contentValue = contentValue,
            onTitleTextChanged = {
                onTitleTextChanged(it)
            },
            onContentTextChanged = {
                onContentTextChanged(it)
            }
        )
    }
}

@Composable
private fun WritePostOfNoticeTopBar(
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
            text = "게시글 작성",
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
private fun PostOfNoticeContent(
    modifier: Modifier,
    titleValue: TextFieldValue,
    contentValue: TextFieldValue,
    onTitleTextChanged: (TextFieldValue) -> Unit,
    onContentTextChanged: (TextFieldValue) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 20.dp)
    ) {
        PostOfNoticeTextField(
            textValue = titleValue,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            hint = "제목을 입력해주세요.",
        ) {
            onTitleTextChanged(it)
        }

        PostOfNoticeTextField(
            modifier = Modifier.padding(top = 8.dp),
            textValue = contentValue,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            hint = "내용을 입력해주세요.",
        ) {
            onContentTextChanged(it)
        }
    }
}

@Composable
private fun PostOfNoticeTextField(
    modifier: Modifier = Modifier,
    textValue: TextFieldValue,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    hint: String,
    onTextChanged: (TextFieldValue) -> Unit
) {
    BasicTextField(
        value = textValue,
        onValueChange = {
            onTextChanged(it)
        },
        modifier = modifier,
        textStyle = TextStyle(
            color = Black,
            fontFamily = nanumSquareFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
            letterSpacing = 0.sp,
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            if (textValue.text.isEmpty()) {
                GuideText(
                    color = Gray_7E7E7E,
                    text = hint,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                    textAlign = TextAlign.Start
                )
            }
            innerTextField()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun WritePostOfNoticeScreenPreview() {
    WritePostOfNoticeContent(
        titleValue = TextFieldValue(""),
        contentValue = TextFieldValue(""),
        snackBarHostState = remember { SnackbarHostState() },
        onTitleTextChanged = { },
        onContentTextChanged = { },
        onBackBtnClick = { },
        onFinishBtnClick = { }
    )
}