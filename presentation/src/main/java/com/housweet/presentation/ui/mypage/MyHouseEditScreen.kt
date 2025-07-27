package com.housweet.presentation.ui.mypage

import android.R.attr.contentDescription
import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.housweet.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHouseEditScreen(
    navController: NavController,
    houseName: String,
    startDate: String,
    inviteCode: String,
    onDelete: () -> Unit = {},
    onComplete: () -> Unit = {},
    onCodeRefresh: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf(houseName) }

    Scaffold (
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Title
            CenterAlignedTopAppBar(
                title={
                    Text(
                        text = "마이하우스 수정",
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

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "하우스 이름",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info), // ⓘ 아이콘
                        contentDescription = null,
                        modifier = Modifier
                            .size(14.dp),
                        tint = Color(0xFF665ED3) // 연보라색
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "10자 이내로 입력해주세요.",
                        fontSize = 10.sp,
                        color = Color(0xFF6C5CE7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    if (it.length <= 10) name = it
                },
                placeholder = { Text("10자 이내로 입력해주세요.", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,    // 포커스 되었을 때
                    unfocusedBorderColor = Color.LightGray,    // 포커스 없을 때
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 방 시작일
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "방 시작일", fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = startDate, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 초대 코드
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "초대 코드", fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.refresh),
                    contentDescription = "refresh",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onCodeRefresh() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = inviteCode, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 방 삭제하기
            Text(
                text = "방 삭제하기",
                color = Color.Red,
                modifier = Modifier.clickable { onDelete() },
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // 완료 버튼
            Button(
                onClick = { /* TODO: 마이하우스 수정 완료 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF665ED3)),
                shape = RoundedCornerShape(6.dp)
            ) {
                androidx.compose.material3.Text("완료", color = Color(0xFFF8F8F8), fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}