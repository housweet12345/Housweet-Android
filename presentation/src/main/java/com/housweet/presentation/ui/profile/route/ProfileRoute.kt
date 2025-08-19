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
    navigateChatting: (userId: Int, nickName: String) -> Unit = { _, _ -> }
) {
    val state = viewModel.profileState.collectAsStateWithLifecycle()
    val reportResult = viewModel.reportResult.collectAsStateWithLifecycle()
    val blockResult = viewModel.blockResult.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadProfile(userId)
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

    LaunchedEffect(key1 = blockResult.value) {
        blockResult.value?.let { result ->
            when (result) {
                is ProfileInfoViewModel.BlockResult.Success -> {
                    Toast.makeText(context, "사용자가 차단되었습니다.", Toast.LENGTH_SHORT).show()
                    onBackClick() // 차단 후 이전 화면으로 이동
                }
                is ProfileInfoViewModel.BlockResult.Error -> {
                    Toast.makeText(context, "차단 실패: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
                is ProfileInfoViewModel.BlockResult.Loading -> {
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
                onReportClick = { type, id -> viewModel.report(type, id) },
                onBlockClick = { userId -> viewModel.blockUser(userId) }
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