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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.housweet.presentaion.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.startPage.navigation.StartPageNavigationManager
import com.housweet.presentation.ui.theme.BackgroundColor
import com.housweet.presentation.ui.theme.PermitBtnColor
import com.housweet.presentation.ui.theme.TextColorBlack
import com.housweet.presentation.ui.theme.TextColorGraye
import com.housweet.presentation.ui.theme.TextColorPurple
import com.housweet.presentation.ui.theme.TextColorWhite

@Composable
fun PermissionGuideScreen(modifier: Modifier, navController: NavHostController) {
    val navigationManager = StartPageNavigationManager(navController)
    PermissionGuideScreen(modifier = modifier) { }
}

@Composable
private fun PermissionGuideScreen(modifier: Modifier, onPermitBtnClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Spacer(modifier = Modifier.height(height = 117.dp))

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = TextColorPurple,
            text = "하우스잇 사용을 위해",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = TextColorPurple,
            text = "아래의 접근 권한이 필요해요.",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
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
            color = TextColorGraye,
            text = "・ 허용하지 않으셔도 앱 사용은 가능하나,",
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = TextColorGraye,
            text = "     일부 서비스 이용에 제한이 있을 수 있습니다.",
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(height = 8.dp))
        
        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = TextColorGraye,
            text = "・ 설정 > 하우스잇에서 권한을 변경할 수 있습니다.",
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.weight(weight = 1f))

        PermitButton { onPermitBtnClick() }
    }
}

@Composable
fun PermissionGuideItem(
    icon: Int,
    content: String,
    description: String,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = content,
                tint = TextColorPurple
            )
            Spacer(modifier = Modifier.width(width = 8.dp))
            Column {
                GuideText(
                    modifier = Modifier,
                    color = TextColorPurple,
                    text = content,
                    fontWeight = FontWeight.W800,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )

                GuideText(
                    modifier = Modifier,
                    color = TextColorBlack,
                    text = description,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
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

@Composable
fun PermitButton(onPermitBtnClick: () -> Unit) {
    Button(
        onClick = { onPermitBtnClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = PermitBtnColor,
            contentColor = TextColorWhite
        )
    ) {
        GuideText(
            modifier = Modifier.fillMaxWidth(),
            color = TextColorWhite,
            text = "허용하기",
            fontWeight = FontWeight.W700,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionGuideScreenPreview() {
    PermissionGuideScreen(modifier = Modifier) { }
}