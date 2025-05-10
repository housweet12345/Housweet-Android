package com.housweet.presentation.ui.profile.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.profile.component.BottomButton
import com.housweet.presentation.ui.profile.component.MultiSelectableTagSection
import com.housweet.presentation.ui.profile.component.ProfileEditCaseNumber
import com.housweet.presentation.ui.profile.component.SimpleSegmentedControl
import com.housweet.presentation.ui.theme.ColorGroup

@Composable
fun EditProfileSelectKeyWordScreen(
    onNextClick: () -> Unit = {}
) {
    // 각 섹션별 선택된 태그를 관리하는 상태
    var lifePatternTags by remember { mutableStateOf(setOf<String>()) }
    var organizationHabitTags by remember { mutableStateOf(setOf<String>()) }
    var personalityTags by remember { mutableStateOf(setOf<String>()) }
    var mbtiState by remember { mutableStateOf(MbtiState()) }

    // 키워드 목록 (실제로는 API나 리소스에서 가져올 수 있음)
    val lifePatternKeywords = listOf("아침형", "저녁형", "밤샘 OK", "음주를 즐기는 편", "비흡연자", "흡연자", "요리를 자주 함", "배달음식 선호", "일찍 귀가", "늦게 귀가")
    val organizationHabitKeywords = listOf("깔끔한 편", "정리도 적당히", "청결 중시", "물건이 많음", "단순함 선호", "정리를 자주함", "규칙적인 생활", "즉흥적인 생활")
    val personalityKeywords = listOf("조용한 편", "활발한 편", "대화를 좋아함", "혼자 있는 시간 필요", "계획적", "즉흥적", "감성적", "이성적", "사교적", "내향적")

    // 다음 버튼 클릭 처리
    val handleNextClick = {
        // 모든 선택된 정보를 모아서 처리
        val allSelectedTags = lifePatternTags + organizationHabitTags + personalityTags
        val selectedMbti = mbtiState.getMbtiType()

        // 다음 화면으로 이동
        onNextClick()
    }

    Scaffold(
        bottomBar = {
            BottomButton(
                text = "완료",
                onClick = handleNextClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()), // 스크롤 가능하도록 설정
        ) {
            // 상단 단계 표시
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileEditCaseNumber(
                    isCurrent = false,
                    number = 1,
                    description = "프로필 설정"
                )
                Spacer(modifier = Modifier.width(36.dp))
                ProfileEditCaseNumber(
                    isCurrent = true,
                    number = 2,
                    description = "키워드 선택"
                )
            }

            // 안내 텍스트
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "나를 표현하는 키워드를 체크해주세요.",
                fontSize = 14.sp,
                style = TextStyle(
                    color = ColorGroup.Primary
                ),
                fontWeight = Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            // 생활 패턴 섹션
            SectionTitle(title = "생활 패턴")
            MultiSelectableTagSection(
                tags = lifePatternKeywords,
                selectedTags = lifePatternTags,
                onSelectionChanged = { lifePatternTags = it },
            )

            Spacer(Modifier.height(16.dp))

            // 정리 습관 섹션
            SectionTitle(title = "정리 습관")
            MultiSelectableTagSection(
                tags = organizationHabitKeywords,
                selectedTags = organizationHabitTags,
                onSelectionChanged = { organizationHabitTags = it },
            )

            Spacer(Modifier.height(16.dp))

            // 성격 섹션
            SectionTitle(title = "성격")
            MultiSelectableTagSection(
                tags = personalityKeywords,
                selectedTags = personalityTags,
                onSelectionChanged = { personalityTags = it },
            )

            Spacer(Modifier.height(16.dp))

            // MBTI 섹션
            SectionTitle(title = "MBTI")
            MbtiSection(
                initialMbti = mbtiState,
                onMbtiChanged = { mbtiState = it }
            )
        }
    }
}

@Composable
private fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        fontSize = 12.sp,
        text = title,
        fontWeight = Bold,
        color = Color.Black
    )
}

@Composable
private fun MbtiSection(
    modifier: Modifier = Modifier,
    initialMbti: MbtiState = MbtiState(),
    onMbtiChanged: (MbtiState) -> Unit = {}
) {

    var mbtiState by remember { mutableStateOf(initialMbti) }

    // MBTI 변경 시 콜백 호출
    val updateMbti = { newState: MbtiState ->
        mbtiState = newState
        onMbtiChanged(newState)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // E vs I
        SimpleSegmentedControl(
            option1 = "E",
            option2 = "I",
            selectedOption = mbtiState.ei,
            onOptionSelected = { selected ->
                updateMbti(mbtiState.copy(ei = selected))
            }
        )

        // S vs N
        SimpleSegmentedControl(
            option1 = "S",
            option2 = "N",
            selectedOption = mbtiState.sn,
            onOptionSelected = { selected ->
                updateMbti(mbtiState.copy(sn = selected))
            }
        )

        // T vs F
        SimpleSegmentedControl(
            option1 = "T",
            option2 = "F",
            selectedOption = mbtiState.tf,
            onOptionSelected = { selected ->
                updateMbti(mbtiState.copy(tf = selected))
            }
        )

        // P vs J
        SimpleSegmentedControl(
            option1 = "P",
            option2 = "J",
            selectedOption = mbtiState.pj,
            onOptionSelected = { selected ->
                updateMbti(mbtiState.copy(pj = selected))
            }
        )
    }
}

data class MbtiState(
    val ei: String = "E",
    val sn: String = "S",
    val tf: String = "T",
    val pj: String = "P"
) {
    fun getMbtiType(): String = "$ei$sn$tf$pj"
}

@Preview
@Composable
private fun EditProfileSelectKeyWordScreenPreview() {
    EditProfileSelectKeyWordScreen()
}