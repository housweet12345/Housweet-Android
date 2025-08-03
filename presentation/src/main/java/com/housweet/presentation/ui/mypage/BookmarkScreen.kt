package com.housweet.presentation.ui.mypage

import BookmarkViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.housweet.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = viewModel(),
    onItemClick: (BookmarkItem) -> Unit,
    navController: NavController
) {
    val bookmarks by remember { mutableStateOf(viewModel.bookmarks) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TopAppBar
        CenterAlignedTopAppBar(
            title={
                androidx.compose.material.Text(
                    text = "북마크",
                    fontSize = 14.sp
                )
            },
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.back_black),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable { navController.popBackStack() }
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White // ✅ 배경색 흰색 지정
            )
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            bookmarks.forEach { item ->
                BookmarkCard(
                    item = item,
                    onClick = { onItemClick(item) },
                    onBookmarkToggle = {viewModel.toggleBookmark(item)}
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun BookmarkCard(
    item: BookmarkItem,
    onClick: () -> Unit,
    onBookmarkToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 썸네일 영역
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color(0xFFE0E0E0))
        )

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
                Text(text = item.location, fontSize = 10.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = item.ageGender, fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 북마크 아이콘
        Image(
            painter = painterResource(
                id = if (item.bookmarked) R.drawable.bookmark_active else R.drawable.bookmark_inactive
            ),
            contentDescription = "북마크",
            modifier = Modifier
                .size(20.dp)
                .clickable { onBookmarkToggle() },
            //삭제?
            contentScale = ContentScale.Fit
        )
    }
}