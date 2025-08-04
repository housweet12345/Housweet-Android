package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.housweet.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNotificationSettingsScreen(
    navController: NavController
) {
    var isNotificationOn by remember { mutableStateOf(false) }

    Scaffold (
        containerColor = Color.White,
        topBar = {
            // 상단 AppBar
            CenterAlignedTopAppBar(
                title={
                    androidx.compose.material.Text(
                        text = "앱 설정",
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.Start
        ) {

            // 알림 켜기 토글
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "알림 켜기", fontSize = 14.sp)
                Switch(
                    checked = isNotificationOn,
                    onCheckedChange = { isNotificationOn = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFF8F8F8),     // 활성화된 thumb 색
                        checkedTrackColor = Color(0xFF665ED3),     // 활성화된 track 색
                        uncheckedThumbColor = Color(0xFFF8F8F8),     // 비활성화된 thumb 색
                        uncheckedTrackColor = Color(0xFFD9D9D9)    // 비활성화된 track 색
                    )
                )
            }

            // 세부 알림 설정들
            if (isNotificationOn) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "일반", fontSize = 12.sp, color = Color.Gray)
                SettingToggleItem("채팅 알림")

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "하우스메이트", fontSize = 12.sp, color = Color.Gray)
                SettingToggleItem("게시글 알림")
                SettingToggleItem("일정 알림")
                SettingToggleItem("가계부 알림")
            }
        }
    }
}

@Composable
fun SettingToggleItem(label: String) {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp)
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFF8F8F8),     // 활성화된 thumb 색
                checkedTrackColor = Color(0xFF665ED3),     // 활성화된 track 색
                uncheckedThumbColor = Color(0xFFF8F8F8),     // 비활성화된 thumb 색
                uncheckedTrackColor = Color(0xFFD9D9D9)    // 비활성화된 track 색
            )
        )
    }
}
