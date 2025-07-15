package com.housweet.presentation

import NotificationScreen
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.chat.ChatScreen
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.home.route.HomeRoute
import com.housweet.presentation.ui.navigation.BottomNavItem
import com.housweet.presentation.ui.mypage.MyHouseDetailScreen
import com.housweet.presentation.ui.mypage.MyPageScreen
import com.housweet.presentation.ui.mypage.BookmarkScreen
import com.housweet.presentation.ui.mypage.HelpScreen
import com.housweet.presentation.ui.mypage.MyHouseEditScreen
import com.housweet.presentation.ui.mypage.MyPostedRoomScreen
import com.housweet.presentation.ui.mypage.NoticeScreen
import com.housweet.presentation.ui.mypage.sampleBookmarks
import com.housweet.presentation.ui.profile.route.EditProfileKeyWordRoute
import com.housweet.presentation.ui.profile.route.EditProfileRoute
import com.housweet.presentation.ui.profile.route.MyProfileRoute
import com.housweet.presentation.ui.registerhouse.*
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginScreen
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginViewModel
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
            val context = LocalContext.current
            val selectedImageBitmap = remember { mutableStateOf<Bitmap?>(null) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let {
                    val bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    }
                    selectedImageBitmap.value = bitmap
                }
            }
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
                        HouseRegisterScreen1(
                            onNextClick = { navController.navigate("house_register_2") },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable("house_register_2") {
                        HouseRegisterScreen2(
                            onNextClick = { navController.navigate("house_register_3") },
                            onBackClick = { navController.navigate("house_register_1") }
                        )
                    }

                    composable("house_register_3") {
                        HouseRegisterScreen3(
                            onNextClick = { navController.navigate("house_register_4") },
                            onBackClick = { navController.navigate("house_register_2") },
                            onImagePickClick = { launcher.launch("image/*") },
                            selectedImageBitmap = selectedImageBitmap.value
                        )
                    }

                    composable("house_register_4") {
                        HouseRegisterScreen4(
                            onBackClick = { navController.navigate("house_register_3") },
                            onCompleteClick = { /* TODO */ }
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
                            navigateChatting = { }
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

                    composable("notification") {
                        NotificationScreen(
                            navController,
                            onBackClick = {}
                        )
                    }

                    composable("mypage") {
                        MyPageScreen(
                            navController,
                            onBackClick = {}
                        )
                    }
                    composable("bookmark") {
                        BookmarkScreen(
                            bookmarks = sampleBookmarks, // 실제 데이터로 교체 가능
                            onItemClick = { /* TODO: 상세 페이지로 이동 등 처리 */ },
                            onBackClick = { navController.popBackStack() }
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
                            onBackClick = { navController.popBackStack() }
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

                        )
                    }
                }
            }
        }
    }
}