package com.housweet.presentation.ui.profile.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.housweet.presentation.ui.profile.component.BottomButton
import com.housweet.presentation.ui.profile.component.InfoMessage
import com.housweet.presentation.ui.profile.component.ProfileEditCaseNumber
import com.housweet.presentation.ui.profile.component.ProfileEditNameTextField
import com.housweet.presentation.ui.profile.component.ProfileImage
import com.housweet.presentation.ui.profile.component.ProfileTopBar
import com.housweet.presentation.ui.profile.component.ToggleButtonGroup
import com.housweet.presentation.ui.profile.component.YearPickerDropdown

@Composable
fun EditProfileScreen(
    name: String = "",
    yearOfBirth: String = "",
    gender: String = "",
    introduction: String = "",
    onBackClick: () -> Unit = {},
    onNextClick: (String, String, String, String) -> Unit = { _, _, _, _ -> }
) {
    // 상태 관리 추가
    var nameState by remember { mutableStateOf(name) }
    var yearOfBirthState by remember { mutableStateOf(yearOfBirth) }
    var genderState by remember { mutableStateOf(gender) }
    var introductionState by remember { mutableStateOf(introduction) }

    var selectedOption by remember {
        mutableIntStateOf(
            when (gender) {
                "남성" -> 1
                "여성" -> 2
                "남자" -> 1
                "여자" -> 2
                else -> 1
            }
        )
    }

    // gender 파라미터가 변경될 때 selectedOption 업데이트
    LaunchedEffect(gender) {
        selectedOption = when (gender) {
            "남성" -> 1
            "여성" -> 2
            "남자" -> 1
            "여자" -> 2
            else -> 1
        }
    }

    // 성별 상태 동기화 (UI용 "남자"/"여자"로 설정)
    LaunchedEffect(selectedOption) {
        genderState = if (selectedOption == 1) "남자" else "여자"
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp), // 상단 여백 제거
        bottomBar = {
            BottomButton(
                text = "다음",
                onClick = {
                    onNextClick(nameState, yearOfBirthState, genderState, introductionState)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileTopBar(
                title = "프로필 수정",
                moreIconButton = false,
                onBackClick = onBackClick
            )

            // 상단 단계 표시 (1단계, 2단계)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileEditCaseNumber(
                    isCurrent = true,
                    number = 1,
                    description = "프로필 설정"
                )
                Spacer(modifier = Modifier.width(36.dp))
                ProfileEditCaseNumber(
                    isCurrent = false,
                    number = 2,
                    description = "키워드 선택"
                )
            }

            // 프로필 이미지
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                ProfileImage()
            }

            ProfileEditNameTextField(
                hint = "이름을 입력해주세요",
                value = nameState,
                onValueChange = { nameState = it } // 상태 업데이트
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                YearPickerDropdown(
                    modifier = Modifier.weight(1f),
                    selectedYear = yearOfBirthState,
                    onYearSelected = { yearOfBirthState = it },
                    enabled = yearOfBirth.isEmpty() // 기존 데이터가 없을 때만 활성화
                )
                Spacer(Modifier.width(10.dp))
                ToggleButtonGroup(
                    modifier = Modifier.weight(1f),
                    option1 = "남자",
                    option2 = "여자",
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it },
                    enabled = gender.isEmpty() // 기존 값이 있으면 비활성화
                )
            }

            // 안내 메시지
            if (gender.isEmpty() && yearOfBirth.isEmpty()){
                InfoMessage(
                    message = "나이와 성별은 한 번 선택 후 변경이 불가능합니다.\n신중하게 선택해 주세요.",
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            ProfileEditNameTextField(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier.height(90.dp),
                hint = "자기소개를 입력해주세요.",
                value = introductionState,
                onValueChange = { introductionState = it } // 상태 업데이트
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenPreview() {
    EditProfileScreen()
}