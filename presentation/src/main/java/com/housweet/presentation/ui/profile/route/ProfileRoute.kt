package com.housweet.presentation.ui.profile.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.profile.screen.ProfileScreen
import com.housweet.presentation.ui.profile.state.ProfileInfoState
import com.housweet.presentation.viewmodel.profile.ProfileInfoViewModel

@Composable
fun ProfileRoute(
    userId: String?,
    viewModel: ProfileInfoViewModel = hiltViewModel(),
    navigateEditProfile: () -> Unit = {},
    onBackClick: () -> Unit = {},
    navigateChatting: () -> Unit = {}
) {
    val state = viewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        val id = if (userId == "me") null else userId
        viewModel.loadProfile(id)
    }

    when (state.value) {
        is ProfileInfoState.Success -> {
            val profile = (state.value as ProfileInfoState.Success).profileInfo
            ProfileScreen(
                profileInfo = profile,
                onBackClick = onBackClick,
                navigateEditProfile = navigateEditProfile,
                navigateChatting = navigateChatting,
            )
        }
        is ProfileInfoState.Loading -> {
            // 로딩 화면
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        else -> {

        }
    }
}