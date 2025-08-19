package com.housweet.presentation.navigation

import androidx.navigation.NavController

fun NavController.goToMyPageAfterDelete() {
    runCatching {
        getBackStackEntry("mypage")   //
            .savedStateHandle["MY_HOUSE_REFRESH"] = true
    }
    if (!popBackStack("mypage", false)) {
        navigate("mypage") {
            popUpTo(graph.startDestinationId) { inclusive = false }
            launchSingleTop = true
            restoreState = false
        }
    }
}
