package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberDismissState
import androidx.compose.material.*
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NoticeScreen(onBackClick: () -> Unit = {}) {
    val notices = remember {
        mutableStateListOf(
            Notice(
                date = "2025.06.05",
                title = "새롭게 업데이트 되었어요!",
                checked = true
            ),
            Notice(
                date = "2025.06.05",
                title = "새롭게 업데이트 되었어요!",
                checked = false
            ),
            Notice(
                date = "2025.06.05",
                title = "새롭게 업데이트 되었어요!",
                checked = false
            )
        )

    }

    val scope = rememberCoroutineScope()

    val dismissState = rememberDismissState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("공지사항") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
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
                        NoticeItem(notice)
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
fun NoticeItem(notice: Notice) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
    val checked: Boolean
)