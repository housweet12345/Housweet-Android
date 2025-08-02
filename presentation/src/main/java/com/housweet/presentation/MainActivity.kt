package com.housweet.presentation

import ChatScreen
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import com.housweet.presentation.ui.home.route.HomeRoute
import com.housweet.presentation.ui.navigation.BottomNavItem
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
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
import androidx.navigation.navArgument
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.profile.route.EditProfileKeyWordRoute
import com.housweet.presentation.ui.profile.route.EditProfileRoute
import com.housweet.presentation.ui.profile.route.MyProfileRoute
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen1
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen2
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen3
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen4
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            // ✅ 전역으로 ViewModel 생성
            val houseRegisterViewModel: HouseRegisterViewModel = hiltViewModel()
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

                    // ✅ ViewModel에 Bitmap 전달
                    houseRegisterViewModel.updateImageBitmap(bitmap)
                    Log.d("ImagePicker", "선택된 이미지: $bitmap")
                    Log.d("HouseRegister", "비트맵 감지됨, ViewModel에 업데이트")
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

                    composable(
                        route = "house_register_1/{mode}",
                        arguments = listOf(navArgument("mode") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val modeStr =
                            backStackEntry.arguments?.getString("mode") ?: RegisterModel.CREATE.name
                        val mode = RegisterModel.valueOf(modeStr)

                        HouseRegisterScreen1(
                            mode = mode,
                            onNextClick = { navController.navigate("house_register_2/${mode.name}") },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "house_register_2/{mode}",
                        arguments = listOf(navArgument("mode") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val modeStr =
                            backStackEntry.arguments?.getString("mode") ?: RegisterModel.CREATE.name
                        val mode = RegisterModel.valueOf(modeStr)
                        HouseRegisterScreen2(
                            mode = mode,
                            onNextClick = { navController.navigate("house_register_3/${mode.name}") },
                            onBackClick = { navController.navigate("house_register_1/${mode.name}") }
                        )
                    }

                    composable(
                        route = "house_register_3/{mode}",
                        arguments = listOf(navArgument("mode") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val modeStr =
                            backStackEntry.arguments?.getString("mode") ?: RegisterModel.CREATE.name
                        val mode = RegisterModel.valueOf(modeStr)

                        HouseRegisterScreen3(
                            mode = mode,
                            onNextClick = { navController.navigate("house_register_4/${mode.name}") },
                            onBackClick = { navController.navigate("house_register_2/${mode.name}") },
                            onImagePickClick = { launcher.launch("image/*") },
                            selectedImageBitmap = selectedImageBitmap.value,
                            viewModel = houseRegisterViewModel
                        )
                    }

                    composable(
                        route = "house_register_4/{mode}",
                        arguments = listOf(navArgument("mode") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val modeStr =
                            backStackEntry.arguments?.getString("mode") ?: RegisterModel.CREATE.name
                        val mode = RegisterModel.valueOf(modeStr)

                        HouseRegisterScreen4(
                            mode = mode,
                            onBackClick = { navController.navigate("house_register_3/${mode.name}") },
                            onCompleteClick = {
                                if (mode == RegisterModel.CREATE) {
                                    Log.d("HouseRegisterScreen4", "✅ 등록 성공")
                                    navController.popBackStack()
                                } else {
                                    //글 수정 API 호출
                                }
                            },
                            viewModel = houseRegisterViewModel
                        )
                    }

                    composable("chat_list") {
                        ChatListScreen(
                            navController
                        )
                    }

                    composable(
                        route = "chat_detail/{chatName}",
                        arguments = listOf(navArgument("chatName") { defaultValue = "Unknown" })
                    )
                    { backStackEntry ->
                        val encodedName =
                            backStackEntry.arguments?.getString("chatName") ?: "Unknown"
                        val chatName = String(
                            Base64.decode(
                                encodedName,
                                Base64.URL_SAFE or Base64.NO_WRAP
                            )
                        ) // ✅ 여기서 디코딩
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
                    }
                }
            }
        }
    }
}