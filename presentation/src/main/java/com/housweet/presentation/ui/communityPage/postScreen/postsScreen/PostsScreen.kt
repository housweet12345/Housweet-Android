package com.housweet.presentation.ui.communityPage.postScreen.postsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White

@Composable
fun PostsScreen(
    onPostClick: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    PostsContent(
        dong = "문정동",
        onPostClick = onPostClick,
        onBackBtnClick = onBackBtnClick
    )
}

@Composable
private fun PostsContent(
    dong: String,
    onPostClick: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            PostsTopBar(
                dong = dong,
                onBackBtnClick = onBackBtnClick
            )
        },
        containerColor = White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            items(10) {
                PostItem {
                    onPostClick()
                }
            }
        }
    }
}

@Composable
private fun PostsTopBar(
    dong: String,
    onBackBtnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .padding(start = 20.dp)
                .clip(CircleShape)
                .clickable { onBackBtnClick() },
            tint = Black
        )

        GuideText(
            color = Black,
            text = dong,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )

        Icon(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "menu",
            modifier = Modifier.padding(end = 20.dp),
            tint = Black
        )
    }
}

@Composable
private fun PostItem(
    onPostClick: () -> Unit
) {
    val context = LocalContext.current
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .height(104.dp)
        .clickable {
            onPostClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
         AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data("https://picsum.photos/300/300")
                .build(),
            contentDescription = "RoomImage",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Row {
                GuideText(
                    modifier = Modifier.width(190.dp),
                    color = Black,
                    text = "애완동물 좋아하는 사람을 구하고 있습니다.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.favorite),
                    contentDescription = "favorite",
                    modifier = Modifier.padding(start = 25.1.dp),
                    tint = Purple
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GuideText(
                color = Black,
                text = "보증금 400 월세 20",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                GuideText(
                    color = Black,
                    text = "송파구 문정동",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )

                GuideText(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Gray_A5A5A5,
                    text = "20대 남자",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.favorite_count),
                    contentDescription = "favoriteCount",
                    modifier = Modifier.padding(start = 25.1.dp),
                    tint = Gray_A5A5A5
                )

                GuideText(
                    modifier = Modifier.padding(start = 2.dp),
                    color = Gray_A5A5A5,
                    text = "1",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview
@Composable
private fun PostsScreenPreview() {
    PostsContent(
        dong = "문정동",
        onPostClick = {},
        onBackBtnClick = {}
    )
}