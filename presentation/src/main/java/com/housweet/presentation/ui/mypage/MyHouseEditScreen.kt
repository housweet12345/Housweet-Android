package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "뒤로가기"
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "마이하우스 수정", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 하우스 이름
        Text(text = "하우스 이름")
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
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 방 시작일
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "방 시작일")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = startDate)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 초대 코드
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "초대 코드")
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "refresh",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onCodeRefresh() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = inviteCode)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 방 삭제하기
        Text(
            text = "방 삭제하기",
            color = Color.Red,
            modifier = Modifier.clickable { onDelete() }
        )

        Spacer(modifier = Modifier.weight(1f))

        // 완료 버튼
        Button(
            onClick = { onComplete() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("완료")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}