package com.housweet.presentation.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items

@Composable
fun ChatScreen(chatName: String, navController: NavController) {
    //채팅 목록 상태 관리 (기본 메세지 3개)
    var messages by remember {
        mutableStateOf(
            listOf<Pair<String, Boolean>>(
                "안녕하세요" to false,
                "안녕하세요" to true,
                "집 문의하고 싶어서 연락드렸어요. 지금도 메이트 구하시나요?" to false
            )
        )
    }

    //입력값 상태 관리
    var inputText by remember {mutableStateOf("")}

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
//        Text(text = "채팅 상대: $chatName", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        TopAppBar(
            title={Text(text = chatName)},
            backgroundColor = Color.White,
            elevation = 0.dp,
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable { navController.popBackStack() }
                )
            }
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
            items(items = messages) { (msg, isMine) ->
                ChatBubble(message = msg, isMine = isMine)
            }
        }

        //입력창
        ChatInput(
            inputText = inputText,
            onTextChange = { inputText = it },
            onSend = {
                if(inputText.isNotBlank()) {
                    messages = messages + Pair(inputText, true)
                    inputText = ""
                }
            }
        )
    }
}