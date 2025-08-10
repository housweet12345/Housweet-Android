package com.housweet.presentation.ui.navigation

import androidx.navigation.NavController

class NavigationManager(private val navController: NavController) {
    fun navigateTo(route: Route) {
        navController.navigate(route)
    }

    fun navigateTo(route: String) {
        navController.navigate(route)
    }

    fun navigateOneWay(popUpToRoute: Route, to: Route) {
        navController.navigate(to) {
            popUpTo(popUpToRoute) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun navigateOneWay(popUpToRoute: Route, to: String) {
        navController.navigate(to) {
            popUpTo(popUpToRoute) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun navigateOneWay(popUpToRoute: Any, to: Any) {
        navController.navigate(to) {
            popUpTo(popUpToRoute) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }
}