package com.housweet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.chat.ChatScreen
import com.housweet.presentation.ui.chatlist.ChatListScreen

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
                startDestination = "chat_list"
            ) {
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