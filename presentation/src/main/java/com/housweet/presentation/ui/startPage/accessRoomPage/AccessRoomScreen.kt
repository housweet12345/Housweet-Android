package com.housweet.presentation.ui.startPage.accessRoomPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentaion.R
import com.housweet.presentation.ui.startPage.BackOnPressed
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Black_1A435671
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.dmsansFontFamily
import com.housweet.presentation.ui.theme.nanumSquareFontFamily

@Composable
fun AccessRoomScreen(
    modifier: Modifier,
    onChatScreen: () -> Unit,
    onAlarmScreen: () -> Unit,
    onMyPageScreen: () -> Unit,
    onCreateRoomScreen: () -> Unit,
    onSearchRoomScreen: () -> Unit,
    onFindRoomMateScreen: () -> Unit
) {
    BackOnPressed()
    AccessRoomContent(
        modifier = modifier,
        onChatBtnClick = onChatScreen,
        onAlarmBtnClick = onAlarmScreen,
        onMyPageBtnClick = onMyPageScreen,
        onCreateBtnClick = onCreateRoomScreen,
        onSearchBtnClick = onSearchRoomScreen,
        onFindRoomMateBtnClick = onFindRoomMateScreen
    )
}

@Composable
private fun AccessRoomContent(
    modifier: Modifier,
    onChatBtnClick: () -> Unit,
    onAlarmBtnClick: () -> Unit,
    onMyPageBtnClick: () -> Unit,
    onCreateBtnClick: () -> Unit,
    onSearchBtnClick: () -> Unit,
    onFindRoomMateBtnClick: () -> Unit
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(White)
    ) {
        Setting(
            onChatBtnClick = onChatBtnClick,
            onAlarmBtnClick = onAlarmBtnClick,
            onMyPageBtnClick = onMyPageBtnClick
        )

        Spacer(modifier = Modifier.height(height = 116.dp))

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Purple,
            text = "하우스메이트를 찾고",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            fontFamily = dmsansFontFamily,
            lineHeight = 35.sp,
            textAlign = TextAlign.Start
        )

        GuideText(
            modifier = Modifier.padding(start = 20.dp),
            color = Purple,
            text = "함께 방을 관리해요!",
            fontWeight = FontWeight.W700,
            fontSize = 22.sp,
            fontFamily = dmsansFontFamily,
            lineHeight = 35.sp,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(height = 80.dp))

        AccessRoomCard(
            text = "하우스 만들기",
        ) {
            onCreateBtnClick()
        }

        Spacer(modifier = Modifier.height(height = 10.dp))

        AccessRoomCard(
            text = "하우스 찾기",
        ) {
            onSearchBtnClick()
        }

        Spacer(modifier = Modifier.height(height = 10.dp))

        FindRoomMateBtn {
            onFindRoomMateBtnClick()
        }
    }
}

@Composable
private fun Setting(
    onChatBtnClick: () -> Unit,
    onAlarmBtnClick: () -> Unit,
    onMyPageBtnClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.chat),
            contentDescription = "chat",
            modifier = Modifier
                .size(24.dp)
                .padding(1.dp)
                .clip(CircleShape)
                .clickable {
                    onChatBtnClick()
                },
        )

        Icon(
            painter = painterResource(id = R.drawable.alarm),
            contentDescription = "alarm",
            modifier = Modifier
                .size(24.dp)
                .padding(1.dp)
                .clip(CircleShape)
                .clickable {
                    onAlarmBtnClick()
                }
        )

        Icon(
            painter = painterResource(id = R.drawable.mypage),
            contentDescription = "mypage",
            modifier = Modifier
                .size(24.dp)
                .padding(1.dp)
                .clip(CircleShape)
                .clickable {
                    onMyPageBtnClick()
                }
        )
    }
}

@Composable
private fun AccessRoomCard(
    text: String,
    onBtnClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 20.dp)
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
                .padding(end = 14.5.dp, bottom = 14.5.dp)
        ) {
            GuideText(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                color = Purple,
                text = text,
                fontWeight = FontWeight.W800,
                fontSize = 16.sp,
                fontFamily = nanumSquareFontFamily,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(45.dp)
                    .background(color = Purple, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable { onBtnClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = if (text == "하우스 만들기") R.drawable.plus else R.drawable.search),
                    contentDescription = "Add",
                    tint = White
                )
            }
        }
    }
}

@Composable
private fun FindRoomMateBtn(
    onBtnClick: () -> Unit
) {
    Button(
        onClick = { onBtnClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple,
            contentColor = White
        )
    ) {
        GuideText(
            color = White,
            text = "룸메 찾기",
            fontWeight = FontWeight.W700,
            fontSize = 15.sp,
            fontFamily = nanumSquareFontFamily,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AccessRoomPagePreview() {
    AccessRoomContent(
        modifier = Modifier,
        onChatBtnClick = {},
        onAlarmBtnClick = {},
        onMyPageBtnClick = {},
        onCreateBtnClick = {},
        onSearchBtnClick = {},
        onFindRoomMateBtnClick = {}
    )
}