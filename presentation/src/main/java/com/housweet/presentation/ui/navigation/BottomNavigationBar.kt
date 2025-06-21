package com.housweet.presentation.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.ColorGroup

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Calendar,
        BottomNavItem.FinanceLedger,
        BottomNavItem.Notice
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        color = Color.White,
        shadowElevation = 12.dp, // 그림자 높이
        tonalElevation = 5.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent, // Surface가 배경을 처리하므로 투명
            contentColor = ColorGroup.Primary,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        val painterResource = when (item) {
                            is BottomNavItem.Home -> painterResource(R.drawable.ic_navigation_home)
                            is BottomNavItem.Calendar -> painterResource(R.drawable.ic_navigation_calendar)
                            is BottomNavItem.FinanceLedger -> painterResource(R.drawable.ic_navigation_finance_ledgar)
                            is BottomNavItem.Notice -> painterResource(R.drawable.ic_navigation_notice)
                        }
                        Icon(
                            painter = painterResource,
                            contentDescription = item.title,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            // 백스택 정리
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorGroup.Primary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}