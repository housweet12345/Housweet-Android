package com.housweet.presentation.ui.startPage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.startPage.loginPage.PermissionGuideScreen
import com.housweet.presentation.ui.startPage.loginPage.WelcomeScreen
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginScreen

@Composable
fun StartPageNavigation(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.LoginRoute.LoginScreen
    ) {
        composable<Route.LoginRoute.LoginScreen> {
            LoginScreen(modifier = modifier, navController = navController)
        }

        composable<Route.LoginRoute.WelComeScreen> {
            WelcomeScreen(modifier = modifier, navController = navController)
        }

        composable<Route.LoginRoute.PermissionGuideScreen> {
            PermissionGuideScreen(modifier = modifier, navController = navController)
        }
    }
}