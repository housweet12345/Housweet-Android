package com.housweet.presentation.ui.chatlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.housweet.domain.model.dummyChatList


//UI 컴포저블
@Composable
fun ChatListScreen(navController: NavController) {
    Column {
        TopAppBar(title={Text("채팅")})
        LazyColumn {
            itemsIndexed(dummyChatList) { _, chat ->
                ChatListItem(chat = chat, onClick = {
                    navController.navigate("chat_detail/${chat.name}")
                })
            }
        }
    }
}