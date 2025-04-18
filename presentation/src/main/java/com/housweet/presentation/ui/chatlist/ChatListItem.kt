package com.housweet.presentation.ui.chatlist

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.domain.model.ChatPreview

//채팅 항목 한 줄
@Composable
fun ChatListItem(chat: ChatPreview, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{}
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)){
            Text(chat.name, fontWeight = FontWeight.Bold)
            Text(chat.lastMessage, color=Color.Gray)
        }
        Text(chat.time, fontSize=12.sp, color=Color.Gray)
    }
}