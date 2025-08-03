package com.housweet.presentation

import ChatScreen
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.housweet.domain.event.AuthEvent
import com.housweet.domain.event.AuthEventBus
import com.housweet.domain.model.Coordinate
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.communityPage.mapScreen.MapScreen
import com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen.DetailPostScreen
import com.housweet.presentation.ui.communityPage.postScreen.postsScreen.PostsScreen
import com.housweet.presentation.ui.communityPage.searchRegionScreen.SearchRegionScreen
import com.housweet.presentation.ui.home.route.HomeRoute
import com.housweet.presentation.ui.navigation.BottomNavItem
import com.housweet.presentation.ui.navigation.CoordinateType
import com.housweet.presentation.ui.navigation.NavigationManager
import com.housweet.presentation.ui.navigation.Route
import com.housweet.presentation.ui.profile.route.EditProfileRoute
import com.housweet.presentation.ui.profile.route.MyProfileRoute
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen1
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen2
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen3
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen4
import com.housweet.presentation.ui.startPage.StartViewModel
import com.housweet.presentation.ui.startPage.accessRoomPage.AccessRoomScreen
import com.housweet.presentation.ui.startPage.accessRoomPage.createRoomScreen.CreateRoomScreen
import com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen.SearchRoomScreen
import com.housweet.presentation.ui.startPage.loginPage.PermissionGuideScreen
import com.housweet.presentation.ui.startPage.loginPage.WelcomeScreen
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.LoginScreen
import com.housweet.presentation.ui.startPage.loginPage.termsOfServicePage.TermsOfServiceScreen
import com.housweet.presentation.ui.startPage.splashPage.SplashScreen
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authEventBus: AuthEventBus
    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFailedRefreshToken = intent.getBooleanExtra("isFailedRefreshToken", false)

        lifecycleScope.launch {
            authEventBus.events.collect { event ->
                when (event) {
                    is AuthEvent.TokenRefreshFailed -> {
                        startViewModel.logout {
                            handleTokenRefreshFailure()
                        }
                    }
                }
            }
        }

        setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            // ✅ 전역으로 ViewModel 생성
            val houseRegisterViewModel: HouseRegisterViewModel = hiltViewModel()
            val context = LocalContext.current
            val selectedImageBitmap = remember { mutableStateOf<Bitmap?>(null) }
            val navigationManager = NavigationManager(navController)

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

            LaunchedEffect(Unit) {
                if (isFailedRefreshToken) {
                    snackBarHostState.showSnackbar(
                        message = "토큰 갱신에 실패했습니다. 다시 로그인해주세요.",
                        actionLabel = "닫기",
                        duration = SnackbarDuration.Short
                    )
                }
            }

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = if (!isFailedRefreshToken) Route.StartPageRoute.Splash else Route.StartPageRoute.LoginRoute.Login,
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
                ) {
                    composable<Route.StartPageRoute.Splash> {
                        SplashScreen { isAutoLogin, isAgreeTermsOfService ->
                            when {
                                !isAutoLogin -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.Splash,
                                        Route.StartPageRoute.LoginRoute.Login
                                    )
                                }
                                isAgreeTermsOfService -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.Splash,
                                        Route.StartPageRoute.AccessRoomRoute.AccessRoom
                                    )
                                }
                                else -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.Splash,
                                        Route.StartPageRoute.LoginRoute.WelCome
                                    )
                                }
                            }
                        }
                    }

                    composable<Route.StartPageRoute.LoginRoute.Login> {
                        LoginScreen {
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
                        WelcomeScreen {
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.LoginRoute.WelCome,
                                Route.StartPageRoute.LoginRoute.PermissionGuide
                            )
                        }
                    }

                    composable<Route.StartPageRoute.LoginRoute.PermissionGuide> {
                        PermissionGuideScreen {
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.LoginRoute.PermissionGuide,
                                Route.StartPageRoute.LoginRoute.TermsOfService
                            )
                        }
                    }

                    composable<Route.StartPageRoute.LoginRoute.TermsOfService> {
                        TermsOfServiceScreen {
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.LoginRoute.TermsOfService,
                                Route.StartPageRoute.AccessRoomRoute.AccessRoom
                            )
                        }
                    }

                    composable<Route.StartPageRoute.AccessRoomRoute.AccessRoom> {
                        AccessRoomScreen(
                            onChatScreen = {
                                navigationManager.navigateTo("chat_list")
                            },
                            onAlarmScreen = {
                                navigationManager.navigateTo("notification")
                            },
                            onMyPageScreen = {
                                navigationManager.navigateTo("mypage")
                            },
                            onFindRoomMateScreen = {
                                navigationManager.navigateTo(
                                    Route.CommunityPageRoute.Map()
                                )
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
                        CreateRoomScreen {
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.AccessRoomRoute.AccessRoom,
                                BottomNavItem.Home.route
                            )
                        }
                    }

                    composable<Route.StartPageRoute.AccessRoomRoute.SearchRoom> {
                        SearchRoomScreen {
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.AccessRoomRoute.AccessRoom,
                                BottomNavItem.Home.route
                            )
                        }
                    }

                    composable<Route.CommunityPageRoute.Map>(
                        typeMap = mapOf(typeOf<Coordinate?>() to CoordinateType)
                    ) {
                        val coordinate = it.toRoute<Route.CommunityPageRoute.Map>().coordinate
                        MapScreen(
                            modifier = Modifier,
                            searchRegion = coordinate,
                            onMarkerClick = { postRegion ->
                                navigationManager.navigateTo(
                                    Route.CommunityPageRoute.PostRoute.Posts(
                                        postRegion
                                    )
                                )
                            },
                            onViewPostBtnClick = { postRegions ->
                                navigationManager.navigateTo(
                                    Route.CommunityPageRoute.PostRoute.Posts(
                                        postRegions
                                    )
                                )
                            },
                            onSearchBtnClick = {
                                navigationManager.navigateTo(Route.CommunityPageRoute.SearchRegion)
                            },
                            onWritePostBtnClick = {
                                navigationManager.navigateTo("house_register_1")
                            },
                            onChatClick = {
                                navigationManager.navigateTo(Route.ChatRoute.ChatList)
                            },
                            onNotificationClick = {
                                navigationManager.navigateTo(Route.NotificationRoute.Notification)
                            },
                            onMyPageClick = {
                                navigationManager.navigateTo(Route.MyPageRoute.MyPage)
                            }
                        )
                    }

                    composable<Route.CommunityPageRoute.SearchRegion> {
                        SearchRegionScreen(
                            onMapScreen = { coordinate ->
                                navigationManager.navigateOneWay(
                                    Route.CommunityPageRoute.SearchRegion,
                                    Route.CommunityPageRoute.Map(coordinate = coordinate)
                                )
                            },
                            onBackBtnClick = {
                                navigationManager.navigateOneWay(
                                    Route.CommunityPageRoute.SearchRegion,
                                    Route.CommunityPageRoute.Map(coordinate = null)
                                )
                            }
                        )
                    }

                    composable<Route.CommunityPageRoute.PostRoute.Posts> {
                        val postRegions = it.toRoute<Route.CommunityPageRoute.PostRoute.Posts>().postRegions
                        PostsScreen(
                            onPostClick = {
                                navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.DetailPost(it))
                            },
                            onBackBtnClick = {
                                navigationManager.navigateOneWay(
                                    Route.CommunityPageRoute.PostRoute.Posts(postRegions),
                                    Route.CommunityPageRoute.Map(coordinate = null)
                                )
                            }
                        )
                    }

                    composable<Route.CommunityPageRoute.PostRoute.DetailPost> {
                        DetailPostScreen(
                            onChatScreen = {
                                navigationManager.navigateTo("chat_detail/${it}")
                            }
                        )
                    }

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

    private fun handleTokenRefreshFailure() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isFailedRefreshToken", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}