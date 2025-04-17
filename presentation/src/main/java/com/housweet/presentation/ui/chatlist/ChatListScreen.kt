package com.housweet.presentation.ui.chatlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.housweet.domain.model.dummyChatList


//UI 컴포저블
@Composable
fun ChatListScreen() {
    Column {
        TopAppBar(title={Text("채팅")})
        LazyColumn{
            items(dummyChatList) {chat ->
                ChatListItem(chat) {
                    //상세 화면으로 이동
                }
            }
        }
    }
}