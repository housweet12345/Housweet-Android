package com.housweet.presentation.ui.home.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.home.HomeScreen
import com.housweet.presentation.ui.home.state.HomeState
import com.housweet.presentation.viewmodel.home.HomeViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    navigateToChat: () -> Unit = {},
    navigateToNotification: () -> Unit = {},
//    navigateToProfile: () -> Unit = {},
    navigateToMyPage: () -> Unit = {},
    navigateToNoticeDetail: (Int) -> Unit = {},
    navigateToTodoDetail: () -> Unit = {},
    navigateToUserList: () -> Unit = {},
) {
    val state = viewModel.homeState.collectAsStateWithLifecycle()

    when (state.value) {
        is HomeState.Success -> {
            val homeInfo = (state.value as HomeState.Success).homeInfo
            HomeScreen(
                homeInfo = homeInfo,
                onChatClick = navigateToChat,
                onNotificationClick = navigateToNotification,
//                onProfileClick = navigateToProfile,
                onMyPageClick = navigateToMyPage,
                onNoticeClick = navigateToNoticeDetail,
                onTodoClick = navigateToTodoDetail,
                onMoodSectionClick = navigateToUserList,
                onTodoToggle = viewModel::toggleTodoComplete,
                onMoodSelect = viewModel::updateMood,
                navController = navController
            )
        }
        is HomeState.Loading -> {
            LoadingScreen()
        }
        is HomeState.Error -> {
            // TODO: 에러 화면 구현
            Box(modifier = Modifier.fillMaxSize()) {
                // 에러 화면 컴포넌트
            }
        }
    }
}