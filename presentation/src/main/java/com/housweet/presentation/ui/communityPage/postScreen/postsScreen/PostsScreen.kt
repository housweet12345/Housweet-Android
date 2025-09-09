package com.housweet.presentation.ui.communityPage.postScreen.postsScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.housweet.domain.model.community.RoomPostsByLocationDataModel
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.startPage.loginPage.loginScreen.Guide
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.White

@Composable
fun PostsScreen(
    updatePostId: Int?,
    blockedUserId: Int?,
    postsViewModel: PostsViewModel = hiltViewModel(),
    onPostClick: (postId: Int, lastRegion: String) -> Unit,
    onBackBtnClick: () -> Unit,
) {
    val uiState by postsViewModel.uiState.collectAsStateWithLifecycle()
    val posts by postsViewModel.posts.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackBarHostState = remember { SnackbarHostState() }

    BackHandler {
        onBackBtnClick()
    }

    LaunchedEffect(updatePostId) {
        postsViewModel.updatePostBookmark(updatePostId ?: return@LaunchedEffect)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            postsViewModel.event.collect { event ->
                when (event) {
                    PostsEvent.Error -> {
                        snackBarHostState.showSnackbar(
                            message = "방 정보를 제대로 불러오지 못했어요.",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }

                    PostsEvent.BookMarkError -> {
                        snackBarHostState.showSnackbar(
                            message = "북마크를 제대로 설정하지 못했어요.",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    when(uiState) {
        PostsState.Idle -> {
            PostsContent(
                postRegions = postsViewModel.postRegions,
                posts = posts,
                blockedUserId = blockedUserId,
                currentUserId = postsViewModel.currentUserId,
                snackBarHostState = snackBarHostState,
                onPostClick = onPostClick,
                onToggleLike = { postRegion, postIndex ->
                    postsViewModel.toggleLike(postRegion, postIndex)
                },
                onBackBtnClick = onBackBtnClick
            )
        }

        PostsState.IsLoading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun PostsContent(
    postRegions: List<String>,
    posts: Map<String, List<RoomPostsByLocationDataModel>>,
    blockedUserId: Int?,
    currentUserId: Int?,
    snackBarHostState: SnackbarHostState,
    onPostClick: (postId: Int, lastRegion: String) -> Unit,
    onToggleLike: (postRegion: String, postIndex: Int) -> Unit,
    onBackBtnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                text = when {
                    postRegions.size > 3 -> {
                        postRegions.take(3).joinToString(", ") { it.split(" ").last() } + "..."
                    }

                    postRegions.size in 1 .. 3 -> {
                        postRegions.joinToString(", ") { it.split(" ").last() }
                    }

                    else -> "하우스 없음"
                },
                onBackBtnClick = onBackBtnClick
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = White
    ) { innerPadding ->
        if (postRegions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(R.drawable.dead_house),
                        contentDescription = "deadHouse",
                        modifier = Modifier.padding(top = 140.dp)
                    )

                    GuideText(
                        modifier = Modifier.padding(top = 20.dp),
                        color = Black,
                        text = "현재 지역에 올라온 방이 없어요",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
            ) {
                posts.forEach { (postRegion, posts) ->
                    val regionParts = postRegion.split(" ")
                    items(
                        count = posts.size,
                        key = { posts[it].id }
                    ) { postIndex ->
                        val postInfo = posts[postIndex]
                        if (postInfo.isVisible && postInfo.userId != blockedUserId) {
                            PostItem(
                                postInfo = postInfo,
                                currentUserId = currentUserId,
                                postRegion = "${regionParts[1]} ${regionParts[2]}",
                                onPostClick = { onPostClick(postInfo.id, regionParts[2]) },
                                onToggleLike = { onToggleLike(postRegion, postIndex) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostItem(
    postInfo: RoomPostsByLocationDataModel,
    currentUserId: Int?,
    postRegion: String,
    onPostClick: () -> Unit,
    onToggleLike: () -> Unit
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
                .data(postInfo.imageUri)
                .error(R.drawable.post_image_null)
                .build(),
            contentDescription = "RoomImage",
            contentScale = ContentScale.Crop,
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
                    text = postInfo.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                if (currentUserId != postInfo.userId) {
                    Image(
                        painter = painterResource(id = if (postInfo.isBookmarked) R.drawable.like else R.drawable.unlike),
                        contentDescription = "favorite",
                        modifier = Modifier
                            .padding(start = 25.1.dp)
                            .clip(CircleShape)
                            .clickable { onToggleLike() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            GuideText(
                color = Black,
                text = "보증금 ${getRelativeMoney(postInfo.deposit)} 월세 ${getRelativeMoney(postInfo.rent)}",
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
                    text = postRegion,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )

                GuideText(
                    modifier = Modifier.padding(start = 8.dp),
                    color = Gray_A5A5A5,
                    text = postInfo.ageRangeAndGender,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.weight(1f))

                /* Icon(
                    painter = painterResource(id = R.drawable.like_count),
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
                ) */
            }
        }
    }
}

fun getRelativeMoney(money: Int): Int {
    return if (money >=10000) money / 10000
    else money
}

@Preview
@Composable
private fun PostsScreenPreview() {
    PostsContent(
        postRegions = listOf(),
        posts = mapOf(
            "서울특별시 강남구 역삼동" to listOf(
                RoomPostsByLocationDataModel(
                    id = 1,
                    userId = 2,
                    title = "방 구하는 분",
                    imageUri = "https://picsum.photos/200/300",
                    isBookmarked = false,
                    rent = 40000000,
                    deposit = 1000000,
                    ageRangeAndGender = "20대 남성",
                    isVisible = true
                )
            )
        ),
        blockedUserId = 0,
        currentUserId = 0,
        snackBarHostState = remember { SnackbarHostState() },
        onPostClick = { _, _ -> },
        onToggleLike = { _, _ -> },
        onBackBtnClick = {}
    )
}