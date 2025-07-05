package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MyPageScreen(navController: NavController, onBackClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "마이페이지", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Profile Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFFF7F7F7), shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "김지안", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "20대 남자", fontSize = 14.sp, color = Color.Gray)
            }
        }

        // Roommate Section
        SectionTitle("룸메이트")
        MyPageMenuItem("북마크") {
            navController.navigate("bookmark")
        }
        MyPageMenuItem("올린 방 관리") {
            navController.navigate("posted_my_room")
        }
        MyPageMenuItem("마이하우스") {
            navController.navigate("myhousedetail")
        }

        Divider(color = Color(0xFFEEEEEE), thickness = 8.dp)

        // Support Section
        SectionTitle("고객 지원")
        MyPageMenuItem("앱 설정")
        MyPageMenuItem("공지사항") {
            navController.navigate("notice") {
                launchSingleTop = true
            }
        }
        MyPageMenuItem("도움말")
        MyPageMenuItem("약관 및 정책")

        // Feedback CTA
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(Color(0xFF6A5ACD), shape = RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "하우스잇이 도움이 되었나요?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "여러분의 의견을 들려주세요!",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // 마지막 여백
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun MyPageMenuItem(text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontSize = 15.sp)
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}