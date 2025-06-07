package com.housweet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.chat.ChatScreen
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.profile.route.EditProfileKeyWordRoute
import com.housweet.presentation.ui.profile.route.EditProfileRoute
import com.housweet.presentation.ui.profile.route.MyProfileRoute
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen1
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen2
import com.housweet.presentation.viewmodel.profile.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "house_register_1"
            ) {
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
                        onBackClick = {navController.navigate("house_register_1")}
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
                        navigateChatting = {  }
                    )
                }
                composable("profile/edit") {
                    EditProfileRoute(
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
                        navigateMyProfile = { navController.navigate("profile/me") }
                    )
                }
            }
        }
    }
}