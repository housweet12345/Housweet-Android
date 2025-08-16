package com.housweet.presentation.ui.profile.route

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.profile.screen.EditProfileScreen
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.viewmodel.profile.EditProfileViewModel

@Composable
fun EditProfileRoute(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSuccessNavigateBack: () -> Unit = {},
    navigateEditKeyword: () -> Unit = {},
) {
    val state = viewModel.profileState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.value) {
        when (state.value) {
            is ProfileInfoState.Error -> {
                val errorMessage = (state.value as ProfileInfoState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                onBackClick()
            }
            is ProfileInfoState.EditSuccess -> {
                Toast.makeText(context, "프로필이 성공적으로 수정되었습니다", Toast.LENGTH_SHORT).show()
                onSuccessNavigateBack()
            }
            else -> {}
        }
    }

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