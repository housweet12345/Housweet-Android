package com.housweet.presentation.ui.mypage.deleteAccount

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.housweet.presentation.ui.common.CustomAlertDialog
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginEvent
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun DeleteAccountScreen(
    onBackClick: () -> Unit,
    onSuccessDeleteAccount: () -> Unit,
    deleteAccountViewModel: DeleteAccountViewModel = hiltViewModel()
) {
    val uiState: DeleteAccountUiState by deleteAccountViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackBarHostState = remember { SnackbarHostState() }
    var isBelongToRoom by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    var isSuccessful by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            deleteAccountViewModel.event.collect { event ->
                when (event) {
                    is DeleteAccountEvent.IsBelongToRoom -> {
                        isBelongToRoom = event.isBelongToRoom
                    }

                    DeleteAccountEvent.DeleteAccountSuccess -> {
                        isSuccessful = true
                    }

                    is DeleteAccountEvent.Error -> {
                        snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }


    when (uiState) {
        DeleteAccountUiState.Idle -> {
            DeleteAccountContent(
                isBelongToRoom = isBelongToRoom,
                isChecked = isChecked,
                isSuccessful = isSuccessful,
                showDeleteAccountDialog = showDeleteAccountDialog,
                snackBarHostState = snackBarHostState,
                onBackClick = onBackClick,
                onCheckClick = { isChecked = !isChecked },
                onDeleteAccountClick = { if (isChecked) showDeleteAccountDialog = true },
                onDismissDialog = { showDeleteAccountDialog = false },
                onConfirmDialog = {
                    deleteAccountViewModel.deleteAccount()
                    showDeleteAccountDialog = false
                },
                onSuccessDeleteAccount = onSuccessDeleteAccount
            )
        }
        DeleteAccountUiState.IsLoading -> { LoadingScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteAccountContent(
    isBelongToRoom: Boolean,
    isChecked: Boolean,
    isSuccessful: Boolean,
    showDeleteAccountDialog: Boolean,
    snackBarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onCheckClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmDialog: () -> Unit,
    onSuccessDeleteAccount: () -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title = {
                    Text(
                        text = "탈퇴하기",
                        fontSize = 14.sp
                    )
                },
                navigationIcon = {
                    if (!isSuccessful) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_black),
                            contentDescription = "뒤로가기",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable { onBackClick() }
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // ✅ 배경색 흰색 지정
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(top = 30.dp)
        ) {
            if (showDeleteAccountDialog) {
                CustomAlertDialog(
                    onDismissRequest = onDismissDialog,
                    onConfirmation = onConfirmDialog,
                    dialogText = "하우스잇 계정을 탈퇴하시겠습니까?"
                )
            }

            if (!isSuccessful) {
                Column(
                    modifier = Modifier.padding(horizontal = 30.dp)
                ) {
                    GuideText(
                        color = Purple,
                        text = "하우스잇 탈퇴",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isBelongToRoom) {
                        IsNotBelongToRoomView()
                    } else {
                        IsBelongToRoomView(
                            isChecked = isChecked,
                            onCheckClick = onCheckClick
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 30.dp)
                ) {
                    GuideText(
                        color = Purple,
                        text = "탈퇴가 완료되었습니다.",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    GuideText(
                        color = Black,
                        text = "그동안 하우스잇을 이용해주셔서 감사합니다.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomButton(
                text = if (isBelongToRoom || isSuccessful) "확인" else "탈퇴하기",
                onBtnClick = {
                    when {
                        isBelongToRoom -> onBackClick()
                        isSuccessful -> onSuccessDeleteAccount()
                        else -> onDeleteAccountClick()
                    }
                }
            )
        }
    }
}

@Composable
private fun IsNotBelongToRoomView() {
    val guideText = buildAnnotatedString {
        append("마이페이지 > 마이하우스 우측 상단을 클릭 한 후\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
            append("수정하기 - 방 삭제")
        }
        append(" 또는 ")

        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
            append("나가기")
        }

        append("를 해주세요.")
    }

    GuideText(
        color = Black,
        text = "현재 마이하우스가 있어 탈퇴가 불가능합니다.",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        textAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = guideText,
        style = TextStyle(
            color = Black,
            fontFamily = nanumSquareFontFamily,
            fontSize = 12.sp,
            lineHeight = 22.sp,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
            letterSpacing = 0.sp
        )
    )
}

@Composable
private fun IsBelongToRoomView(
    isChecked: Boolean,
    onCheckClick: () -> Unit
) {
    GuideText(
        color = Black,
        text = "탈퇴하기 전에 아래 사항을 확인해주세요.",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        textAlign = TextAlign.Start
    )

    Row(
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = if (isChecked) R.drawable.check else R.drawable.uncheck),
            contentDescription = "check",
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onCheckClick()
                }
        )

        GuideText(
            modifier = Modifier.padding(start = 5.dp, end = 25.dp),
            color = Black,
            text = "채팅, 게시글 등 모든 기록은 삭제되며, 복구할 수 없습니다.",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteAccountScreenPreview() {
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    var isSuccessful by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    DeleteAccountContent(
        isBelongToRoom = false,
        showDeleteAccountDialog = showDeleteAccountDialog,
        isSuccessful = isSuccessful,
        snackBarHostState = remember { snackBarHostState },
        onBackClick = {},
        onDeleteAccountClick = { if (isChecked) showDeleteAccountDialog = true },
        onDismissDialog = { showDeleteAccountDialog = false },
        onConfirmDialog = {
            isSuccessful = true
            showDeleteAccountDialog = false
        },
        isChecked = isChecked,
        onCheckClick = { isChecked = !isChecked },
        onSuccessDeleteAccount = {}
    )
}