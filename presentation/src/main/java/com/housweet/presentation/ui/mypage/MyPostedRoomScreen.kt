package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostedRoomScreen() {
    var selectedTab by remember { mutableStateOf(0) } // 0: 게시중, 1: 숨김
    val tabTitles = listOf("게시중", "숨김")

    // 모달 상태
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("올린 방 관리") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: 뒤로가기 */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // 방 올리기 버튼
            Button(
                onClick = { /* TODO: 방 올리기 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE6FF))
            ) {
                Text("방 올리기", color = Color(0xFF6C5CE7))
            }

            // 탭
            TabRow(selectedTabIndex = selectedTab) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // 방 목록 (더미)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(2) {
                    RoomItem(onMenuClick = { showSheet = true })
                }
            }
        }

        // 🛠️ Modal Bottom Sheet
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "게시글 숨기기",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clickable {
                                // TODO: 숨기기 동작
                                showSheet = false
                            }
                            .padding(16.dp)
                    )
                    Text(
                        text = "게시글 삭제",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .clickable {
                                // TODO: 삭제 동작
                                showSheet = false
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RoomItem(onMenuClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(70.dp)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text("애완동물 좋아하는 사람을 구하고 있습니다.")
                Text("보증금 400 월세 20", fontWeight = FontWeight.Bold)
                Text("송파구 문정동 · 20대 남자", fontSize = 12.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(8.dp))

        Row {
            Button(
                onClick = { /* TODO: 글 수정하기 */ },
                modifier = Modifier.width(280.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F7F7))
            ) {
                Text("글 수정하기", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(18.dp))
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.width(70.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFF7F7F7)
                )
            ) {
                Icon(Icons.Default.MoreHoriz, contentDescription = "More")
            }
        }
    }
}