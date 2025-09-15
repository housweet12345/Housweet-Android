package com.housweet.presentation.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

class NavigationManager(
    private val navController: NavController,
    private val onNavigate: () -> Unit = {}
) {
    fun navigateTo(route: Route, configure: (NavOptionsBuilder.() -> Unit)? = null) {
        onNavigate()

        navController.navigate(route) {
            configure?.invoke(this)
        }
    }

    fun navigateTo(route: String, configure: (NavOptionsBuilder.() -> Unit)? = null) {
        onNavigate()

        navController.navigate(route) {
            configure?.invoke(this)
        }
    }

    fun navigateOneWay(popUpToRoute: Route, to: Route) {
        onNavigate()

        navController.navigate(to) {
            popUpTo(popUpToRoute) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun navigateOneWay(popUpToRoute: Route, to: String) {
        onNavigate()

        navController.navigate(to) {
            popUpTo(popUpToRoute) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun navigateOneWay(popUpToRoute: String, to: Route) {
        onNavigate()

        navController.navigate(to) {
            popUpTo(popUpToRoute) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun navigateOneWay(popUpToRoute: String, to: String) {
        onNavigate()

        navController.navigate(to) {
            popUpTo(popUpToRoute) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun popBackStack() {
        onNavigate()

        navController.popBackStack()
    }
}