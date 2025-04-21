package com.housweet.presentation.ui.startPage.loginPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentaion.R
import com.housweet.presentation.ui.startPage.BackOnPressed
import com.housweet.presentation.ui.startPage.BottomButton
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.dmsansFontFamily
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun PermissionGuideScreen(modifier: Modifier, onNextScreen: () -> Unit) {
    BackOnPressed()
    PermissionGuideContent(modifier = modifier) {
        onNextScreen()
    }
}

@Composable
private fun PermissionGuideContent(modifier: Modifier, onPermitBtnClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Spacer(modifier = Modifier.height(height = 117.dp))

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Purple,
            text = "하우스잇 사용을 위해",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            fontFamily = dmsansFontFamily,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Purple,
            text = "아래의 접근 권한이 필요해요.",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            fontFamily = dmsansFontFamily,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(height = 32.dp))

        PermissionGuideItem(
            icon = R.drawable.camera_icon,
            content = "카메라 (선택)",
            description = "사진 첨부 시 촬영을 위한 사용"
        )

        PermissionGuideItem(
            icon = R.drawable.gallery_icon,
            content = "갤러리 (선택)",
            description = "사진 첨부 시 사용"
        )

        PermissionGuideItem(
            icon = R.drawable.alarm_icon,
            content = "알람 (선택)",
            description = "알림 기능 시 사용"
        )

        PermissionGuideItem(
            icon = R.drawable.location_icon,
            content = "위치 정보 (선택)",
            description = "메이트 찾기 시 사용"
        )

        Spacer(modifier = Modifier.height(height = 32.dp))

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Gray_7E7E7E,
            text = "・ 허용하지 않으셔도 앱 사용은 가능하나,",
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            fontFamily = nanumSquareFontFamily,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Gray_7E7E7E,
            text = "   일부 서비스 이용에 제한이 있을 수 있습니다.",
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            fontFamily = nanumSquareFontFamily,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(height = 8.dp))
        
        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Gray_7E7E7E,
            text = "・ 설정 > 하우스잇에서 권한을 변경할 수 있습니다.",
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            fontFamily = nanumSquareFontFamily,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.weight(weight = 1f))

        BottomButton(text = "허용하기") {
            onPermitBtnClick()
        }
    }
}

@Composable
private fun PermissionGuideItem(
    icon: Int,
    content: String,
    description: String,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = content,
                tint = Purple
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Column {
                GuideText(
                    modifier = Modifier,
                    color = Purple,
                    text = content,
                    fontWeight = FontWeight.W800,
                    fontSize = 14.sp,
                    fontFamily = nanumSquareFontFamily,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )

                GuideText(
                    modifier = Modifier,
                    color = Black,
                    text = description,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    fontFamily = nanumSquareFontFamily,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color(0xFFCBCBCB)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionGuideScreenPreview() {
    PermissionGuideContent(modifier = Modifier) { }
}