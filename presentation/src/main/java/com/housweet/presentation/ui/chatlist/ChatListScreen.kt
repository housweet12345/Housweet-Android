package com.housweet.presentation.ui.chatlist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.housweet.domain.model.dummyChatList


//UI 컴포저블
@Composable
fun ChatListScreen() {
    Column {
        TopAppBar(title={Text("채팅")})
        LazyColumn {
            itemsIndexed(dummyChatList) { index, chat ->
                ChatListItem(chat = chat, onClick = {Log.d("ChatList", "Clicked: ${chat.name}")})
            }
        }
    }
}