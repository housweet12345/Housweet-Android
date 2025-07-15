package com.housweet.presentation.ui.startPage

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.communityPage.CommunityActivity
import com.housweet.presentation.ui.navigation.NavigationManager
import com.housweet.presentation.ui.navigation.Route
import com.housweet.presentation.ui.startPage.accessRoomPage.AccessRoomScreen
import com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen.CreateRoomScreen
import com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen.SearchRoomScreen
import com.housweet.presentation.ui.startPage.loginPage.PermissionGuideScreen
import com.housweet.presentation.ui.startPage.loginPage.WelcomeScreen
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginScreen
import com.housweet.presentation.ui.startPage.loginPage.termsOfServicePage.TermsOfServiceScreen

@Composable
fun StartPageNavigation(isAutoLogin: Boolean, modifier: Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = if (isAutoLogin) Route.StartPageRoute.AccessRoomRoute.AccessRoom else Route.StartPageRoute.LoginRoute.Login
    ) {
        val navigationManager = NavigationManager(navController)
        composable<Route.StartPageRoute.LoginRoute.Login> {
            LoginScreen(modifier = modifier) {
                if (it == "sign_in") {
                    navigationManager.navigateOneWay(
                        Route.StartPageRoute.LoginRoute.Login,
                        Route.StartPageRoute.AccessRoomRoute.AccessRoom
                    )
                } else if (it == "sign_up") {
                    navigationManager.navigateOneWay(
                        Route.StartPageRoute.LoginRoute.Login,
                        Route.StartPageRoute.LoginRoute.WelCome
                    )
                }
            }
        }

        composable<Route.StartPageRoute.LoginRoute.WelCome> {
            WelcomeScreen(modifier = modifier) {
                navigationManager.navigateOneWay(
                    Route.StartPageRoute.LoginRoute.WelCome,
                    Route.StartPageRoute.LoginRoute.PermissionGuide
                )
            }
        }

        composable<Route.StartPageRoute.LoginRoute.PermissionGuide> {
            PermissionGuideScreen(modifier = modifier) {
                navigationManager.navigateOneWay(
                    Route.StartPageRoute.LoginRoute.PermissionGuide,
                    Route.StartPageRoute.LoginRoute.TermsOfService
                )
            }
        }

        composable<Route.StartPageRoute.LoginRoute.TermsOfService> {
            TermsOfServiceScreen(modifier = modifier) {
                navigationManager.navigateOneWay(
                    Route.StartPageRoute.LoginRoute.TermsOfService,
                    Route.StartPageRoute.AccessRoomRoute.AccessRoom
                )
            }
        }

        composable<Route.StartPageRoute.AccessRoomRoute.AccessRoom> {
            AccessRoomScreen(
                modifier = modifier,
                onChatScreen = {

                },
                onAlarmScreen = {

                },
                onMyPageScreen = {

                },
                onFindRoomMateScreen = {
                    val communityActivityIntent = Intent(context, CommunityActivity::class.java)
                    context.startActivity(communityActivityIntent)
                },
                onCreateRoomScreen = {
                    navigationManager.navigateTo(
                        Route.StartPageRoute.AccessRoomRoute.CreateRoom
                    )
                },
                onSearchRoomScreen = {
                    navigationManager.navigateTo(
                        Route.StartPageRoute.AccessRoomRoute.SearchRoom
                    )
                }
            )
        }

        composable<Route.StartPageRoute.AccessRoomRoute.CreateRoom> {
            CreateRoomScreen(
                modifier = modifier
            )
        }

        composable<Route.StartPageRoute.AccessRoomRoute.SearchRoom> {
            SearchRoomScreen(
                modifier = modifier
            )
        }
    }
}