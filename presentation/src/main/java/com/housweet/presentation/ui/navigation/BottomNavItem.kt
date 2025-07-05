package com.housweet.presentation.ui.navigation

sealed class BottomNavItem(val route: String, val title: String) {
    data object Home : BottomNavItem("home", "홈")
    data object Calendar : BottomNavItem("calendar", "캘린더")
    data object FinanceLedger : BottomNavItem("financeLedger", "가계부")
    data object Notice : BottomNavItem("notice", "공지사항")
}
