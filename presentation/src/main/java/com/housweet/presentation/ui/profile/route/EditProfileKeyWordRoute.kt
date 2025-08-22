package com.housweet.presentation.ui.profile.route

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.profile.screen.EditProfileSelectKeyWordScreen
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.viewmodel.profile.EditProfileViewModel

@Composable
fun EditProfileKeyWordRoute(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSuccessNavigateBack: () -> Unit = {},
    showSkipButton: Boolean = false,
    onSkipClick: () -> Unit = {},
) {
    val state = viewModel.profileState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.value) {
        when (val currentState = state.value) {
            is ProfileInfoState.EditSuccess -> {
                onSuccessNavigateBack()
            }
            is ProfileInfoState.Error -> {
                Toast.makeText(context, currentState.message, Toast.LENGTH_SHORT).show()
                onSuccessNavigateBack()
            }
            else -> {}
        }
    }

    when(val currentState = state.value) {
        is ProfileInfoState.Success -> {
            val profile = currentState.profileInfo
            EditProfileSelectKeyWordScreen(
                currentProfile = profile,
                showSkipButton = showSkipButton,
                onBackClick = onBackClick,
                onNextClick = viewModel::updateProfile,
                onSkipClick = viewModel::updateProfile
            )
        }
        ProfileInfoState.Loading -> {
            // 로딩
        }
        else -> {}
    }
}