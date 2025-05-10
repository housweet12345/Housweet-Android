package com.housweet.presentation.ui.startPage.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.startPage.accessRoomPage.AccessRoomScreen
import com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen.CreateRoomScreen
import com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen.SearchRoomScreen
import com.housweet.presentation.ui.startPage.loginPage.PermissionGuideScreen
import com.housweet.presentation.ui.startPage.loginPage.WelcomeScreen
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginScreen

@Composable
fun StartPageNavigation(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.LoginRoute.LoginScreen
    ) {
        val navigationManager = StartPageNavigationManager(navController)
        composable<Route.LoginRoute.LoginScreen> {
            LoginScreen(modifier = modifier) {
                navigationManager.navigateOneWay(
                    Route.LoginRoute.LoginScreen,
                    Route.LoginRoute.WelComeScreen
                )
            }

        }

        composable<Route.LoginRoute.WelComeScreen> {
            WelcomeScreen(modifier = modifier) {
                navigationManager.navigateOneWay(
                    Route.LoginRoute.WelComeScreen,
                    Route.LoginRoute.PermissionGuideScreen
                )
            }
        }

        composable<Route.LoginRoute.PermissionGuideScreen> {
            PermissionGuideScreen(modifier = modifier) {
                navigationManager.navigateOneWay(
                    Route.LoginRoute.PermissionGuideScreen,
                    Route.AccessRoomRoute.AccessRoomScreen
                )
            }
        }

        composable<Route.AccessRoomRoute.AccessRoomScreen> {
            AccessRoomScreen(
                modifier = modifier,
                onChatScreen = {

                },
                onAlarmScreen = {

                },
                onMyPageScreen = {

                },
                onFindRoomMateScreen = {

                },
                onCreateRoomScreen = {
                    navigationManager.navigateTo(
                        Route.AccessRoomRoute.CreateRoomScreen
                    )
                },
                onSearchRoomScreen = {
                    navigationManager.navigateTo(
                        Route.AccessRoomRoute.SearchRoomScreen
                    )
                }
            )
        }

        composable<Route.AccessRoomRoute.CreateRoomScreen> {
            CreateRoomScreen(
                modifier = modifier
            )
        }

        composable<Route.AccessRoomRoute.SearchRoomScreen> {
            SearchRoomScreen(
                modifier = modifier
            )
        }
    }
}