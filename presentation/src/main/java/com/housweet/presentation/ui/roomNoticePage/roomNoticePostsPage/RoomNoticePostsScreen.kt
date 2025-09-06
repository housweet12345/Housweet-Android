package com.housweet.presentation.ui.roomNoticePage.roomNoticePostsPage

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.navigation.BottomNavigation
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White

@Composable
fun NoticePostsScreen(
    modifier: Modifier,
    navController: NavController,
    onNoticeClick: () -> Unit,
    onWritePostClick: () -> Unit,
    onWriteRuleClick: () -> Unit,
    roomNoticePostsViewModel: RoomNoticePostsViewModel = hiltViewModel()
) {
    val uiState by roomNoticePostsViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    var roomOfRulesExpanded by remember { mutableStateOf(false) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            roomNoticePostsViewModel.event.collect { event ->
                when (event) {
                    RoomNoticePostsEvent.Error -> {
                        snackBarHostState.showSnackbar(
                            message = "공지와 규칙을 불러오는데 실패했습니다.",
                            actionLabel = "닫기",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    when (uiState) {
        RoomNoticePostsStates.Idle -> {
            NoticePostsContent(
                modifier = modifier,
                navController = navController,
                roomOfRulesExpanded = roomOfRulesExpanded,
                snackBarHostState = snackBarHostState,
                onExpandBtnClick = {
                    roomOfRulesExpanded = !roomOfRulesExpanded
                },
                onNoticeClick = onNoticeClick,
                onWritePostClick = onWritePostClick,
                onWriteRuleClick = onWriteRuleClick
            )
        }

        RoomNoticePostsStates.Loading -> {
            LoadingScreen()
        }
    }
}

@Composable
private fun NoticePostsContent(
    modifier: Modifier,
    navController: NavController,
    roomOfRulesExpanded: Boolean,
    snackBarHostState: SnackbarHostState,
    onExpandBtnClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onWritePostClick: () -> Unit,
    onWriteRuleClick: () -> Unit
) {
    var roomBarHeight by remember { mutableStateOf(48.0.dp) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigation(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
                .padding(innerPadding)
                .padding(top = 20.dp)
        ) {
            RoomOfRulesBar(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                roomOfRulesExpanded = roomOfRulesExpanded,
                onExpandBtnClick = onExpandBtnClick,
                onSizeChange = {
                    roomBarHeight = it
                },
                onWriteRuleClick = onWriteRuleClick
            )

            NoticePostItems(
                modifier = Modifier
                    .zIndex(-1f)
                    .padding(
                        top = roomBarHeight - 6.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                onNoticeClick = onNoticeClick
            )

            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                    .align(Alignment.BottomEnd)
                    .size(45.dp)
                    .background(color = Purple, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable { onWritePostClick() },
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
}

@Composable
private fun RoomOfRulesBar(
    modifier: Modifier,
    roomOfRulesExpanded: Boolean,
    onExpandBtnClick: () -> Unit,
    onSizeChange: (Dp) -> Unit,
    onWriteRuleClick: () -> Unit
) {
    val density = LocalDensity.current
    Box(
        modifier = modifier
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
            RoomOfRulesContent(
                onWriteRuleClick = onWriteRuleClick
            )
        }
    }
}

@Composable
private fun RoomOfRulesContent(
    onWriteRuleClick: () -> Unit
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
                        }.clickable { onWriteRuleClick() },
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
    modifier: Modifier,
    onNoticeClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier
    ) {
        val temp = listOf("", "", "", "", "", "", "", "", "", "", "")
        itemsIndexed(temp) { index, _ ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(22.dp))
            }

            NoticePostItem(
                onNoticeClick = onNoticeClick
            )

            Spacer(modifier = Modifier.height(
                if(index != temp.lastIndex) 10.dp else 22.dp)
            )
        }
    }
}

@Composable
private fun NoticePostItem(
    onNoticeClick: () -> Unit,
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = Gray_A5A5A5,
                ambientColor = Gray_CBCBCB
            ).clickable { onNoticeClick() },
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
    NoticePostsContent(
        modifier = Modifier,
        navController = NavController(LocalContext.current),
        roomOfRulesExpanded = false,
        snackBarHostState = remember { SnackbarHostState() },
        onExpandBtnClick = {},
        onNoticeClick = {},
        onWritePostClick = {},
        onWriteRuleClick = {}
    )
}

@Preview
@Composable
private fun NoticePostItemPreview() {
    NoticePostItem {}
}