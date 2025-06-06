package com.housweet.presentation.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.profile.screen.ProfileScreen
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.viewmodel.profile.ProfileInfoViewModel

@Composable
fun MyProfileRoute(
    viewModel: ProfileInfoViewModel = hiltViewModel(),
) {
    val state = viewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        //null일 때 자신의 프로필을 가져옴
        viewModel.loadProfile(null)
    }

    when (state.value) {
        is ProfileInfoState.Success -> {
            val profile = (state.value as ProfileInfoState.Success).profileInfo
            ProfileScreen(profile)
        }
        else -> {
            //TODO()
        }
    }
}