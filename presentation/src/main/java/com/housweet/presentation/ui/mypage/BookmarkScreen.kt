package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.housweet.presentation.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.presentation.ui.common.LoadingScreen
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.navigation.NavigationManager
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.viewmodel.mypage.BookmarkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    navigationManager: NavigationManager,
    viewModel: BookmarkViewModel = hiltViewModel(),
    onItemClick: (BookmarkUiItem) -> Unit = {}
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) { viewModel.load() }

    BookmarkContent(
        bookmarks = bookmarks,
        isLoading = isLoading,
        onBack = { navigationManager.popBackStack() },
        onItemClick = onItemClick,
        onToggleBookmark = { viewModel.toggleBookmark(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkContent(
    bookmarks: List<BookmarkUiItem>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onItemClick: (BookmarkUiItem) -> Unit,
    onToggleBookmark: (BookmarkUiItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            text = "북마크",
            onBackBtnClick = onBack
        )

        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                bookmarks.forEach { item ->
                    Box(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        BookmarkCard(
                            item = item,
                            onClick = { onItemClick(item) },
                            onBookmarkToggle = { onToggleBookmark(item) }
                        )
                    }

                    Divider(
                        thickness = 0.5.dp,
                        color = Gray_CBCBCB
                    )
                }
            }
        }
    }
}

@Composable
fun BookmarkCard(
    item: BookmarkUiItem,
    onClick: () -> Unit,
    onBookmarkToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 썸네일 영역 (이미지 로더 붙일거면 Coil 등으로 대체)
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFE0E0E0))
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.thumbnailUrl)           // ✅ 서버에서 내려오는 profile_image
                    .crossfade(true)                   // 페이드 효과
                    .build(),
                contentDescription = "방 이미지",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop      // 이미지 꽉 차게
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontSize = 12.sp,
                color = Color.Black,
                maxLines = 2
            )
            Text(
                text = item.price,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(modifier = Modifier.padding(top = 2.dp)) {
                Text(text = item.areaText, fontSize = 10.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = item.ageGender, fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(
                id = if (item.bookmarked) R.drawable.bookmark_active else R.drawable.bookmark_inactive
            ),
            contentDescription = "북마크",
            modifier = Modifier
                .size(20.dp)
                .clickable { onBookmarkToggle() },
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookmarkScreenPreview() {
    val previewItems = listOf(
        BookmarkUiItem(1, "프리뷰용 북마크 1입니다", null, "보증금 100 / 월세 30", "강원특별자치도 춘천시", "20대 여성", true),
        BookmarkUiItem(2, "프리뷰용 북마크 2입니다", null, "보증금 200 / 월세 40", "강원특별자치도 춘천시", "30대 남성", true)
    )

    BookmarkContent(
        bookmarks = previewItems,
        isLoading = false,
        onBack = {},
        onItemClick = {},
        onToggleBookmark = {}
    )
}
