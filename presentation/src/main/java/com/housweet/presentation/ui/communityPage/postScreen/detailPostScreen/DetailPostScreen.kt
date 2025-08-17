package com.housweet.presentation.ui.communityPage.postScreen.detailPostScreen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.domain.model.RoomPostDetailDataModel
import com.housweet.presentation.R
import com.housweet.presentation.ui.startPage.GuideText
import com.housweet.presentation.ui.startPage.LoadingScreen
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.White_F8F8F8
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.time.Duration

@Composable
fun DetailPostScreen(
    modifier: Modifier,
    onChatScreen: (userId: Int, nickName: String) -> Unit,
    onProfileScreen: (userId: Int) -> Unit,
    onBackBtnClick: (isBookmarkChanged: Boolean) -> Unit,
    detailPostViewModel: DetailPostViewModel = hiltViewModel()
) {
    val uiState by detailPostViewModel.uiState.collectAsStateWithLifecycle()
    val roomPostDetail by detailPostViewModel.roomPostDetail.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackBarHostState = remember { SnackbarHostState() }
    var isMenuExpanded by remember { mutableStateOf(false) }

    BackHandler {
        val isBookmarkChanged = detailPostViewModel.originalBookMarkState != roomPostDetail.isBookmarked
        onBackBtnClick(isBookmarkChanged)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            detailPostViewModel.event.collect { event ->
                when (event) {
                    DetailPostEvent.Error -> {
                        snackBarHostState.showSnackbar(
                            message = "방 정보를 제대로 불러오지 못했어요.",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }

                    DetailPostEvent.BookMarkError -> {
                        snackBarHostState.showSnackbar(
                            message = "북마크를 제대로 설정하지 못했어요.",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }

                    is DetailPostEvent.ReportRoom -> {
                        snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    when(uiState) {
        DetailPostState.Idle -> {
            DetailPostContent(
                modifier = modifier,
                roomPostDetail = roomPostDetail,
                snackBarHostState = snackBarHostState,
                isMenuExpanded = isMenuExpanded,
                onChatScreen = onChatScreen,
                onProfileScreen = onProfileScreen,
                onBackBtnClick = {
                    val isBookmarkChanged = detailPostViewModel.originalBookMarkState != roomPostDetail.isBookmarked
                    onBackBtnClick(isBookmarkChanged)
                },
                onMenuClick = {
                    isMenuExpanded = !isMenuExpanded
                },
                onScreenClick = {
                    isMenuExpanded = false
                },
                onReportClick = {
                    isMenuExpanded = false
                    detailPostViewModel.reportRoom()
                },
                onToggleLike = {
                    detailPostViewModel.toggleLike()
                }
            )
        }

        DetailPostState.IsLoading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun DetailPostContent(
    modifier: Modifier,
    roomPostDetail: RoomPostDetailDataModel,
    snackBarHostState: SnackbarHostState,
    isMenuExpanded: Boolean,
    onChatScreen: (userId: Int, nickName: String) -> Unit,
    onProfileScreen: (userId: Int) -> Unit,
    onBackBtnClick: () -> Unit,
    onMenuClick: () -> Unit,
    onScreenClick: () -> Unit,
    onReportClick: () -> Unit,
    onToggleLike: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    onScreenClick()
                }
            },
        topBar = {
            DetailPostTopBar(
                roomPostDetail = roomPostDetail,
                onBackBtnClick = onBackBtnClick,
                onMenuClick = onMenuClick
            )
        },
        bottomBar = {
            BottomBar(
                roomPostDetail = roomPostDetail,
                onChatScreen = onChatScreen
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(context)
                        .data(roomPostDetail.imageUri)
                        .error(R.drawable.post_image_null)
                        .build(),
                    contentDescription = "RoomImage",
                    contentScale = ContentScale.Crop,
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
                        roomPostDetail = roomPostDetail,
                        context = context,
                        onProfileScreen = onProfileScreen
                    )

                    DetailContent(
                        roomPostDetail = roomPostDetail
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    HouseFeatures(
                        roomPostDetail = roomPostDetail
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PreferredFeatures(
                        roomPostDetail = roomPostDetail
                    )

                    Spacer(modifier = Modifier.height(101.dp))
                }
            }

            Icon(
                painter = painterResource(id = if (roomPostDetail.isBookmarked) R.drawable.like else R.drawable.unlike),
                contentDescription = "favorite",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onToggleLike() },
                tint = if (roomPostDetail.isBookmarked) Purple else White
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isMenuExpanded) {
            MenuDropdownMenu(
                modifier = Modifier.align(Alignment.TopEnd),
                onReportClick = onReportClick
            )
        }
    }
}

@Composable
private fun MenuDropdownMenu(
    modifier: Modifier,
    onReportClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(top = 10.dp, end = 10.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = Gray_A5A5A5,
                ambientColor = Gray_CBCBCB
            ),
        shape = RoundedCornerShape(6.dp),
        color = White
    ) {
        Column {
            GuideText(
                modifier = Modifier
                    .padding(start = 16.dp, end = 90.dp, top = 12.dp, bottom = 12.dp)
                    .clickable { onReportClick() },
                color = Black,
                text = "게시글 신고하기",
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DetailPostTopBar(
    roomPostDetail: RoomPostDetailDataModel,
    onBackBtnClick: () -> Unit,
    onMenuClick: () -> Unit
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
            text = roomPostDetail.lotNumberAddress,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )

        Icon(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "menu",
            modifier = Modifier
                .padding(end = 20.dp)
                .clip(CircleShape)
                .clickable { onMenuClick() },
            tint = Black
        )
    }
}

@Composable
private fun UserProfile(
    roomPostDetail: RoomPostDetailDataModel,
    context: Context,
    onProfileScreen: (userId: Int) -> Unit
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
                .data(roomPostDetail.profileImageUrl)
                .error(R.drawable.default_profile_img)
                .build(),
            contentDescription = "RoomImage",
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable { onProfileScreen(roomPostDetail.userId) }
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            GuideText(
                color = Black,
                text = roomPostDetail.nickName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row {
                GuideText(
                    color = Black,
                    text = roomPostDetail.ageRangeAndGender,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                GuideText(
                    color = Gray_7E7E7E,
                    text = getRelativeTime(roomPostDetail.createdAtKst),
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
private fun DetailContent(
    roomPostDetail: RoomPostDetailDataModel
) {
    Spacer(modifier = Modifier.height(16.dp))
    GuideText(
        color = Black,
        text = roomPostDetail.title,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(10.dp))

    GuideText(
        color = Black,
        text = roomPostDetail.content,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start
    )

    Spacer(modifier = Modifier.height(10.dp))

    Box(
        modifier = Modifier
            .height(28.dp)
            .background(color = White_F8F8F8, shape = RoundedCornerShape(6.dp))
    ) {
        GuideText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            color = Black,
            text = "입주가능일 : ${roomPostDetail.availableFrom}",
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HouseFeatures(
    roomPostDetail: RoomPostDetailDataModel
) {
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
        featureList = roomPostDetail.trafficTags + roomPostDetail.sizeOfHouseTags + roomPostDetail.infraTags + roomPostDetail.personalityTags
    )
}

@Composable
private fun PreferredFeatures(
    roomPostDetail: RoomPostDetailDataModel
) {
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
        featureList = roomPostDetail.lifePatternTags + roomPostDetail.tidyingUpHabitTags
    )
}

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
    roomPostDetail: RoomPostDetailDataModel,
    onChatScreen: (userId: Int, nickName: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(White)
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
                    text = roomPostDetail.lotNumberAddress,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    textAlign = TextAlign.Start
                )

                GuideText(
                    modifier = Modifier.padding(top = 4.dp),
                    color = Black,
                    text = "보증금 ${roomPostDetail.deposit}만원 월세 ${roomPostDetail.rent}만원 관리비 ${roomPostDetail.managementFee}만원",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onChatScreen(roomPostDetail.userId, roomPostDetail.nickName) },
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

fun getRelativeTime(dateTimeString: String): String {
    if (dateTimeString.isEmpty()) return "오류"
    val pastInstant = Instant.parse(dateTimeString)
    val now = Instant.now()

    val hours = ChronoUnit.HOURS.between(pastInstant, now)
    val minutes = ChronoUnit.MINUTES.between(pastInstant, now)

    return when {
        hours >= 24 -> {
            val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
            pastInstant.atZone(ZoneId.systemDefault()).format(formatter)
        }
        hours >= 1 -> "${hours}시간 전"
        minutes >= 1 -> "${minutes}분 전"
        else -> "방금 전"
    }
}

@Preview
@Composable
private fun DetailPostScreenPreview() {
    var isMenuExpanded by remember { mutableStateOf(false) }
    DetailPostContent(
        modifier = Modifier,
        roomPostDetail = RoomPostDetailDataModel(),
        snackBarHostState = remember { SnackbarHostState() },
        onChatScreen = { _, _ -> },
        onProfileScreen = {},
        onBackBtnClick = {},
        isMenuExpanded = isMenuExpanded,
        onMenuClick = { isMenuExpanded = !isMenuExpanded },
        onScreenClick = { isMenuExpanded = false },
        onReportClick = {},
        onToggleLike = {}
    )
}
