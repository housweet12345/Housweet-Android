package com.housweet.presentation.ui.profile.route

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val reportResult = viewModel.reportResult.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val id = if (userId == "me") null else userId
        viewModel.loadProfile(id)
    }

    LaunchedEffect(key1 = reportResult.value) {
        reportResult.value?.let { result ->
            when (result) {
                is ProfileInfoViewModel.ReportResult.Success -> {
                    Toast.makeText(context, "신고가 접수되었습니다. Report ID: ${result.data.id}", Toast.LENGTH_LONG).show()
                }
                is ProfileInfoViewModel.ReportResult.Error -> {
                    Toast.makeText(context, "신고 실패: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
                is ProfileInfoViewModel.ReportResult.Loading -> {
                    // Optionally show a loading indicator
                }
            }
        }
    }

    when (state.value) {
        is ProfileInfoState.Success -> {
            val profile = (state.value as ProfileInfoState.Success).profileInfo
            ProfileScreen(
                profileInfo = profile,
                onBackClick = onBackClick,
                navigateEditProfile = navigateEditProfile,
                navigateChatting = navigateChatting,
                onReportClick = { type, id -> viewModel.report(type, id) }
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