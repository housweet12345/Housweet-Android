package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.domain.model.Notice
import com.housweet.domain.repository.NoticeRepository
import com.housweet.presentation.R
import com.housweet.presentation.util.isoToDotDate
import com.housweet.presentation.viewmodel.mypage.NoticeDetailUiState
import com.housweet.presentation.viewmodel.mypage.NoticeDetailViewModel
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeDetailScreen(
    id: Int,
    onBackClick: () -> Unit,
    viewModel: NoticeDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(id) { viewModel.load(id) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title = {
                    Text(text = "공지사항", fontSize = 14.sp)
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { onBackClick() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        when (state) {
            is NoticeDetailUiState.Loading -> Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("불러오는 중...")
            }

            is NoticeDetailUiState.Error -> Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text((state as NoticeDetailUiState.Error).message, color = Color.Red)
            }

            is NoticeDetailUiState.Success -> {
                val notice = (state as NoticeDetailUiState.Success).notice
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Text(
                        text = notice.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = notice.content,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = notice.createdAt.isoToDotDate(),
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoticeDetailScreenPreview() {
    // 1) 가짜 레포
    val fakeRepo = remember { FakeNoticeRepository() }

    // 2) 프리뷰용 VM
    val fakeViewModel = remember { PreviewNoticeDetailViewModel(repo = fakeRepo) }

    NoticeDetailScreen(
        id = 1,
        onBackClick = {},
        viewModel = fakeViewModel
    )
}

class FakeNoticeRepository : NoticeRepository {
    override suspend fun getNotice(id: Int): Notice {
        return Notice(
            id = id,
            title = "미리보기 공지 제목",
            content = "미리보기 공지 내용입니다.\n여러 줄도 잘 보이는지 확인!",
            isRead = false,
            createdAt = "2025.06.05"
        )
    }
    override suspend fun getNotices(): List<Notice> {
        return listOf(
            Notice(
                id = 1,
                title = "미리보기 공지 제목",
                content = "미리보기 공지 내용입니다.\n여러 줄도 잘 보이는지 확인!",
                isRead = true,
                createdAt = "2025.06.05"
            )
        )
    }
}

class PreviewNoticeDetailViewModel(repo: NoticeRepository) : NoticeDetailViewModel(repo)