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
import com.housweet.presentation.ui.startPage.BackOnPressed
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.dmsansFontFamily
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(modifier: Modifier, onNextScreen: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(3000)
        onNextScreen()
    }
    BackOnPressed()
    WelcomeContent(modifier)
}

@Composable
private fun WelcomeContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(height = 117.dp))

        GuideText(
            color = Purple,
            text = "반가워요,",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            fontFamily = dmsansFontFamily,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        GuideText(
            color = Purple,
            text = "하우스메이트님!",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            fontFamily = dmsansFontFamily,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeContent(Modifier)
}