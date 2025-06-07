package com.housweet.presentation.ui.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.housweet.presentation.ui.profile.component.EditProfileButton
import com.housweet.presentation.ui.profile.component.ProfileInfoSection
import com.housweet.presentation.ui.profile.component.ProfileTopBar
import com.housweet.presentation.ui.profile.component.TagSection
import com.housweet.presentation.ui.profile.state.ProfileInfo

@Composable
fun ProfileScreen(
    profileInfo: ProfileInfo,
    onBackClick: () -> Unit = {},
    navigateEditProfile: () -> Unit = {},
    navigateChatting: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        // 상단 앱바
        ProfileTopBar(
            title = if (profileInfo.myProfile) "내 프로필" else "프로필",
            moreIconButton = !profileInfo.myProfile,
            onBackClick = onBackClick
        )
        // 프로필 정보
        ProfileInfoSection(
            nickname = profileInfo.nickname,
            age = profileInfo.age,
            gender = profileInfo.gender,
            introduction = profileInfo.introduce,
        )
        Spacer(modifier = Modifier.height(16.dp))
        // 태그 섹션
        TagSection(mbti = profileInfo.mbti, tags = profileInfo.tags)
        Spacer(modifier = Modifier.height(24.dp))
        // 하단 버튼
        EditProfileButton(
            isMyProfile = profileInfo.myProfile,
            editButtonOnClick = navigateEditProfile,
            chatButtonOnClick = navigateChatting
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        profileInfo = ProfileInfo().copy(
            nickname = "김아무개",
            gender = "남자",
            age = "20대",
            mbti = "ISTP",
            introduce = "안녕하세요, 잘부탁드립니다",
            tags = listOf("ISTP","개발자", "안드로이드", "코틀린")
        )
    )
}
