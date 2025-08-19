package com.housweet.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
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
import com.housweet.presentation.ui.common.ComingSoonScreen
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
import com.housweet.presentation.ui.mypage.TermsLocationInformationPolies
import com.housweet.presentation.ui.mypage.TermsPrivacyPolicies
import com.housweet.presentation.ui.mypage.deleteAccount.DeleteAccountScreen
import com.housweet.presentation.ui.navigation.BottomNavItem
import com.housweet.presentation.ui.navigation.BottomNavigation
import com.housweet.presentation.ui.navigation.CoordinateType
import com.housweet.presentation.ui.navigation.NavigationManager
import com.housweet.presentation.ui.navigation.Route
import com.housweet.presentation.ui.notification.NotificationScreen
import com.housweet.presentation.ui.profile.route.EditProfileKeyWordRoute
import com.housweet.presentation.ui.profile.route.EditProfileRoute
import com.housweet.presentation.ui.profile.route.ProfileRoute
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
import com.housweet.presentation.ui.userlist.route.UserListRoute
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
        val isLogout = intent.getBooleanExtra("isLogout", false)
        val isDeleteAccount = intent.getBooleanExtra("isDeleteAccount", false)

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
            val navigationManager = NavigationManager(navController)

            val scope = rememberCoroutineScope()

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
                    startDestination = if (!isFailedRefreshToken && !isLogout && !isDeleteAccount) Route.StartPageRoute.Splash else Route.StartPageRoute.LoginRoute.Login,

                    // access_token Log 확인 테스트용
                    // startDestination = Route.StartPageRoute.LoginRoute.Login,
//                     startDestination = Route.StartPageRoute.LoginRoute.Login,
//                    startDestination= "mypage",
                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
                ) {
                    composable<Route.StartPageRoute.Splash> {
                        SplashScreen(
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        ) { isAutoLogin, isAgreeTermsOfService, isSetProfile, isBelongToRoom ->
                            when {
                                !isAutoLogin -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.Splash,
                                        Route.StartPageRoute.LoginRoute.Login
                                    )
                                }

                                isAgreeTermsOfService -> {
                                    if (!isSetProfile) {
                                        navigationManager.navigateOneWay(
                                            Route.StartPageRoute.Splash,
                                            "profile/edit?fromTerms=true"
                                        )

                                        return@SplashScreen
                                    }

                                    if (!isBelongToRoom) {
                                        navigationManager.navigateOneWay(
                                            Route.StartPageRoute.Splash,
                                            Route.StartPageRoute.AccessRoomRoute.AccessRoom
                                        )

                                        return@SplashScreen
                                    }

                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.Splash,
                                        BottomNavItem.Home.route
                                    )
                                }

                                else -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.Splash,
                                        Route.StartPageRoute.LoginRoute.WelCome(isSetProfile, isBelongToRoom)
                                    )
                                }
                            }
                        }

                        // ✅ 임시: 앱 진입 시 바로 AccessRoom으로 (테스트용)
//                        SplashScreen(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),)
//                        { _, _, _ ->
//                            navigationManager.navigateOneWay(
//                                Route.StartPageRoute.Splash,
//                                Route.StartPageRoute.AccessRoomRoute.AccessRoom
//                            )
//                        }

                    }

                    composable<Route.StartPageRoute.LoginRoute.Login> {
                        LoginScreen(
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        ) { isTermsOfServiceAgreed, isSetProfile ,isBelongToRoom ->
                            when {
                                !isTermsOfServiceAgreed -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.LoginRoute.Login,
                                        Route.StartPageRoute.LoginRoute.WelCome(isBelongToRoom, false)
                                    )
                                }

                                !isSetProfile -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.LoginRoute.Login,
                                        "profile/edit?fromTerms=true"
                                    )
                                }

                                isBelongToRoom -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.LoginRoute.Login,
                                        BottomNavItem.Home.route
                                    )
                                }

                                else -> {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.LoginRoute.Login,
                                        Route.StartPageRoute.AccessRoomRoute.AccessRoom
                                    )
                                }
                            }
                        }

                        // ✅ 임시: 로그인 시 바로 AccessRoom으로 (테스트용)
