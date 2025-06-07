package com.housweet.presentation.ui.profile.route

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.profile.screen.EditProfileScreen
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.viewmodel.profile.EditProfileViewModel

@Composable
fun EditProfileRoute(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    navigateEditKeyword: () -> Unit = {},
) {
    val state = viewModel.profileState.collectAsStateWithLifecycle()

    when(state.value) {
        is ProfileInfoState.Success -> {
            val profile = (state.value as ProfileInfoState.Success).profileInfo
            EditProfileScreen(
                name = profile.nickname,
                yearOfBirth = profile.yearOfBirth,
                gender = profile.gender,
                introduction = profile.introduce,
                onBackClick = onBackClick,
                onNextClick = { nickname, yearOfBirth, gender, introduce ->
                    viewModel.saveBasicProfileData(nickname, yearOfBirth, gender, introduce)
                    navigateEditKeyword()
                }
            )
        }
        else -> {}
    }
}