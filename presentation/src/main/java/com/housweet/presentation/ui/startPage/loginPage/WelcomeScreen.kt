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
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onNextScreen: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(3000)
        onNextScreen()
    }
    BackOnPressed()
    WelcomeContent()
}

@Composable
private fun WelcomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(start = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(height = 117.dp))

        GuideText(
            color = Purple,
            text = "반가워요,\n하우스메이트님!",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            lineHeight = 30.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeContent()
}