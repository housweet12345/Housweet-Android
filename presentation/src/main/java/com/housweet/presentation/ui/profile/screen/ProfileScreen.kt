package com.housweet.presentation.ui.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.housweet.presentation.ui.common.CustomAlertDialog
import com.housweet.presentation.ui.common.CustomMenu
import com.housweet.presentation.ui.common.MenuItem
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
    navigateChatting: () -> Unit = {},
    onReportClick: (type: String, id: Int) -> Unit = { _, _ -> }
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }

    val menuItems = listOf(
        MenuItem(text = "차단하기") { showBlockDialog = true },
        MenuItem(text = "신고하기") {
            onReportClick( "user", profileInfo.userId)
            menuExpanded = false
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 20.dp)
        ) {
            // 상단 앱바
            ProfileTopBar(
                title = if (profileInfo.myProfile) "내 프로필" else "프로필",
                moreIconButton = !profileInfo.myProfile,
                onBackClick = onBackClick,
                onMoreClick = { menuExpanded = true }
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

        if (menuExpanded) {
            CustomMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                menuItems = menuItems,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp, end = 20.dp)
            )
        }

        if (showBlockDialog) {
            CustomAlertDialog(
                onDismissRequest = { showBlockDialog = false },
                onConfirmation = {
                    showBlockDialog = false
                    // TODO: 차단 로직 구현
                },
                dialogText = "차단하시겠습니까?"
            )
        }
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
