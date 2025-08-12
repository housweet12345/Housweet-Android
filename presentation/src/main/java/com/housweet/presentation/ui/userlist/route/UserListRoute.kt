package com.housweet.presentation.ui.userlist.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.housweet.presentation.ui.userlist.screen.UserListScreen
import com.housweet.presentation.ui.userlist.state.UserListState
import com.housweet.presentation.viewmodel.userlist.UserListViewModel

@Composable
fun UserListRoute(
    viewModel: UserListViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    navigateToProfile: (String) -> Unit = {}
) {
    val state = viewModel.userListState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }
    
    when (state.value) {
        is UserListState.Success -> {
            val users = (state.value as UserListState.Success).userItems
            UserListScreen(
                userItems = users,
                onBackClick = onBackClick,
                navigateToProfile = navigateToProfile
            )
        }
        is UserListState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF6C5CE7)
                )
            }
        }
        is UserListState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = (state.value as UserListState.Error).message,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
        }
    }
}