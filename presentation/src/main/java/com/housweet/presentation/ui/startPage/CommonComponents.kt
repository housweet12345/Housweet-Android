package com.housweet.presentation.ui.startPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.housweet.presentaion.R
import com.housweet.presentation.ui.theme.BackgroundColor
import com.housweet.presentation.ui.theme.TextColorGrayd

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

@Composable
fun LoadingBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(ImageDecoderDecoder.Factory())
            }
            .build()

        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context).data(data = R.drawable.loading_bar)
                        .apply(block = {
                            size(
                                Size.ORIGINAL
                            )
                        }).build(),
                    imageLoader = imageLoader
                ),
                contentDescription = "Loading Bar",
                modifier = Modifier.fillMaxWidth()
            )

            GuideText(
                modifier = Modifier.fillMaxWidth(),
                color = TextColorGrayd,
                text = "잠시만 기다려 주세요!",
                fontWeight = FontWeight.W700,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingBarPreview() {
    LoadingBar()
}