//                         LoginScreen { _, _ ->
//                             navigationManager.navigateOneWay(
//                                 Route.StartPageRoute.LoginRoute.Login,
//                                 Route.StartPageRoute.AccessRoomRoute.AccessRoom
//                             )
//                         }

                    }

                    composable<Route.StartPageRoute.LoginRoute.WelCome> {
                        val isBelongToRoom = it.toRoute<Route.StartPageRoute.LoginRoute.WelCome>().isBelongToRoom
                        val isSetProfile = it.toRoute<Route.StartPageRoute.LoginRoute.WelCome>().isSetProfile
                        WelcomeScreen {
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.LoginRoute.WelCome(isSetProfile, isBelongToRoom),
                                Route.StartPageRoute.LoginRoute.PermissionGuide(isSetProfile, isBelongToRoom)
                            )
                        }
                    }

                    composable<Route.StartPageRoute.LoginRoute.PermissionGuide> {
                        val isBelongToRoom = it.toRoute<Route.StartPageRoute.LoginRoute.PermissionGuide>().isBelongToRoom
                        val isSetProfile = it.toRoute<Route.StartPageRoute.LoginRoute.PermissionGuide>().isSetProfile
                        PermissionGuideScreen(
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        ) {
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.LoginRoute.PermissionGuide(isSetProfile, isBelongToRoom),
                                Route.StartPageRoute.LoginRoute.TermsOfService(isSetProfile,isBelongToRoom)
                            )
                        }
                    }

                    composable<Route.StartPageRoute.LoginRoute.TermsOfService> {
                        val isBelongToRoom = it.toRoute<Route.StartPageRoute.LoginRoute.TermsOfService>().isBelongToRoom
                        val isSetProfile = it.toRoute<Route.StartPageRoute.LoginRoute.TermsOfService>().isSetProfile
                        TermsOfServiceScreen(
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                            onNextScreen = {
                                if (!isSetProfile) {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.LoginRoute.TermsOfService(
                                            false,
                                            isBelongToRoom
                                        ),
                                        "profile/edit?fromTerms=true"
                                    )

                                    return@TermsOfServiceScreen
                                }

                                if (!isBelongToRoom) {
                                    navigationManager.navigateOneWay(
                                        Route.StartPageRoute.LoginRoute.TermsOfService(
                                            isSetProfile = true,
                                            isBelongToRoom = false
                                        ),
                                        Route.StartPageRoute.AccessRoomRoute.AccessRoom
                                    )

                                    return@TermsOfServiceScreen
                                }
                                navigationManager.navigateOneWay(
                                    Route.StartPageRoute.LoginRoute.TermsOfService(isSetProfile = true, isBelongToRoom = true),
                                    BottomNavItem.Home.route
                                )
                            },
                            onDetailTermsConditionsPoliciesClick = {
                                navigationManager.navigateTo("terms_conditions_policies")
                            },
                            onDetailTermsPrivacyPoliciesClick = {
                                navigationManager.navigateTo("terms_privacy_policies")
                            },
                            onDetailTermsLocationInformationPoliesClick = {
                                navigationManager.navigateTo("terms_location_information_policies")
                            }
                        )
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
                        CreateRoomScreen(
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        ){
                            navigationManager.navigateOneWay(
                                Route.StartPageRoute.AccessRoomRoute.AccessRoom,
                                BottomNavItem.Home.route
                            )
                        }
                    }

                    composable<Route.StartPageRoute.AccessRoomRoute.SearchRoom> {
                        SearchRoomScreen(
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                        ){
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
                                navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.Posts(postRegion))
                            },
                            onViewPostBtnClick = { postRegions ->
                                navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.Posts(postRegions))
                            },
                            onSearchBtnClick = {
                                navigationManager.navigateTo(Route.CommunityPageRoute.SearchRegion)
                            },
                            onWritePostBtnClick = {
                                navigationManager.navigateTo(Route.HouseRegisterRoute.Step1(mode = RegisterModel.CREATE))
                            },
                            onChatClick = {
                                navigationManager.navigateTo("chat_list")
                            },
                            onNotificationClick = {
                                navigationManager.navigateTo("notification")
                            },
                            onMyPageClick = {
                                navigationManager.navigateTo("mypage")
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
                                    Route.CommunityPageRoute.Map()
                                )
                            }
                        )
                    }

                    composable<Route.CommunityPageRoute.PostRoute.Posts> {
                        val postRegions = it.toRoute<Route.CommunityPageRoute.PostRoute.Posts>().postRegions
                        val updatePostId = it.toRoute<Route.CommunityPageRoute.PostRoute.Posts>().updatePostId
                        PostsScreen(
                            updatePostId = updatePostId,
                            onPostClick = { id, lastRegion ->
                                navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.DetailPost(id, lastRegion))
                            },
                            onBackBtnClick = {
                                navigationManager.navigateOneWay(
                                    Route.CommunityPageRoute.PostRoute.Posts(postRegions, updatePostId),
                                    Route.CommunityPageRoute.Map()
                                )
                            },
                        )
                    }

                    composable<Route.CommunityPageRoute.PostRoute.DetailPost> {
                        val postId = it.toRoute<Route.CommunityPageRoute.PostRoute.DetailPost>().postId
                        val lastRegion = it.toRoute<Route.CommunityPageRoute.PostRoute.DetailPost>().lastRegion
                        val chatListViewModel: com.housweet.presentation.viewmodel.chatlist.ChatListViewModel = hiltViewModel()
                        val myId by chatListViewModel.myUserId.collectAsStateWithLifecycle()

                        // 필요 시 초기 로드
                        LaunchedEffect(Unit) {
                            if (myId == null) chatListViewModel.refreshMyUserIdAndUsers()
                        }

                        DetailPostScreen(
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                            onChatScreen = { receiverId, nickName ->
                                val sender = myId ?: run {
                                    // 스낵바 등으로 로그인 필요 메시지 처리
                                    return@DetailPostScreen
                                }
                                chatListViewModel.createRoomAndShow(
                                    senderId = sender,
                                    receiverId = receiverId,
                                    counterpartNickname = nickName
                                ) { roomId ->
                                    val encoded = android.util.Base64.encodeToString(
                                        nickName.toByteArray(),
                                        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING // ← 패딩 제거
                                    )
                                    navigationManager.navigateTo("chat_detail/$receiverId/$roomId/$encoded")
                                }
                            },
                            onProfileScreen = { userId -> navigationManager.navigateTo("profile/$userId") },
                            onBackBtnClick = { isBookmarkChanged ->
                                if (isBookmarkChanged) {
                                    navigationManager.navigateOneWay(
                                        Route.CommunityPageRoute.PostRoute.DetailPost(postId, lastRegion),
                                        Route.CommunityPageRoute.PostRoute.Posts(updatePostId = postId)
                                    )
                                } else {
                                    navigationManager.navigateOneWay(
                                        Route.CommunityPageRoute.PostRoute.DetailPost(postId, lastRegion),
                                        Route.CommunityPageRoute.PostRoute.Posts()
                                    )
                                }
                            }
                        )
                    }

                    composable(BottomNavItem.Home.route) {
                        HomeRoute(
                            navigateToChat = { navController.navigate("chat_list") },
                            navigateToNotification = { navController.navigate("notification") },
//                            navigateToProfile = { navController.navigate("profile/me") },
                            navigateToMyPage = { navController.navigate("mypage") },
                            navigateToNoticeDetail = { noticeId -> /* TODO: 공지사항 상세 */ },
                            navigateToTodoDetail = { navController.navigate(BottomNavItem.Calendar.route) },
                            navigateToUserList = { navigationManager.navigateTo("roommate/userlist") },
                            navController = navController
                        )
                    }

                    composable(BottomNavItem.Calendar.route) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            ComingSoonScreen(
                                modifier = Modifier.weight(1f)
                            )
                            BottomNavigation(
                                navController = navController
                            )
                        }
                    }
                    composable(BottomNavItem.FinanceLedger.route) {
                        //가계부 화면
                        Column(modifier = Modifier.fillMaxSize()) {
                            ComingSoonScreen(
                                modifier = Modifier.weight(1f)
                            )
                            BottomNavigation(
                                navController = navController
                            )
                        }
                    }
                    composable(BottomNavItem.Notice.route) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            ComingSoonScreen(
                                modifier = Modifier.weight(1f)
                            )
                            BottomNavigation(
                                navController = navController
                            )
                        }
                    }

                    composable<Route.HouseRegisterRoute.Step1> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step1>()
                        val mode = route.mode
                        val postingId = route.postingId

                        HouseRegisterScreen1(
                            mode = mode,
                            postingId = postingId,
                            onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step2(mode)) },
                            onBackClick = {
                                if (mode == RegisterModel.EDIT) {
                                    // 1) 백스택에 이미 MyPostedRoomScreen 이 있으면 거기로 '직행' (중간 스텝들 모두 제거)
                                    val popped = navController.popBackStack("posted_my_room", /* inclusive = */ false)
                                    if (!popped) {
                                        // 2) 없으면 새로 띄우되, 현재 Step1(EDIT) 자체는 지우고 단일 상단으로 정리
                                        navController.navigate("posted_my_room") {
                                            // 그래프 시작점까지 정리하거나, 필요 시 특정 지점으로 조정 가능
                                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    }
                                } else {
                                    // 기존 CREATE 플로우 동작 유지
                                    val previousRoute = navController.previousBackStackEntry?.destination?.route
                                    if (previousRoute?.contains("CommunityPageRoute.Map") == true) {
                                        navigationManager.navigateOneWay(
                                            Route.HouseRegisterRoute.Step1(mode),
                                            Route.CommunityPageRoute.Map()
                                        )
                                    } else {
                                        navController.popBackStack()
                                    }
                                }
                            },
                          viewModel = houseRegisterViewModel
                        )
                    }
                    composable<Route.HouseRegisterRoute.Step2> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step2>()
                        val mode = route.mode
                        val postingId = route.postingId

                        HouseRegisterScreen2(
                            mode = mode,
                            postingId = postingId,
                            onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step3(mode)) },
                            onBackClick = { navController.popBackStack() },
                            viewModel = houseRegisterViewModel
                        )
                    }
                    composable<Route.HouseRegisterRoute.Step3> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step3>()
                        val mode = route.mode
                        val postingId = route.postingId

                        HouseRegisterScreen3(
                            mode = mode,
                            postingId = postingId,
                            onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step4(mode)) },
                            onBackClick = { navController.popBackStack() },
                            viewModel = houseRegisterViewModel
                        )
                    }
                    composable<Route.HouseRegisterRoute.Step4> {
                        val route = it.toRoute<Route.HouseRegisterRoute.Step4>()
                        val mode = route.mode
                        val postingId = route.postingId

                        HouseRegisterScreen4(
                            mode = mode,
                            postingId = postingId,
                            onBackClick = { navController.popBackStack() },
                            onCompleteClick = {
                                scope.launch {
                                    if (mode == RegisterModel.EDIT) {
                                        // 알림
                                        snackBarHostState.showSnackbar(
                                            message = "방 공고 수정이 완료 되었습니다.",
                                            duration = SnackbarDuration.Short
                                        )

                                        // 이동: 올린 방 관리
                                        repeat(4) { navController.popBackStack() } // Step4→3→2→1 제거
                                        navController.navigate("posted_my_room") {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        // 알림
                                        snackBarHostState.showSnackbar(
                                            message = "방 공고 생성이 완료 되었습니다.",
                                            duration = SnackbarDuration.Short
                                        )
                                        // 이동: 지도 화면
                                        repeat(4) { navController.popBackStack() } // Step4→3→2→1 제거
                                        navController.navigate(Route.CommunityPageRoute.Map()) {
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            },
                            viewModel = houseRegisterViewModel,
                            onShowSnackbar = { msg ->
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = msg,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }

                    composable(
                        route = "mypage?afterDelete={afterDelete}",
                        arguments = listOf(
                            navArgument("afterDelete") {
                                type = NavType.BoolType
                                defaultValue = false
                            }
                        )
                    ) { backStackEntry ->
                        val afterDelete = backStackEntry.arguments?.getBoolean("afterDelete") ?: false

                        MyPageScreen(
                            navController = navController,
                            onBackClick = {
                                if (afterDelete) {
                                    // ✅ 삭제 플로우로 진입했으면 Home 대신 AccessRoom으로
                                    navigationManager.navigateOneWay(
                                        "mypage?afterDelete=$afterDelete",
                                        Route.StartPageRoute.AccessRoomRoute.AccessRoom
                                    )
                                } else {
                                    // 기존 동작 유지
                                    val previousRoute = navController.previousBackStackEntry?.destination?.route
                                    if (previousRoute?.contains("CommunityPageRoute.Map") == true) {
                                        navigationManager.navigateOneWay(
                                            "mypage",
                                            Route.CommunityPageRoute.Map()
                                        )
                                    } else {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            onLogoutClick = {
                                startViewModel.logout { handleLogout() }
                            },
                            onDeleteAccountClick = {
                                navigationManager.navigateTo("deleteAccount")
                            }
                        )
                    }

                    composable("deleteAccount") {
                        DeleteAccountScreen(
                            onBackClick = { navController.popBackStack() },
                            onSuccessDeleteAccount = { handleDeleteAccount() }
                        )
                    }

                    composable("bookmark") {
                        BookmarkScreen(
                            navController = navController,
                            onItemClick = { uiItem ->
                                // 타입세이프 라우트로 디테일 이동
                                navigationManager.navigateTo(
                                    Route.CommunityPageRoute.PostRoute.DetailPost(uiItem.id)
                                )
                            }
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
                            navController,
                        )
                    }

                    composable("help") {
                        HelpScreen(navController)
                    }

                    composable("terms_conditions_policies") {
                        TermsConditionsPolicies(navController)
                    }

                    composable("terms_privacy_policies") {
                        TermsPrivacyPolicies {
                            navController.popBackStack()
                        }
                    }

                    composable("terms_location_information_policies") {
                        TermsLocationInformationPolies {
                            navController.popBackStack()
                        }
                    }

                    composable("notification") {
                        NotificationScreen(
                            navController
                        ) {
                            val previousRoute = navController.previousBackStackEntry?.destination?.route
                            if (previousRoute?.contains("CommunityPageRoute.Map") == true) {
                                navigationManager.navigateOneWay(
                                    "notification",
                                    Route.CommunityPageRoute.Map()
                                )
                            } else {
                                navController.popBackStack()
                            }
                        }
                    }

                    composable("chat_list") {
                        ChatListScreen(
                            navController,
                            onBackClick = {
                                val previousRoute = navController.previousBackStackEntry?.destination?.route
                                if (previousRoute?.contains("CommunityPageRoute.Map") == true) {
                                    navigationManager.navigateOneWay(
                                        "chat_list",
                                        Route.CommunityPageRoute.Map()
                                    )
                                } else {
                                    navController.popBackStack()
                                }
                            }
                        )
                    }

                    composable(
                        route = "chat_detail/{receiverId}/{roomId}/{chatName}",
                        arguments = listOf(
                            navArgument("receiverId") { type = NavType.IntType },
                            navArgument ( "roomId" ) { type = NavType.IntType},
                            navArgument("chatName") { type = NavType.StringType }
                        )
                    )
                    { backStackEntry ->
                        val receiverId = backStackEntry.arguments?.getInt("receiverId") ?: -1
                        val roomId     = backStackEntry.arguments?.getInt("roomId") ?: -1
                        val encodedName = backStackEntry.arguments?.getString("chatName") ?: ""
                        val chatName = runCatching {
                            String(Base64.decode(encodedName, Base64.URL_SAFE or Base64.NO_WRAP))
                        }.getOrElse { encodedName } // 혹시 디코딩 실패하면 원문 사용
                        ChatScreen(
                            chatName = chatName,
                            navController = navController,
                            receiverId = receiverId,
                            roomId = roomId
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

                    // ✅ 추가
                    composable(
                        route = "notice_detail/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                        NoticeDetailScreen(
                            id = id,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "profile/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.StringType })
                    ) {
                        val userId = it.arguments?.getString("userId")
                        val chatListViewModel: com.housweet.presentation.viewmodel.chatlist.ChatListViewModel = hiltViewModel()
                        val myId by chatListViewModel.myUserId.collectAsStateWithLifecycle()

                        // 필요 시 초기 로드
                        LaunchedEffect(Unit) {
                            if (myId == null) chatListViewModel.refreshMyUserIdAndUsers()
                        }

                        ProfileRoute(
                            userId = userId,
                            navigateEditProfile = { navController.navigate("profile/edit?fromTerms=false") },
                            onBackClick = { navController.popBackStack() },
                            navigateChatting = {receiverId, nickName ->
                                val sender = myId ?: run {
                                // 스낵바 등으로 로그인 필요 메시지 처리
                                return@ProfileRoute
                            }
                                chatListViewModel.createRoomAndShow(
                                    senderId = sender,
                                    receiverId = receiverId,
                                    counterpartNickname = nickName
                                ) { roomId ->
                                    val encoded = android.util.Base64.encodeToString(
                                        nickName.toByteArray(),
                                        android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING // ← 패딩 제거
                                    )
                                    navigationManager.navigateTo("chat_detail/$receiverId/$roomId/$encoded")
                                }
                            }
                        )
                    }

                    composable(
                        route = "profile/edit?fromTerms={fromTerms}",
                        arguments = listOf(navArgument("fromTerms") {
                            type = NavType.BoolType
                            defaultValue = false
                        })
                    ) { backStackEntry ->
                        val fromTerms = backStackEntry.arguments?.getBoolean("fromTerms") ?: false
                        EditProfileRoute(
                            onBackClick = { navController.popBackStack() },
                            onSuccessNavigateBack = {
                                if (fromTerms) {
                                    navController.navigate(Route.StartPageRoute.AccessRoomRoute.AccessRoom) {
                                        popUpTo("profile/edit?fromTerms=$fromTerms") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate("profile/1") {
                                        popUpTo("profile/edit?fromTerms=$fromTerms") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            navigateEditKeyword = {
                                navController.navigate("profile/edit_keyword?fromTerms=$fromTerms")
                            }
                        )
                    }

                    composable(
                        route = "profile/edit_keyword?fromTerms={fromTerms}",
                        arguments = listOf(navArgument("fromTerms") {
                            type = NavType.BoolType
                            defaultValue = false
                        })
                    ) { navBackStackEntry ->
                        val fromTerms = navBackStackEntry.arguments?.getBoolean("fromTerms") ?: false
                        val parentEntry = remember(navBackStackEntry) {
                            navController.getBackStackEntry("profile/edit?fromTerms=$fromTerms")
                        }
                        EditProfileKeyWordRoute(
                            onBackClick = { navController.popBackStack() },
                            viewModel = hiltViewModel(parentEntry),
                            showSkipButton = fromTerms,
                            onSuccessNavigateBack = {
                                if (fromTerms) {
                                    navController.navigate(Route.StartPageRoute.AccessRoomRoute.AccessRoom) {
                                        popUpTo("profile/edit?fromTerms=$fromTerms") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate("profile/me") {
                                        popUpTo("profile/edit?fromTerms=$fromTerms") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            },
                            onSkipClick = {
                                if (fromTerms) {
                                    navigationManager.navigateOneWay(
                                        "profile/edit_keyword?fromTerms=$fromTerms",
                                        Route.StartPageRoute.AccessRoomRoute.AccessRoom
                                    )
                                } else {
                                    navigationManager.navigateOneWay(
                                        "profile/edit_keyword?fromTerms=$fromTerms",
                                        "profile/me"
                                    )
                                }
                            }
                        )
                    }

                    composable("roommate/userlist") {
                        UserListRoute(
                            onBackClick = { navController.popBackStack() },
                            navigateToProfile = { navController.navigate("profile/$it") },
                            onWorkspaceInvite = { navController.navigate(Route.CommunityPageRoute.Map()) }
                        )
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

    private fun handleLogout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isLogout", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun handleDeleteAccount() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isDeleteAccount", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}