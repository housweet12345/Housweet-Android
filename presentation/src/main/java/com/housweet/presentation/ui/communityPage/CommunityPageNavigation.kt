package com.housweet.presentation.ui.communityPage

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import kotlin.reflect.typeOf

@Composable
fun CommunityPageNavigation(paddingValue: PaddingValues) {
    val navController = rememberNavController()

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
                onViewPostBtnClick = {
                    navigationManager.navigateTo(Route.CommunityPageRoute.PostRoute.Posts)
                },
                onSearchBtnClick = {
                    navigationManager.navigateTo(Route.CommunityPageRoute.SearchRegion)
                },
                onWritePostBtnClick = {

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
                        Route.CommunityPageRoute.Map()
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
    }
}