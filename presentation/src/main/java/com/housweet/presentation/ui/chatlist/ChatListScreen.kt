package com.housweet.presentation.ui.chatlist

import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.housweet.presentation.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.domain.model.ChatPreview
import com.housweet.domain.model.ChatUser
import com.housweet.presentation.viewmodel.chatlist.ChatListViewModel


//UI 컴포저블
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val chatUsersState = viewModel.chatUsers.collectAsState()
    val chatUsers: List<ChatUser> = chatUsersState.value

    val myUserId = 1 // TODO: 실제 로그인한 유저 ID로 교체
    val filteredUsers = chatUsers.filter { it.id != myUserId }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title={
                    Text(
                        text = "채팅",
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
                    containerColor = Color.White // ✅ 배경색 흰색 지정
                )       )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(innerPadding),
            horizontalAlignment = Alignment.Start
        ) {
            LazyColumn(
                modifier = Modifier.background(Color.White)
            ) {
                itemsIndexed(filteredUsers) { _, user ->
                    ChatListItem(
                        chat = ChatPreview(
                            name = user.username,
                            lastMessage = "",
                            time = "",
                            profileImageUrl = "",
                            unread = false
                        ),
                        onClick = {
                            val encodedName = Base64.encodeToString(user.username.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
                            navController.navigate("chat_detail/${user.id}/$encodedName")
                    })
                }
            }
        }
    }
}