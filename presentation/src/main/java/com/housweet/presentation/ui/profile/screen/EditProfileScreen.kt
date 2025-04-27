package com.housweet.presentation.ui.profile.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentaion.R
import com.housweet.presentation.ui.profile.component.BottomButton
import com.housweet.presentation.ui.profile.component.InfoMessage
import com.housweet.presentation.ui.profile.component.ProfileEditCaseNumber
import com.housweet.presentation.ui.profile.component.ProfileEditNameTextField
import com.housweet.presentation.ui.profile.component.ProfileImage
import com.housweet.presentation.ui.profile.component.ToggleButtonGroup
import com.housweet.presentation.ui.theme.ColorGroup

@Composable
fun ProfileSetupScreen(
    onNextClick: () -> Unit = {}
) {
    var selectedOption by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomButton(
                text = "다음",
                onClick = onNextClick
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
                value = "",
                onValueChange = {}
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileEditNameTextField(
                    modifier = Modifier.weight(1f),
                    hint = "태어난 년도를 선택해주세요.",
                    value = "",
                    onValueChange = {}
                )
                Spacer(Modifier.width(10.dp))
                ToggleButtonGroup(
                    modifier = Modifier.weight(1f),
                    option1 = "남자",
                    option2 = "여자",
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it }
                )
            }

            // 안내 메시지
            InfoMessage(
                message = "나이와 성별은 한 번 선택 후 변경이 불가능합니다.\n신중하게 선택해 주세요.",
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(Modifier.height(10.dp))

            ProfileEditNameTextField(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier.height(90.dp),
                hint = "자기소개를 입력해주세요.",
                value = "",
                onValueChange = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenPreview() {
    ProfileSetupScreen()
}