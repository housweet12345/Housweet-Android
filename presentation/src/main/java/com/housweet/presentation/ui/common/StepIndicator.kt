package com.housweet.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R

@Composable
fun StepIndicator(currentStep: Int) {
    val steps = listOf("하우스 특징", "추가 정보 입력", "사진 업로드", "선호하는 사람")
    val stepImages: List<Pair<Int, Int>> = listOf(
        Pair(R.drawable.btn_purple_1, R.drawable.btn_gray_1),
        Pair(R.drawable.btn_purple_2, R.drawable.btn_gray_2),
        Pair(R.drawable.btn_purple_3, R.drawable.btn_gray_3),
        Pair(R.drawable.btn_purple_4, R.drawable.btn_gray_4),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp, start = 28.dp, end = 28.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, title ->
            val isActive = currentStep == index + 1
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = if(isActive) stepImages[index].first else stepImages[index].second),
                    contentDescription = "Step ${index + 1}",
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = title,
                    fontSize = 10.sp,
                    color = if (isActive) Color(0xFF6C4DFF) else Color.Gray,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(top= 8.dp)
                )
            }
        }
    }
}