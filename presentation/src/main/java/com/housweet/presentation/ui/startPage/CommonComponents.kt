package com.housweet.presentation.ui.startPage

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.housweet.presentaion.R
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Black_1A435671
import com.housweet.presentation.ui.theme.Gray_7D7D7D
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun GuideText(
    modifier: Modifier = Modifier,
    color: Color,
    text: String,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    fontFamily: FontFamily = FontFamily.Default,
    lineHeight: TextUnit,
    textAlign: TextAlign
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            ),
            letterSpacing = 0.sp,
            textAlign = textAlign
        )
    )
}

@Composable
fun BottomButton(
    text: String,
    onBtnClick: () -> Unit
) {
    Button(
        onClick = { onBtnClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple,
            contentColor = White
        )
    ) {
        GuideText(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            text = text,
            fontWeight = FontWeight.W700,
            fontSize = 15.sp,
            fontFamily = nanumSquareFontFamily,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WriteTextFiled(
    text: String,
    textLength: Int = 20,
    textColor: Color = Black,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = {
            if (it.length <= textLength) {
                onValueChange(it)
            }
        },
        modifier = Modifier.padding(horizontal = 20.dp),
        textStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.W400,
            fontFamily = nanumSquareFontFamily,
            color = textColor
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Black_1A435671,
                        ambientColor = Black_1A435671
                    ),
                shape = RoundedCornerShape(6.dp),
                color = White,
                border = BorderStroke(width = 0.2.dp, color = Gray_CBCBCB)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun WarningText(
    text: String
) {
    Row(modifier = Modifier
        .padding(start = 22.dp)
        .height(18.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.bang),
            contentDescription = "bang",
            modifier = Modifier.size(12.dp)
        )

        GuideText(
            color = Purple,
            text = text,
            fontWeight = FontWeight.W400,
            fontSize = 10.sp,
            fontFamily = nanumSquareFontFamily,
            lineHeight = 18.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun LoadingBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
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
                color = Gray_7D7D7D,
                text = "잠시만 기다려 주세요!",
                fontWeight = FontWeight.W700,
                fontFamily = nanumSquareFontFamily,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedTime = 0L // 뒤로가기 버튼을 눌렀던 시간을 저장하는 변수

    BackHandler(enabled = true) {
        // 만약 전에 뒤로가기 버튼 누른 시간과 특정한 시간 만큼 차이가 나지 않으면 앱종료.
        if(System.currentTimeMillis() - backPressedTime <= 400L) {
            (context as Activity).finish() // 앱 종료
        } else {
            // 특정한 시간 이상으로 차이가 난다면 토스트로 한 번 더 버튼을 누르라고 알림
            Toast.makeText(context, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        // 뒤로가기 버튼을 눌렀던 시간을 저장
        backPressedTime = System.currentTimeMillis()
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomButtonPreview() {
    BottomButton(
        text = "로그인",
        onBtnClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun LoadingBarPreview() {
   LoadingBar()
}

