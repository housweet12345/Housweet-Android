package com.housweet.presentation.ui.registerhouse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
        "교통" to listOf("버스정류장 인근", "역세권", "버스, 지하철 더블 역세권", "자차 추천", "교통 좋음",
            "교통 나쁘지 않음"),
        "집 상태" to listOf("쾌적함", "채광 좋음", "뷰가 좋음", "환기 잘 됨", "리모델링함", "관리 잘 됨", "방음 잘 됨", "층간 소음 없음", "풀옵션", "냉난방 잘 됨", "저층", "고층", "엘레베이터", "벌레 없음"),
        "인프라" to listOf("마트", "편의점", "카페", "식당", "병원", "공원", "산책로", "숲세권", "약국", "전통시장", "헬스장", "대학가", "상권 많음"),
        "기타" to listOf("치안 좋음", "밤길 안전함", "전입신고 가능", "전입신고 불가능", "단기 거주 가능", "장기 거주 가능", "장기 거주 희망", "즉시 입주 가능", "입주일 상의 필요", "월세 및 보증금 협의 가능", "월세 및 보증금 협의 불가")
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
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
            Text(
                text = "다음",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
