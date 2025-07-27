package com.housweet.presentation.ui.startPage

import ChatScreen
import android.content.Intent
import android.util.Base64
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.communityPage.CommunityActivity
import com.housweet.presentation.ui.mypage.AppNotificationSettingsScreen
import com.housweet.presentation.ui.mypage.BookmarkScreen
import com.housweet.presentation.ui.mypage.HelpScreen
import com.housweet.presentation.ui.mypage.MyHouseDetailScreen
import com.housweet.presentation.ui.mypage.MyHouseEditScreen
import com.housweet.presentation.ui.mypage.MyPageScreen
import com.housweet.presentation.ui.mypage.MyPostedRoomScreen
import com.housweet.presentation.ui.mypage.NoticeDetailScreen
import com.housweet.presentation.ui.mypage.NoticeScreen
import com.housweet.presentation.ui.mypage.TermsConditionsPolicies
import com.housweet.presentation.ui.mypage.sampleBookmarks
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
                    navigationManager.navigateTo(
                        Route.ChatRoute.ChatList
                    )
                },
                onAlarmScreen = {

                },
                onMyPageScreen = {
                    navigationManager.navigateTo(
                        Route.MyPageRoute.MyPage
                    )
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

        composable<Route.ChatRoute.ChatList> {
            ChatListScreen(navController = navController)
        }

        composable("chat_detail/{chatName}") { backStackEntry ->
            val encodedName = backStackEntry.arguments?.getString("chatName") ?: "Unknown"
            val chatName = try {
                String(Base64.decode(encodedName, Base64.URL_SAFE or Base64.NO_WRAP))
            } catch (e: Exception) {
                "알 수 없음"
            }
            ChatScreen(chatName = chatName, navController = navController)
        }

        composable<Route.MyPageRoute.MyPage> {
            MyPageScreen(navController = navController)
        }

        composable("bookmark") {
            BookmarkScreen(
                bookmarks = sampleBookmarks, // 실제 데이터로 교체 가능
                onItemClick = { /* TODO: 상세 페이지로 이동 등 처리 */ },
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }
        composable("myhousedetail") {
            MyHouseDetailScreen(
                navController,
                isHost = true,
                onBackClick = { navController.popBackStack() },
                onMenuClick = {},
                inviteCode = "000112320",
            )
        }
        composable("notice") {
            NoticeScreen(
                onBackClick = { navController.popBackStack() },
                navController
            )
        }
        composable("edit_my_house") {
            MyHouseEditScreen(
                navController,
                houseName = "곰돌이방",
                startDate = "2025.01.05",
                inviteCode = "000112320",
                onDelete = { /* 삭제 로직 */ },
                onComplete = { navController.popBackStack() },
                onCodeRefresh = {},
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("posted_my_room") {
            MyPostedRoomScreen(
                navController
            )
        }
        composable("app_setting") {
            AppNotificationSettingsScreen(navController)
        }
        composable("help") {
            HelpScreen(navController)
        }
        composable("terms_conditions_policies") {
            TermsConditionsPolicies(navController)
        }
        composable(
            "noticeDetail/{date}/{title}/{content}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""

            NoticeDetailScreen(
                date = date,
                title = title,
                content = content,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}