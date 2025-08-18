package com.housweet.presentation.ui.mypage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.housweet.presentation.R
import com.housweet.presentation.viewmodel.mypage.MyHouseEditEffect
import com.housweet.presentation.viewmodel.mypage.MyHouseEditViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHouseEditScreen(
    navController: NavController,
    houseName: String,
    startDate: String,
    inviteCode: String,
    onDelete: () -> Unit = {},
    onComplete: () -> Unit = {},
    onCodeRefresh: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val viewModel: MyHouseEditViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // 스낵바
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 최초 로드
    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { e ->
            when (e) {
                is MyHouseEditEffect.ShowMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(e.msg, duration = SnackbarDuration.Short)
                    }
                }
                is MyHouseEditEffect.CloseWithRefresh -> {
                    val key = "MY_HOUSE_REFRESH"
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(key, true)

                    val popped = navController.popBackStack()
                    if (!popped) {
                        navController.navigate("myhousedetail") {
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                }
            }
        }
    }

    BackHandler {
        val popped = navController.popBackStack()
        if (!popped) {
            navController.navigate("myhousedetail") {
                launchSingleTop = true
                restoreState = false
            }
        }
    }

    BackHandler {
        val popped = navController.popBackStack()
        if (!popped) {
            navController.navigate("myhousedetail") {
                launchSingleTop = true
                restoreState = false
            }
        }
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title={
                    Text(
                        text = "마이하우스 수정",
                        fontSize = 14.sp
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "하우스 이름",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info), // ⓘ 아이콘
                        contentDescription = null,
                        modifier = Modifier
                            .size(14.dp),
                        tint = Color(0xFF665ED3) // 연보라색
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "10자 이내로 입력해주세요.",
                        fontSize = 10.sp,
                        color = Color(0xFF6C5CE7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.name,
                onValueChange = {viewModel.onNameChange(it)},
                placeholder = { Text("10자 이내로 입력해주세요.", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,    // 포커스 되었을 때
                    unfocusedBorderColor = Color.LightGray,    // 포커스 없을 때
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 방 시작일
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "방 시작일", fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = uiState.startDate, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 초대 코드
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "초대 코드", fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.refresh),
                    contentDescription = "refresh",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(enabled = !uiState.isLoading) {
                            viewModel.refreshInviteCode()
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = uiState.inviteCode, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 방 삭제하기
            Text(
                text = "방 삭제하기",
                color = Color.Red,
                modifier = Modifier.clickable(enabled = !uiState.isLoading) {
                        viewModel.deleteMyHouse()
                },
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // 완료 버튼
            Button(
                onClick = {
                    if (!uiState.isLoading && uiState.roomId != null) {
                        viewModel.submit()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (uiState.isLoading) "처리 중입니다..." else "방 정보를 불러오는 중입니다.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF665ED3)),
                shape = RoundedCornerShape(6.dp),
                enabled = !uiState.isLoading && uiState.roomId != null
            ) {
                androidx.compose.material3.Text(
                    if (uiState.isLoading) "저장 중..." else "완료",
                    color = Color(0xFFF8F8F8),
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}