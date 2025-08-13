package com.housweet.presentation.ui.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.domain.model.NotificationModel
import com.housweet.presentation.R
import com.housweet.presentation.viewmodel.mypage.NotificationViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val sampleNotifications = listOf(
    NotificationModel(1, "room_mate", "문정동에서 룸메이트를 구하고 있어요", "2025-08-06T11:30:00Z"),
    NotificationModel(2, "my_house", "'김지안'님께서 게시글을 작성했어요.", "2025-08-05T11:30:00Z"),
    NotificationModel(3, "etc", "'김지안'님께서 게시글을 작성했어요...", "2025-08-04T09:15:00Z")
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel? = if (!LocalInspectionMode.current) hiltViewModel() else null,
    previewData: List<NotificationModel>? = null
) {
    val notifications by viewModel?.notifications?.collectAsState() ?: remember { mutableStateOf(emptyList()) }

    // ✅ previewData 가 null이 아니면 그걸 쓰고, 아니면 viewModel 사용
    val displayList = previewData ?: notifications

    LaunchedEffect(previewData) {
        if (previewData == null) {
            viewModel?.fetchNotifications()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title = { Text(text = "알림", fontSize = 14.sp) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                items = displayList,
                key = { it.id }
            ) { notification ->
                NotificationItem(notification = notification)
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = when (notification.type) {
                "room_mate" -> "룸메이트 찾기"
                "my_house" -> "마이 하우스"
                else -> "알림"
            },
            color = Color(0xFF665ED3),
            fontSize = 10.sp
        )
        Text(
            text = notification.content,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = formatDate(notification.createdAt),
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
        )
        Divider(modifier = Modifier.padding(top = 12.dp))
    }
}

fun formatDate(date: String): String {
    return try {
        val parsedDate = ZonedDateTime.parse(date)
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.getDefault())
        parsedDate.format(formatter)
    } catch (e: Exception) {
        "날짜 오류"
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(
        navController = rememberNavController(),
        previewData = sampleNotifications
    )
}