package com.housweet.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.ColorGroup

@Composable
fun ComingSoonScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_coming_soon),
                contentDescription = "Coming Soon",
                modifier = Modifier.size(203.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "COMING SOON",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ColorGroup.Primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "준비중인 페이지입니다. 빠른 시일 내로 올게요!",
                fontSize = 12.sp,
                color = ColorGroup.Gray_7E7E7E,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun ComingSoonScreenPreview() {
    ComingSoonScreen()
}