package com.housweet.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R
import com.housweet.presentation.ui.chat.ChatScreen
import com.housweet.presentation.ui.chatlist.ChatListScreen
import androidx.compose.animation.*
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen1
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen2


//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}

@OptIn(ExperimentalAnimationApi::class)
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

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "chat_list"
        ) {
            composable("chat_list") {
                ChatListScreen(navController)
            }
            composable("chat/{chatName}") { backStackEntry ->
                val chatName = backStackEntry.arguments?.getString("chatName") ?: "Unknown"
                ChatScreen(chatName, navController)
            }
        }
    }
}