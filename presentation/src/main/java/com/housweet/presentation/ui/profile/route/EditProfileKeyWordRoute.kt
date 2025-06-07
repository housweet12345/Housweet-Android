package com.housweet.presentation.ui.profile.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.profile.screen.EditProfileSelectKeyWordScreen
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.viewmodel.profile.EditProfileViewModel

@Composable
fun EditProfileKeyWordRoute(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    navigateMyProfile: () -> Unit = {},
) {
    val state = viewModel.profileState.collectAsStateWithLifecycle()

    when(val currentState = state.value) {
        ProfileInfoState.EditSuccess -> {
            // 수정 성공 시 다음 화면으로 이동
            LaunchedEffect(Unit) {
                navigateMyProfile()
            }
        }
        is ProfileInfoState.Success -> {
            val profile = currentState.profileInfo
            EditProfileSelectKeyWordScreen(
                currentProfile = profile, // 현재 프로필 정보 전달
                onBackClick = onBackClick,
                onNextClick = viewModel::updateProfile,
            )
        }
        ProfileInfoState.Loading -> {
            // 로딩
        }
        is ProfileInfoState.Error -> {
            // 에러
        }
    }
}