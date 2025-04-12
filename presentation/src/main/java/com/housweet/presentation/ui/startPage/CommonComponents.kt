package com.housweet.presentation.ui.startPage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun GuideText(
    modifier: Modifier = Modifier,
    color: Color,
    text: String,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    textAlign: TextAlign
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontFamily = FontFamily.Default,
            fontWeight = fontWeight,
            fontSize = fontSize,
            lineHeight = lineHeight,
            letterSpacing = 0.sp,
            textAlign = textAlign
        )
    )
}