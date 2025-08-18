package com.housweet.presentation.ui.noticePage.noticePostsPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White

@Composable
fun NoticePostsScreen(
) {
    var roomOfRulesExpanded by remember { mutableStateOf(false) }
    NoticePostsContent(
        modifier = Modifier,
        roomOfRulesExpanded = roomOfRulesExpanded,
    ) {
        roomOfRulesExpanded = !roomOfRulesExpanded
    }
}

@Composable
private fun NoticePostsContent(
    modifier: Modifier,
    roomOfRulesExpanded: Boolean,
    onExpandBtnClick: () -> Unit
) {
    var roomBarHeight by remember { mutableStateOf(0.dp) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        RoomOfRulesBar(
            roomOfRulesExpanded = roomOfRulesExpanded,
            onExpandBtnClick = onExpandBtnClick,
            onSizeChange = {
                roomBarHeight = it
            }
        )

        NoticePostItems(
            modifier = Modifier
                .zIndex(-1f)
                .padding(top = if (roomBarHeight == 0.dp) 64.dp else roomBarHeight - 6.dp))

        Box(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomEnd)
                .size(45.dp)
                .background(color = Purple, shape = CircleShape)
                .clip(CircleShape)
                .clickable {  },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wrtie),
                contentDescription = "Add",
                tint = White
            )
        }
    }
}
@Composable
private fun RoomOfRulesBar(
    roomOfRulesExpanded: Boolean,
    onExpandBtnClick: () -> Unit,
    onSizeChange: (Dp) -> Unit
) {
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .onSizeChanged {
                val height = with(density) { it.height.toDp() }
                onSizeChange(height)
            }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            shape = RoundedCornerShape(6.dp),
            color = Purple
        ) {
            Row(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.megaphone),
                    contentDescription = "megaphone",
                    modifier = Modifier.padding(start = 16.dp)
                )

                GuideText(
                    modifier = Modifier.padding(start = 10.dp),
                    color = White,
                    text = "방의 규칙",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(R.drawable.arrowunder),
                    contentDescription = "arrowunder",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clip(shape = CircleShape)
                        .clickable {
                            onExpandBtnClick()
                        }
                )
            }
        }

        if (roomOfRulesExpanded) {
            RoomOfRulesContent()
        }
    }
}

@Composable
private fun RoomOfRulesContent(
) {
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp)
            .zIndex(0f),
        shape = RoundedCornerShape(6.dp),
        color = White,
        border = BorderStroke(width = 1.dp, color = Purple)
    ) {
        Column {
            GuideText(
                modifier = Modifier.padding(start = 16.dp, top = 20.dp),
                color = Gray_7E7E7E,
                text = "방의 규칙이 없습니다.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                GuideText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .drawBehind {
                            val strokeWidthPx = 0.5.dp.toPx()
                            val verticalOffset = size.height - 2.sp.toPx()
                            drawLine(
                                color = Gray_7E7E7E,
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        },
                    color = Gray_7E7E7E,
                    text = "수정하기",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun NoticePostItems(
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(listOf("", "", "", "", "", "", "", "", "", "", "")) { index, _ ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(22.dp))
            }

            NoticePostItem()

            if (index != 9) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun NoticePostItem() {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = Gray_A5A5A5,
                ambientColor = Gray_CBCBCB
            ),
        shape = RoundedCornerShape(6.dp),
        color = White,
        border = BorderStroke(width = 1.dp, color = Gray_CBCBCB)
    ) {
        Column {
            GuideText(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                color = Black,
                text = "오늘 집에 일찍 들어오기",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 18.sp,
                textAlign = TextAlign.Start
            )

            GuideText(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                color = Black,
                text = "저녁 같이 먹어야함\n필수필수",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                textAlign = TextAlign.Start
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                thickness = 1.dp,
                color = Gray_CBCBCB
            )

            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(context)
                        .data("https://picsum.photos/300/300")
                        .build(),
                    contentDescription = "profileImage",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                GuideText(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Black,
                    text = "홍길동",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                GuideText(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Gray_A5A5A5,
                    text = "2024.09.12",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoticePostsScreenPreview() {
    NoticePostsScreen()
}

@Preview
@Composable
private fun NoticePostItemPreview() {
    NoticePostItem()
}