package com.housweet.presentation.ui.chatlist

import android.util.Base64
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.domain.model.ChatPreview
import com.housweet.domain.model.ChatUser
import com.housweet.presentation.R
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

    val chatUsersState = viewModel.chatUsers.collectAsState()
    ChatListContent(navController = navController, chatUsers = chatUsersState.value, onBackClick = onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun ChatListContent(
    navController: NavController,
    chatUsers: List<ChatUser>,
    onBackClick: () -> Unit,
) {
    val myUserId = 3
    val filteredUsers = chatUsers.filter { it.id != myUserId }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "채팅", fontSize = 14.sp) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { onBackClick() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
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
                itemsIndexed(filteredUsers) { _, user ->
                    ChatListItem(
                        chat = ChatPreview(
                            name = user.username,
                            lastMessage = "말이 없어요",
//                            lastMessage = "",
                            time = "12:34",
//                            time = "",
                            profileImageUrl = "",
                            unread = false
                        ),
                        onClick = {
                            val encodedName = Base64.encodeToString(
                                user.username.toByteArray(),
                                Base64.URL_SAFE or Base64.NO_WRAP
                            )
                            navController.navigate("chat_detail/${user.id}/$encodedName")
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
    val mockUsers = listOf(
        ChatUser(1, "정희철", "test1@example.com"),
        ChatUser(2, "이윤현", "test2@example.com"),
        ChatUser(3, "박민규", "test3@example.com")
    )

    ChatListContent(navController = navController, chatUsers = mockUsers, onBackClick = {})
}