package com.housweet.presentation.ui.profile.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.domain.model.profile.ProfileUpdateModel
import com.housweet.presentation.ui.profile.component.BottomButton
import com.housweet.presentation.ui.profile.component.MultiSelectableTagSection
import com.housweet.presentation.ui.profile.component.ProfileEditCaseNumber
import com.housweet.presentation.ui.profile.component.ProfileTopBar
import com.housweet.presentation.ui.profile.component.SimpleSegmentedControl
import com.housweet.presentation.ui.profile.state.ProfileInfo
import com.housweet.presentation.ui.theme.ColorGroup

@Composable
fun EditProfileSelectKeyWordScreen(
    currentProfile: ProfileInfo, // 현재 프로필 정보 추가
    showSkipButton: Boolean = false, // 건너뛰기 버튼 표시 여부
    onBackClick: () -> Unit = {},
    onNextClick: (ProfileUpdateModel) -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    // 각 섹션별 선택된 태그를 관리하는 상태
    var lifePatternTags by remember { mutableStateOf(setOf<String>()) }
    var organizationHabitTags by remember { mutableStateOf(setOf<String>()) }
    var personalityTags by remember { mutableStateOf(setOf<String>()) }
    var mbtiState by remember {
        mutableStateOf(
            MbtiState.fromString(currentProfile.mbti)
        )
    }

    // 키워드 목록
    val lifePatternKeywords = listOf(
        "미흡연자", "저녁형", "조용한 환경 선호", "음악, 소음 OK", "전화를 자주함", "비흡연자",
        "흡연자", "술은 적당히", "술을 즐기는 편", "요리를 자주 함", "음식은 사먹는 편", "냉장고 음식 공유 가능"
    )
    val organizationHabitKeywords = listOf(
        "깔끔한 스타일", "청소를 자주하는 편", "정리는 적당히", "공용 공간 정리 철저",
        "빨래를 자주 돌림", "설거지를 자주함"
    )
    val personalityKeywords = listOf("대화를 좋아함", "혼자있는 걸 좋아함")

    // 다음 버튼 클릭 처리
    val handleNextClick = {
        // 모든 선택된 정보를 모아서 ProfileUpdateModel 생성
        val allSelectedTags = (lifePatternTags + organizationHabitTags + personalityTags).toList()
        val selectedMbti = mbtiState.getMbtiType()

        val profileUpdateModel = ProfileUpdateModel(
            gender = currentProfile.gender, // 기존 정보 유지
            introduce = currentProfile.introduce, // 기존 정보 유지
            mbti = selectedMbti, // 새로 선택된 MBTI
            nickname = currentProfile.nickname, // 기존 정보 유지
            tags = allSelectedTags, // 새로 선택된 태그들
            yearOfBirth = currentProfile.yearOfBirth // 기존 정보 유지
        )

        onNextClick(profileUpdateModel)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp), // 상단 여백 제거
        bottomBar = {
            if (showSkipButton) {
                DoubleBottomButtons(
                    leftText = "완료",
                    rightText = "건너뛰기",
                    onLeftClick = handleNextClick,
                    onRightClick = onSkipClick
                )
            } else {
                BottomButton(
                    text = "완료",
                    onClick = handleNextClick
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()), // 스크롤 가능하도록 설정
        ) {
            ProfileTopBar(
                title = "프로필 수정",
                moreIconButton = false,
                onBackClick = onBackClick
            )

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
            Spacer(modifier = Modifier.height(8.dp))
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
                val newSelection = if (mbtiState.ei == selected) "" else selected
                updateMbti(mbtiState.copy(ei = newSelection))
            }
        )

        // S vs N
        SimpleSegmentedControl(
            option1 = "S",
            option2 = "N",
            selectedOption = mbtiState.sn,
            onOptionSelected = { selected ->
                val newSelection = if (mbtiState.sn == selected) "" else selected
                updateMbti(mbtiState.copy(sn = newSelection))
            }
        )

        // T vs F
        SimpleSegmentedControl(
            option1 = "T",
            option2 = "F",
            selectedOption = mbtiState.tf,
            onOptionSelected = { selected ->
                val newSelection = if (mbtiState.tf == selected) "" else selected
                updateMbti(mbtiState.copy(tf = newSelection))
            }
        )

        // P vs J
        SimpleSegmentedControl(
            option1 = "P",
            option2 = "J",
            selectedOption = mbtiState.pj,
            onOptionSelected = { selected ->
                val newSelection = if (mbtiState.pj == selected) "" else selected
                updateMbti(mbtiState.copy(pj = newSelection))
            }
        )
    }
}

data class MbtiState(
    val ei: String = "",
    val sn: String = "",
    val tf: String = "",
    val pj: String = ""
) {
    fun getMbtiType(): String {
        val result = "${ei.ifEmpty { "X" }}${sn.ifEmpty { "X" }}${tf.ifEmpty { "X" }}${pj.ifEmpty { "X" }}"
        return result
    }
    companion object {
        fun fromString(mbti: String): MbtiState {
            var ei = ""
            var sn = ""
            var tf = ""
            var pj = ""
            
            mbti.forEach { char ->
                when (char.uppercaseChar()) {
                    'E' -> ei = "E"
                    'I' -> ei = "I"
                    'S' -> sn = "S"
                    'N' -> sn = "N"
                    'T' -> tf = "T"
                    'F' -> tf = "F"
                    'P' -> pj = "P"
                    'J' -> pj = "J"
                }
            }
            
            return MbtiState(
                ei = ei,
                sn = sn,
                tf = tf,
                pj = pj
            )
        }
    }
}

@Composable
private fun DoubleBottomButtons(
    leftText: String,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit
) {
    Column(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onLeftClick,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorGroup.Primary
                ),
                shape = RectangleShape
            ) {
                Text(
                    text = leftText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            
            Button(
                onClick = onRightClick,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorGroup.White_F8F8F8
                ),
                shape = RectangleShape
            ) {
                Text(
                    text = rightText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorGroup.Gray_7E7E7E
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditProfileSelectKeyWordScreenPreview() {
    EditProfileSelectKeyWordScreen(
        ProfileInfo()
    )
}