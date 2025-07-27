package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.icons.Icons
import androidx.compose.material.rememberDismissState
import androidx.compose.material.*
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.housweet.presentation.R
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NoticeScreen(onBackClick: () -> Unit = {}, navController: NavController) {
    val notices = remember {
        mutableStateListOf(
            Notice(
                date = "2025.06.05",
                title = "새롭게 업데이트 되었어요!",
                checked = true,
                id = "3",
                content = "이번에 새로 추가된 가계부 기능으로 편하게 방세 및 관리비를 기록하세요!"
            ),
            Notice(
                date = "2025.06.05",
                title = "새롭게 업데이트 되었어요!",
                checked = false,
                id = "2",
                content = "이번에 새로 추가된 가계부 기능으로 편하게 방세 및 관리비를 기록하세요!"
            ),
            Notice(
                date = "2025.06.05",
                title = "새롭게 업데이트 되었어요!",
                checked = false,
                id = "1",
                content = "이번에 새로 추가된 가계부 기능으로 편하게 방세 및 관리비를 기록하세요!"
            )
        )

    }

    val scope = rememberCoroutineScope()

    val dismissState = rememberDismissState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title={
                    Text(
                        text = "공지사항",
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
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            items(
                items = notices,
                key = { it.id }
            ) { notice ->
                val dismissState = rememberDismissState()

                if (dismissState.isDismissed(DismissDirection.StartToEnd) ||
                    dismissState.isDismissed(DismissDirection.EndToStart)
                ) {
                    LaunchedEffect(key1 = notice) {
                        notices.remove(notice)
                    }
                }

                SwipeToDismiss(
                    state = dismissState,
                    background = {},
                    dismissContent = {
                        NoticeItem(notice = notice) {
                            navController.navigate(
                                "noticeDetail/${notice.date}/${notice.title}/${notice.content}"
                            )
                        }
                    },
                    directions = setOf(
                        DismissDirection.EndToStart,
                        DismissDirection.StartToEnd
                    )
                )
            }

        }
    }
}

@Composable
fun NoticeItem(notice: Notice, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Notice",
            tint = if (notice.checked) Color(0xFF684FFF) else Color.Gray,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 8.dp)
        )

        Column {
            Text(
                text = notice.date,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = notice.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

data class Notice(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val title: String,
    val content: String,
    val checked: Boolean
)