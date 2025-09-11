package com.housweet.presentation.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.CustomMenu
import com.housweet.presentation.ui.common.MenuItem
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.viewmodel.chatlist.ChatListViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatName: String,
    navController: NavController,
    senderId: Int,
    receiverId: Int,
    roomId: Int
) {
    RequestGalleryPermission()
    val viewModel = hiltViewModel<ChatListViewModel>()
    val chatItems = remember { mutableStateListOf<ChatItem>() }

    var expanded by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showReportConfirm by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val menuItems = buildList {
        add(MenuItem(text = "채팅방 삭제하기") {
            showDeleteConfirm
            expanded = false
        })

        add(MenuItem(text = "신고하기") {
            showReportConfirm = true
            expanded = false
        })
    }

    // 채팅 메시지 polling
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.fetchChatMessages(senderId, receiverId) { messages ->
                chatItems.clear()
                chatItems.addAll(processChatMessagesWithDate(messages, senderId))
            }
            delay(3000)
        }
    }

    LaunchedEffect(receiverId) {
        Log.d("Chat", "OPEN chat with senderId=$senderId receiverId=$receiverId")
    }

    // 최신 메시지로 스크롤
    LaunchedEffect(chatItems.size) {
        if (chatItems.isNotEmpty()) {
            listState.animateScrollToItem(chatItems.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                text = chatName,
                onBackBtnClick = { navController.popBackStack() }
            ) { modifier ->
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "menu",
                    modifier = modifier
                        .padding(end = 20.dp)
                        .clip(CircleShape)
                        .clickable { expanded = !expanded },
                    tint = Black
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()   // 하단 시스템 바 높이만큼 패딩
                    .imePadding()              // 키보드가 뜰 때 그 높이만큼 위로
            ) {
                ChatInput(
                    senderId = senderId,
                    receiverId = receiverId,
                    onSendMessage = { sId, rId, message ->
                        if (message.isNotBlank()) {
                            chatItems.add(ChatItem.TextMessage(message, true))
                            viewModel.sendChatMessage(sId, rId, message) { success ->
                                Log.d("Chat", if (success) "전송 성공" else "전송 실패")
                            }
                        }
                    },
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().zIndex(1f)
            ) {
                if (expanded) {
                    CustomMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        menuItems = menuItems,
                        modifier = Modifier.align(Alignment.TopEnd)

                    )
                }
            }

            ChatScreenContent(
                chatItems = chatItems,
                modifier = Modifier.fillMaxSize(),
                listState = listState,
                navController = navController,
                chatName = "채팅 미리보기"
            )

            // 삭제 확인 다이얼로그
            if (showDeleteConfirm) {
                ConfirmDialog(
                    title = "채팅방 삭제",
                    message = "이 채팅방을 삭제할까요?",
                    onConfirm = {
                        showDeleteConfirm = false
                        viewModel.deleteChatRoom(roomId) { ok, err ->
                            if (ok) {
                                // 스낵바/토스트 노출 후 리스트로 이동
                                navController.popBackStack()
                            } else {
                                // 에러 메시지 노출
                                Log.e("Chat", "삭제 실패: $err")
                            }
                        }
                    },
                    onDismiss = { showDeleteConfirm = false }
                )
            }

            // 신고 확인 다이얼로그
            if (showReportConfirm) {
                ConfirmDialog(
                    title = "신고하기",
                    message = "이 채팅방을 신고(차단)할까요?",
                    onConfirm = {
                        showReportConfirm = false
                        viewModel.reportChatRoom(roomId) { ok, err ->
                            if (ok) {
                                viewModel.refreshChatUsers(senderId = senderId)         // ✅ 신고 후 새로고침
                                navController.popBackStack()
                            } else {
                                Log.e("Chat", "신고 실패: $err")
                            }
                        }
                    },
                    onDismiss = { showReportConfirm = false }
                )
            }
        }
    }
}

@Composable
fun ChatScreenContent(
    chatItems: List<ChatItem>,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    navController: NavController,
    chatName: String
) {
    LazyColumn(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(bottom = 12.dp), // 마지막 아이템이 인풋에 가려지지 않게
        state = listState
    ) {
        item { WarningBanner() }
        items(chatItems) { item ->
            when (item) {
                is ChatItem.TextMessage -> {
                    ChatBubble(
                        message = item.message,
                        isMine = item.isMine,
                        profileImage = item.profileImageRes?.let { painterResource(id = it) }
                    )
                }
                is ChatItem.DateHeader -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.date, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun WarningBanner() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 12.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "notification",
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF6F3DD2)
        )
        Text(
            "연락처, 주소 등 민감한 개인정보는 채팅을 통해 공유하지 마세요.",
            fontSize = 10.sp,
            color = Color(0xFF6F3DD2)
        )
        Text(
            "직접 만나실 경우, 안전한 공공장소에서 만나시길 바랍니다.",
            fontSize = 10.sp,
            color = Color(0xFF6F3DD2)
        )
    }
}

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        androidx.compose.material3.Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(title, fontSize = 16.sp, color = Color.Black)
                Text(message, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) { Text("취소") }
                    androidx.compose.material3.TextButton(onClick = onConfirm) { Text("확인") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val navController = rememberNavController()
    val chatItems = listOf(
        ChatItem.DateHeader("8월 6일"),
        ChatItem.TextMessage("안녕하세요!", isMine = false, profileImageRes = R.drawable.default_profile),
        ChatItem.TextMessage("안녕하세요~ 반가워요!", isMine = true)
    )

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("채팅 미리보기", fontSize = 14.sp) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            ChatInput(
                senderId = 1,
                receiverId = 2,
                onSendMessage = { _, _, _ -> }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        ChatScreenContent(
            chatName = "채팅 미리보기",
            chatItems = chatItems,
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            listState = listState
        )
    }
}