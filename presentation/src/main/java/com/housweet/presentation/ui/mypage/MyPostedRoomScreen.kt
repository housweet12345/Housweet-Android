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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.housweet.presentation.R
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostedRoomScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) } // 0: 게시중, 1: 숨김
    val tabTitles = listOf("게시중", "숨김")

    // 모달 상태
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    val sampleRoomPosts = listOf(
        RoomPost("애완동물 좋아하는 사람을 구하고 있습니다.", "보증금 400 월세 20", "송파구 문정동 · 20대 남자"),
        RoomPost("깔끔한 사람을 구해요!", "보증금 300 월세 25", "강남구 역삼동 · 30대 여자")
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            // TopAppBar
            CenterAlignedTopAppBar(
                title={
                    androidx.compose.material.Text(
                        text = "올린 방 관리",
                        fontSize = 14.sp
                    )
                },
                navigationIcon = {
                    androidx.compose.material.Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // ✅ 배경색 흰색 지정
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).background(Color.White)) {
            // 방 올리기 버튼
            Button(
                onClick = { /* TODO: 방 올리기 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE6FF)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("방 올리기", color = Color(0xFF6C5CE7), fontSize = 12.sp)
            }

            // 탭
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontSize = 12.sp) }
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = sampleRoomPosts) { room ->
                    RoomItem(
                        roomPost = room,
                        onMenuClick = { showSheet = true },
                        onEditClick = { /* TODO: 글 수정 동작 */ }
                    )
                }
            }

        }

        // 🛠️ Modal Bottom Sheet
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFFF8F8F8)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: 숨기기 동작
                                showSheet = false
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "게시글 숨기기",
                            fontSize = 14.sp,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: 삭제 동작
                                showSheet = false
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "게시글 삭제",
                            fontSize = 14.sp,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(
    roomPost: RoomPost,
    onMenuClick: () -> Unit,
    onEditClick: () -> Unit
) {
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
                Text(roomPost.title, fontSize = 12.sp)
                Text(roomPost.priceInfo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(roomPost.metaInfo, fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(8.dp))

        Row {
            Button(
                onClick = onEditClick,
                modifier = Modifier.width(280.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F7F7)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("글 수정하기", color = Color.Black, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(18.dp))

            Box(
                modifier = Modifier
                    .width(70.dp)
                    .clip(RoundedCornerShape(6.dp)) // ✅ radius 설정
                    .background(Color(0xFFF7F7F7))   // ✅ 배경색
            ) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.fillMaxSize(),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent // ✅ 배경 투명으로
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More",
                        modifier = Modifier.size(12.dp) // ← 아이콘 크기 조절
                    )
                }
            }

        }
    }
}