package com.housweet.presentation.ui.chat

import android.R.attr.text
import android.R.attr.top
import android.R.id.message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatScreen(chatName: String) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
//        Text(text = "채팅 상대: $chatName", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        TopAppBar(
            title={Text(text = chatName)},
            backgroundColor = Color.White,
            elevation = 0.dp
        )

        //안내 문구
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F9F9))
                .padding(16.dp)
        ) {
            Text(
                text = "연락처, 주소 등 민감한 개정보는 채팅을 통해 공유하지 마세요.",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Text(
                text = "직접 만날 경우, 안전한 공공장소에서 만나시기 바랍니다.",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        //날짜
        Text(
            text = "3월 8일",
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 12.sp,
            color = Color.Gray
        )

        //채팅 목록
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                //왼쪽 말풍선
                ChatBubble(
                    message = "안녕하세요",
                    isMine = false
                )
            }
            item {
                //오른쪽 말풍선
                ChatBubble(
                    message = "안녕하세요",
                    isMine = true
                )
            }
            item {
                ChatBubble  (
                    message = "집 문의하고 싶어서 연락드렸어요. 지금도 메이트 구하시나요?",
                    isMine = false
                )
            }
        }

        //입력창
        ChatInput()
    }
}