package com.housweet.presentation.navigation

import androidx.navigation.NavController
import com.housweet.presentation.ui.navigation.BottomNavItem

fun NavController.goToMyPageAfterDeleteV2() {
    navigate("mypage?afterDelete=true") {
        popUpTo(BottomNavItem.Home.route) { inclusive = true }
        launchSingleTop = true
        restoreState = false
    }
}