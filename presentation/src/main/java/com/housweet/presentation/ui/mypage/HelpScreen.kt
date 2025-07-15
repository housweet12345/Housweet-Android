package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen() {
    val faqList = listOf(
        "마이하우스는 무엇인가요?",
        "마이하우스는 무엇인가요?",
        "마이하우스는 무엇인가요?",
        "마이하우스는 무엇인가요?"
    )

    val expandedStates = remember { mutableStateListOf(*Array(faqList.size) { false }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("도움말") },
                navigationIcon = {
                    IconButton(onClick = { /* 뒤로가기 */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 카테고리 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("카테고리", "카테고리", "카테고리").forEachIndexed { index, text ->
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, if (index == 0) Color(0xFF5B51FE) else Color.Gray),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (index == 0) Color(0xFF5B51FE) else Color.Gray
                        )
                    ) {
                        Text(text)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FAQ 리스트
            faqList.forEachIndexed { index, question ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White)
                        .clickable {
                            expandedStates[index] = !expandedStates[index]
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = question, style = MaterialTheme.typography.bodyLarge)
                        Icon(
                            imageVector = if (expandedStates[index]) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand"
                        )
                    }

                    if (expandedStates[index]) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "마이하우스는 현재 방을 같이 살고 있는 사람과 함께 배려하며 살기 좋도록 기획한 서비스입니다.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                Divider()
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 문의 안내 박스
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFF5B51FE),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "그 외 궁금한 점이 있으신가요?",
                        color = Color(0xFF5B51FE),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1:1 문의로 해결하세요!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}