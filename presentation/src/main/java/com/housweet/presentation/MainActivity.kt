package com.housweet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.chat.ChatScreen
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.home.route.HomeRoute
import com.housweet.presentation.ui.navigation.BottomNavItem
import com.housweet.presentation.ui.profile.route.EditProfileKeyWordRoute
import com.housweet.presentation.ui.profile.route.EditProfileRoute
import com.housweet.presentation.ui.profile.route.MyProfileRoute
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen1
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen2
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen3
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen4
import com.housweet.presentation.viewmodel.profile.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold { padingValues ->
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Home.route,
                    modifier = Modifier.padding(top = padingValues.calculateTopPadding())
                ) {
                    composable(BottomNavItem.Home.route) {
                        HomeRoute(
                            navigateToChat = { navController.navigate("chat_list") },
                            navigateToNotification = { /* TODO: 알림 화면 */ },
                            navigateToProfile = { navController.navigate("profile/me") },
                            navigateToNoticeDetail = { noticeId -> /* TODO: 공지사항 상세 */ },
                            navigateToTodoDetail = { /* TODO: 할일 상세 */ },
                            navController = navController
                        )
                    }
                    composable(BottomNavItem.Calendar.route) {
                        //캘린더 화면
                        Text("캘린더")
                    }
                    composable(BottomNavItem.FinanceLedger.route) {
                        //가계부 화면
                        Text("가계부")
                    }
                    composable(BottomNavItem.Notice.route) {
                        //공지사항 화면
                        Text("공지사항")
                    }
                    composable("house_register_1") {
                        HouseRegisterScreen1( onNextClick = {
                            navController.navigate("house_register_2")
                        },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("house_register_2") {
                        HouseRegisterScreen2(
                            onNextClick = {
                                navController.navigate("house_register_3")
                            },
                            onBackClick = {navController.navigate("house_register_1")},
                            onRegionSelectClick = {
                                navController.navigate("region_select")
                            }
                        )
                    }
                    composable("house_register_3") {
                        HouseRegisterScreen3(
                            onNextClick = {
                                navController.navigate("house_register_4")
                            },
                            onBackClick = { navController.navigate("house_register_2") },
                            onImagePickClick = {},
                            selectedImageBitmap = null
                        )
                    }
                    composable("house_register_4") {
                        HouseRegisterScreen4(
                            onBackClick = {
                                navController.navigate("house_register_3")
                            },
                            onCompleteClick = {}
                        )
                    }
                    composable("chat_list") {
                        ChatListScreen(navController)
                    }
                    composable("chat_detail/{chatName}") { backStackEntry ->
                        val chatName = backStackEntry.arguments?.getString("chatName") ?: "Unknown"
                        ChatScreen(chatName, navController)
                    }
                    composable("profile/me") {
                        MyProfileRoute(
                            navigateEditProfile = { navController.navigate("profile/edit") },
                            onBackClick = { navController.popBackStack() },
                            navigateChatting = {  }
                        )
                    }
                    composable("profile/edit") {
                        EditProfileRoute(
                            onBackClick = { navController.popBackStack() },
                            navigateEditKeyword = { navController.navigate("profile/edit_keyword") }
                        )
                    }

                    composable("profile/edit_keyword") { navBackStackEntry ->
                        val parentEntry = remember(navBackStackEntry) {
                            navController.getBackStackEntry("profile/edit")
                        }
                        val viewModel: EditProfileViewModel = hiltViewModel(parentEntry)

                        EditProfileKeyWordRoute(
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() },
                            navigateMyProfile = { navController.navigate("profile/me") }
                        )
                    }
                }
            }
        }
    }
}