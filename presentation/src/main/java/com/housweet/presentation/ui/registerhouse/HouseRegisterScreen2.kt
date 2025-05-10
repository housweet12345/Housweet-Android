package com.housweet.presentation.ui.registerhouse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.common.StepIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun HouseRegisterScreen2(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // 상태 변수들
    var region by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var moveInDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 상단바
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
            Text(
                text = "하우스 올리기",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // 단계 표시기
        StepIndicator(currentStep = 2)

        Spacer(modifier = Modifier.height(16.dp))

        // 지역 선택 버튼
        Text(
            text = "지역을 선택해주세요",
            color = Color(0xFF6C4DFF),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // 지역 선택 로직 추가
                }
                .padding(vertical = 12.dp)
                .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 제목 입력 필드
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 설명 입력 필드
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("자세한 설명") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 보증금 입력 필드
        OutlinedTextField(
            value = deposit,
            onValueChange = { deposit = it },
            label = { Text("보증금") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 월세 입력 필드
        OutlinedTextField(
            value = monthlyRent,
            onValueChange = { monthlyRent = it },
            label = { Text("월세") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 입주 가능일 입력 필드
        OutlinedTextField(
            value = moveInDate,
            onValueChange = { moveInDate = it },
            label = { Text("입주 가능일") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        // 다음 버튼
        Button(
            onClick = onNextClick,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "다음")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
