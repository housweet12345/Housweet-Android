package com.housweet.presentation.ui.startPage.navigation

import androidx.navigation.NavController

class StartPageNavigationManager(private val navController: NavController) {
    fun navigateTo(route: Route) {
        navController.navigate(route)
    }

    fun navigateOneWay(from: Route, to: Route) {
        navController.navigate(to) {
            popUpTo(from) {
                inclusive = true
            }
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}