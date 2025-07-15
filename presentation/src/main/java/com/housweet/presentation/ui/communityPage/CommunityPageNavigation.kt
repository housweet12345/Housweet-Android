package com.housweet.presentation.ui.communityPage

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.housweet.domain.model.Coordinate
import com.housweet.presentation.ui.communityPage.mapScreen.MapScreen
import com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen.DetailPostScreen
import com.housweet.presentation.ui.communityPage.postScreen.postsScreen.PostsScreen
import com.housweet.presentation.ui.communityPage.searchRegionScreen.SearchRegionScreen
import com.housweet.presentation.ui.navigation.CoordinateType
import com.housweet.presentation.ui.navigation.NavigationManager
import com.housweet.presentation.ui.navigation.Route
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen1
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen2
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen3
import com.housweet.presentation.ui.registerhouse.HouseRegisterScreen4
import kotlin.reflect.typeOf

@Composable
fun CommunityPageNavigation(paddingValue: PaddingValues) {
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

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.CommunityPageRoute.Map()
    ) {
        val navigationManager = NavigationManager(navController)
        composable<Route.CommunityPageRoute.Map>(
            typeMap = mapOf(typeOf<Coordinate?>() to CoordinateType)
        ) {
            val coordinate = it.toRoute<Route.CommunityPageRoute.Map>().coordinate
            MapScreen(
                modifier = Modifier.padding(top = paddingValue.calculateTopPadding()),
                searchRegion = coordinate,
                onMarkerClick = {
                    navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.Posts)
                },
                onViewPostBtnClick = {
                    navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.Posts)
                },
                onSearchBtnClick = {
                    navigationManager.navigateTo(Route.CommunityPageRoute.SearchRegion)
                },
                onWritePostBtnClick = {
                    navigationManager.navigateTo(Route.HouseRegisterRoute.Step1)
                }
            )
        }

        composable<Route.CommunityPageRoute.SearchRegion> {
            SearchRegionScreen(
                modifier = Modifier.padding(top = paddingValue.calculateTopPadding()),
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
            PostsScreen(
                modifier = Modifier.padding(top = paddingValue.calculateTopPadding()),
                onPostClick = {
                    navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.DetailPost)
                }
            )
        }

        composable<Route.CommunityPageRoute.PostRoute.DetailPost> {
            DetailPostScreen(
                modifier = Modifier.padding(bottom = paddingValue.calculateBottomPadding())
            )
        }

        composable<Route.HouseRegisterRoute.Step1> {
            HouseRegisterScreen1(
                onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step2) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<Route.HouseRegisterRoute.Step2> {
            HouseRegisterScreen2(
                onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step3) },
                onBackClick = { navController.navigate(Route.HouseRegisterRoute.Step1) }
            )
        }
        composable<Route.HouseRegisterRoute.Step3> {
            HouseRegisterScreen3(
                onNextClick = { navController.navigate(Route.HouseRegisterRoute.Step4) },
                onBackClick = { navController.navigate(Route.HouseRegisterRoute.Step2) },
                onImagePickClick = { launcher.launch("image/*") },
                selectedImageBitmap = selectedImageBitmap.value
            )
        }

        composable<Route.HouseRegisterRoute.Step4> {
            HouseRegisterScreen4(
                onBackClick = { navController.navigate(Route.HouseRegisterRoute.Step2) },
                onCompleteClick = { }
            )
        }
    }
}