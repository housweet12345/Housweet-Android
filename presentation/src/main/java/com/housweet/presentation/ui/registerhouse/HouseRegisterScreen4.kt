package com.housweet.presentation.ui.registerhouse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.common.StepIndicator
import com.housweet.presentation.ui.common.TopBarWithBackButton

@Composable
fun HouseRegisterScreen4(
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    val selectedTags = remember { mutableStateListOf<String>() }

    val sections = listOf(
        "생활 패턴" to listOf(
            "미흡연자", "저녁형", "조용한 환경 선호", "음악, 소음 OK", "전화를 자주함", "비흡연자",
            "흡연자", "술은 적당히", "술을 즐기는 편", "요리를 자주 함", "음식은 사먹는 편", "냉장고 음식 공유 가능"
        ),
        "정리 습관" to listOf(
            "깔끔한 스타일", "청소를 자주하는 편", "정리는 적당히", "공용 공간 정리 철저",
            "빨래를 자주 돌림", "설거지를 자주함"
        ),
        "성격" to listOf("대화를 좋아함", "혼자있는 걸 좋아함")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        TopBarWithBackButton(
            title = "하우스 올리기",
            onBackClick = onBackClick
        )

        StepIndicator(currentStep = 4)

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "선호하는 사람에 대한 키워드를 선택해주세요.",
                color = Color(0xFF6C4DFF),
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sections.forEach { (title, tags) ->
                item {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                item {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            val isSelected = selectedTags.contains(tag)

                            Box(
                                modifier = Modifier
                                    .clickable {
                                        if (isSelected) selectedTags.remove(tag)
                                        else selectedTags.add(tag)
                                    }
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFF665ED3),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(
                                        color = if (isSelected) Color(0xFF665ED3) else Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCompleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF665ED3),
                contentColor = Color.White
            )
        ) {
            Text("완료")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
