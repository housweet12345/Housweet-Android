package com.housweet.presentation

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
import com.housweet.presentation.ui.chat.ChatScreen
import com.housweet.presentation.ui.chatlist.ChatListScreen
import com.housweet.presentation.ui.communityPage.mapScreen.MapScreen
import com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen.DetailPostScreen
import com.housweet.presentation.ui.communityPage.postScreen.postsScreen.PostsScreen
import com.housweet.presentation.ui.communityPage.searchRegionScreen.SearchRegionScreen
import com.housweet.presentation.ui.home.route.HomeRoute
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
import com.housweet.presentation.ui.navigation.BottomNavItem
import com.housweet.presentation.ui.navigation.CoordinateType
import com.housweet.presentation.ui.navigation.NavigationManager
import com.housweet.presentation.ui.navigation.Route
import com.housweet.presentation.ui.notification.NotificationScreen
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
                                navController.navigate("chat_list")
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
                                navigationManager.navigateTo(Route.HouseRegisterRoute.Step1(mode = RegisterModel.CREATE))
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
                            onPostClick = { id ->
                                navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.DetailPost(id))
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

                    composable<Route.HouseRegisterRoute.Step1> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step1>()
                        val mode = route.mode

                        HouseRegisterScreen1(
                            mode = mode,
                            onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step2(mode)) },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<Route.HouseRegisterRoute.Step2> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step2>()
                        val mode = route.mode

                        HouseRegisterScreen2(
                            mode = mode,
                            onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step3(mode)) },
                            onBackClick = { navController.navigate(Route.HouseRegisterRoute.Step1(mode)) }
                        )
                    }
                    composable<Route.HouseRegisterRoute.Step3> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step3>()
                        val mode = route.mode

                        HouseRegisterScreen3(
                            mode = mode,
                            onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step4(mode)) },
                            onBackClick = { navController.navigate(Route.HouseRegisterRoute.Step2(mode)) },
                            onImagePickClick = { launcher.launch("image/*") },
                            selectedImageBitmap = selectedImageBitmap.value,
                            viewModel = houseRegisterViewModel
                        )
                    }
                    composable<Route.HouseRegisterRoute.Step4> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step4>()
                        val mode = route.mode

                        HouseRegisterScreen4(
                            mode = mode,
                            onBackClick = { navController.navigate(Route.HouseRegisterRoute.Step3(mode)) },
                            onCompleteClick = {},
                            viewModel = houseRegisterViewModel
                        )
                    }

                    composable<Route.ChatRoute.ChatList> {
                        ChatListScreen(
                            navController = navController
                        )
                    }

                    composable("mypage") {
                        MyPageScreen(
                            navController
                        )
                    }

                    composable("bookmark") {
                        BookmarkScreen(
                            onItemClick = { /* TODO: 상세 페이지로 이동 등 처리 */ },
                            navController = navController
                        )
                    }

                    composable("posted_my_room") {
                        MyPostedRoomScreen(
                            navController
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

                    composable("app_setting") {
                        AppNotificationSettingsScreen(navController)
                    }

                    composable("notice") {
                        NoticeScreen(
                            onBackClick = { navController.popBackStack() },
                            navController
                        )
                    }

                    composable("help") {
                        HelpScreen(navController)
                    }

                    composable("terms_conditions_policies") {
                        TermsConditionsPolicies(navController)
                    }

                    composable("notification") {
                        NotificationScreen(
                            navController
                        )
                    }

                    composable("chat_list") {
                        ChatListScreen(
                            navController
                        )
                    }

                    composable(
                        route = "chat_detail/{receiverId}/{chatName}",
                        arguments = listOf(
                            navArgument("receiverId") { type = NavType.IntType },
                            navArgument("chatName") { defaultValue = "Unknown" }
                        )
                    )
                    { backStackEntry ->
                        val receiverId = backStackEntry.arguments?.getInt("receiverId") ?: -1
                        val encodedName =
                            backStackEntry.arguments?.getString("chatName") ?: "Unknown"
                        val chatName = String(
                            Base64.decode(
                                encodedName,
                                Base64.URL_SAFE or Base64.NO_WRAP
                            )
                        ) // ✅ 여기서 디코딩
                        ChatScreen(
                            chatName = chatName,
                            receiverId = receiverId,
                            navController = navController,
                        )
                    }

                    composable<Route.MyPageRoute.MyPage> {
                        MyPageScreen(
                            navController = navController
                        )
                    }

                    composable("bookmark") {
                        BookmarkScreen(
                            onItemClick = { /* TODO: 상세 페이지로 이동 등 처리 */ },
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

                    composable<Route.NotificationRoute.Notification> {
                        NotificationScreen(navController = navController)
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