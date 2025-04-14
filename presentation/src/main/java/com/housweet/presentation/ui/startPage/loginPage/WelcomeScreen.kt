package com.housweet.presentation.ui.startPage.loginPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.startPage.navigation.Route
import com.housweet.presentation.ui.startPage.navigation.StartPageNavigationManager
import com.housweet.presentation.ui.theme.BackgroundColor
import com.housweet.presentation.ui.theme.TextColorPurple
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(modifier: Modifier, navController: NavHostController) {
    val navigationManager = StartPageNavigationManager(navController)
    LaunchedEffect(key1 = true) {
        delay(3000)
        navigationManager.navigateOneWay(Route.LoginRoute.WelComeScreen, Route.LoginRoute.PermissionGuideScreen)
    }
    WelcomeScreen(modifier)
}

@Composable
private fun WelcomeScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(start = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(height = 117.dp))

        GuideText(
            color = TextColorPurple,
            text = "반가워요,",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        GuideText(
            color = TextColorPurple,
            text = "하우스메이트님!",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(Modifier)
}