package com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.White_F8F8F8


@Composable
fun DetailPostScreen(
    modifier: Modifier
) {
    DetailPostContent(
        modifier = modifier
    )
}

@Composable
private fun DetailPostContent(
    modifier: Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                modifier = modifier
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(context)
                    .data("https://picsum.photos/300/300")
                    .build(),
                contentDescription = "RoomImage",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                UserProfile(
                    context = context
                )

                DetailContent()

                Spacer(modifier = Modifier.height(20.dp))

                HouseFeatures()

                Spacer(modifier = Modifier.height(16.dp))

                PreferredFeatures()

                Spacer(modifier = Modifier.height(101.dp))
            }
        }
    }
}

@Composable
private fun UserProfile(
    context: Context
) {
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data("https://picsum.photos/300/300")
                .build(),
            contentDescription = "RoomImage",
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            GuideText(
                color = Black,
                text = "김지안",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row {
                GuideText(
                    color = Black,
                    text = "20대 남자",
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                GuideText(
                    color = Gray_7E7E7E,
                    text = "1시간 전",
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun DetailContent() {
    Spacer(modifier = Modifier.height(16.dp))
    GuideText(
        color = Black,
        text = "애완동물 좋아하는 사람",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(10.dp))

    GuideText(
        color = Black,
        text = "집이 넓어 같이 살 사람을 구합니다.\n" +
                "밤에 시끄럽지 않은 사람이면 좋겠습니다.",
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .width(119.dp)
            .height(28.dp)
            .background(color = White_F8F8F8, shape = RoundedCornerShape(6.dp))
    ) {
        GuideText(
            modifier = Modifier.align(Alignment.Center),
            color = Black,
            text = "입주가능일 : 협의 가능",
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HouseFeatures() {
    GuideText(
        color = Black,
        text = "이 집의 특징",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(8.dp))

    FeaturesLayout(
        maxLines = 4,
        featureList = listOf("미흡연자", "저녁형", "조용한 환경 선호", "음악, 소음 OK", "전화를 자주함", "흡연자", "술은 적당히", "미흡연자", "요리를 자주 함", "음식은 사먹는 편")
    )
}

@Composable
private fun PreferredFeatures() {
    GuideText(
        color = Black,
        text = "선호하는 사람",
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(8.dp))

    FeaturesLayout(
        maxLines = 3,
        featureList = listOf("아침형", "청소를 자주하는 편", "정리는 적당히", "공용 공간 정리 철저", "빨래를 자주 돌림", "설거지를 자주함")
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FeaturesLayout(
    maxLines: Int,
    featureList: List<String>
) {
    FlowRow (
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        maxLines = maxLines
    ) {
        featureList.forEach { feature ->
            FeatureBox(
                feature = feature
            )
        }
    }
}

@Composable
private fun FeatureBox(
    feature: String
) {
    Box(
        modifier = Modifier
            .height(26.dp)
            .border(shape = RoundedCornerShape(6.dp), width = 0.5.dp, color = Purple)
            .clip(RoundedCornerShape(6.dp))
    ) {
        GuideText(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = Black,
            text = feature,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            thickness = 0.5.dp,
            color = Gray_CBCBCB
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                GuideText(
                    color = Black,
                    text = "문정동",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    textAlign = TextAlign.Start
                )

                GuideText(
                    modifier = Modifier.padding(top = 4.dp),
                    color = Black,
                    text = "보증금 200만 월세 40만",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {},
                modifier = Modifier
                    .width(68.dp)
                    .height(30.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                GuideText(
                    color = White,
                    text = "채팅하기",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview
@Composable
private fun DetailPostScreenPreview() {
    DetailPostContent(
        modifier = Modifier
    )
}

@Preview
@Composable
private fun PeatureBoxPreview() {
    FeatureBox(
        feature = "아침형"
    )
}
