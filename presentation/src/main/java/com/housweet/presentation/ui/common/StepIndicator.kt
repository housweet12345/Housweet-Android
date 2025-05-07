package com.housweet.presentation.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepIndicator(currentStep: Int) {
    val steps = listOf("하우스 특징", "추가 정보 입력", "사진 업로드", "선호하는 사람")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, title ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${index + 1}",
                    color = if (currentStep == index + 1) Color(0xFF6C4DFF) else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = if (currentStep == index + 1) Color(0xFF6C4DFF) else Color.Gray
                )
            }
        }
    }
}