package com.housweet.presentation.ui.registerhouse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.common.StepIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.housweet.presentation.ui.common.TopBarWithBackButton
import com.housweet.presentation.ui.theme.Purple40

@Composable
fun HouseRegisterScreen1(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val selectedTags = remember { mutableStateListOf<String>() }

    val sections = listOf(
        "교통" to listOf("저녁형", "조용한 환경 선호", "음악, 소음 OK", "전화 통화 자유", "비흡연자",
            "흡연자", "알쓰", "애주가", "요리한 음식 선호", "배달 음식 선호", "음식 공유 가능"),
        "집의 크기" to listOf("깔끔한 스타일", "청소 자주 함", "정리는 적당히", "거실 정리 필수", "빨래 자주 함", "설거지 자주 함"),
        "인프라" to listOf("외향적", "내향적")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
    ) {
        // 상단바 + 제목
        TopBarWithBackButton(
            title = "하우스 올리기",
            onBackClick = onBackClick
        )

        StepIndicator(currentStep = 1)

        Spacer(modifier = Modifier.height(8.dp))

        // 중앙 정렬된 콘텐츠 (텍스트 + 태그 선택)
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "집에 대한 키워드를 선택해주세요.",
                color = Color(0xFF6C4DFF),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            sections.forEach { (title, tags) ->
                Text(
                    text = title,
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tags) { tag ->
                        val isSelected = tag in selectedTags
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) Color(0xFF665ED3) else Color.White,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    if (isSelected) selectedTags.remove(tag)
                                    else selectedTags.add(tag)
                                }
                                .align(Alignment.CenterHorizontally)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF665ED3),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Text(
                                text = tag,
                                color = if (isSelected) Color.White else Color.Black,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNextClick,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF665ED3),
                contentColor = Color.White
            )
        ) {
            Text(text = "다음")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
