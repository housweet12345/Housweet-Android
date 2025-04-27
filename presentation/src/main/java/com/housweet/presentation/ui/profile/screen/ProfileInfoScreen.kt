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
import com.housweet.presentation.ui.profile.component.ProfileInfo
import com.housweet.presentation.ui.profile.component.ProfileTopBar
import com.housweet.presentation.ui.profile.component.TagSection

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        // 상단 앱바
        ProfileTopBar()
        // 프로필 정보
        ProfileInfo()
        Spacer(modifier = Modifier.height(16.dp))
        // 태그 섹션
        TagSection(listOf("태그1", "태그2", "태그3"))
        Spacer(modifier = Modifier.height(24.dp))
        // 하단 버튼
        EditProfileButton(false)
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
