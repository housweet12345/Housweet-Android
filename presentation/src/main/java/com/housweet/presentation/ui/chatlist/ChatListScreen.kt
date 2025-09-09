package com.housweet.presentation.ui.chatlist

import android.util.Base64
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.domain.model.ChatPreview
import com.housweet.domain.model.ChatUser
import com.housweet.presentation.ui.chat.toKstKoreanFull
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.viewmodel.chatlist.ChatListViewModel
import kotlin.io.encoding.ExperimentalEncodingApi


@Composable
fun ChatListScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    BackHandler {
        onBackClick()
    }

    val myIdState = viewModel.myUserId.collectAsState()
    val chatUsersState = viewModel.chatUsers.collectAsState()

    val myId = myIdState.value ?: run { return }

    val visibleUsers = remember(chatUsersState.value) {
        chatUsersState.value.filter { !it.is_blocked }      // 차단 숨김
    }

    ChatListContent(navController = navController, senderId = myId, chatUsers = visibleUsers, onBackClick = onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun ChatListContent(
    navController: NavController,
    senderId: Int,
    chatUsers: List<ChatUser>,
    onBackClick: () -> Unit,
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                text = "채팅",
                onBackBtnClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start
        ) {
            LazyColumn(
                modifier = Modifier.background(Color.White)
            ) {
                itemsIndexed(chatUsers) { _, user ->
                    val formatted = runCatching<String> {
                        user.last_message_created_at.toKstKoreanFull()
                    }.getOrDefault(user.last_message_created_at)

                    val last = user.last_message_content.ifBlank { "대화를 시작해보세요" }
                    val time = user.last_message_created_at
                        .takeIf { it.isNotBlank() }                                         // 비어있으면 건너뜀
                        ?.let { runCatching { it.toKstKoreanFull() }.getOrNull() }          // 포맷 실패도 안전
                        .orEmpty()

                    ChatListItem(
                            chat = ChatPreview(
                            name = user.receiver_nickname,
                            lastMessage = last,
                            time = time,
                            profileImageUrl = "",
                            unread = false
                        ),
                        onClick = {
                            val displayName = user.counterpart_nickname
                            val encodedName = Base64.encodeToString(
                                displayName.toByteArray(),
                                Base64.URL_SAFE or Base64.NO_WRAP
                            )
                            navController.navigate("chat_detail/$senderId/${user.counterpart_id}/${user.room_id}/$encodedName")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    val navController = rememberNavController()
    val mockSenderId = 3
    val mockUsers = listOf(
        ChatUser(
            room_id = 1,
            sender_id = 3,
            receiver_id = 1,
            created_at = "2025-08-15T09:38:19",
            updated_at = "2025-08-15T09:38:19",
            is_blocked = false,
            counterpart_id = 1,
            sender_nickname = "테스트3",
            receiver_nickname = "테스트1",
            counterpart_nickname = "테스트1",
            last_message_content = "어쩌라구요",
            last_message_created_at = "2025.08.15",
        ),
        ChatUser(
            room_id = 2,
            sender_id = 3,
            receiver_id = 2,
            created_at = "2025-08-15T09:38:19",
            updated_at = "2025-08-15T09:38:19",
            is_blocked = false,
            counterpart_id = 2,
            sender_nickname = "테스트3",
            receiver_nickname = "테스트2",
            counterpart_nickname = "테스트2",
            last_message_content = "어쩌라구요2",
            last_message_created_at = "2025.08.15",
        ),
        ChatUser(
            room_id = 3,
            sender_id = 3,
            receiver_id = 4,
            created_at = "2025-08-15T09:38:19",
            updated_at = "2025-08-15T09:38:19",
            is_blocked = false,
            counterpart_id = 4,
            sender_nickname = "테스트3",
            receiver_nickname = "테스트4",
            counterpart_nickname = "테스트4",
            last_message_content = "어쩌라구요4",
            last_message_created_at = "2025.08.15",
        )
    )

    ChatListContent(navController = navController, senderId = mockSenderId, chatUsers = mockUsers, onBackClick = {})
}