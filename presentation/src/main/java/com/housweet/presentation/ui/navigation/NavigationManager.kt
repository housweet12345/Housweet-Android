package com.housweet.presentation.ui.navigation

import androidx.navigation.NavController

class NavigationManager(private val navController: NavController) {
    fun navigateTo(route: Route) {
        navController.navigate(route)
    }

    fun navigateOneWay(from: Route, to: Route) {
        navController.navigate(to) {
            popUpTo(from) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }
}