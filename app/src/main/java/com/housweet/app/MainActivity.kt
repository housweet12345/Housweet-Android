package com.housweet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.chat.ChatScreen
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen1
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen2

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ChatListScreen()
//        }
//    }
//}

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
            }
        }
    }
}