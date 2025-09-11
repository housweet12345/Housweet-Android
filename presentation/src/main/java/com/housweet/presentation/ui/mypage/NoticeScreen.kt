package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.util.isoToDotDate
import com.housweet.presentation.viewmodel.mypage.NoticeUiState
import com.housweet.presentation.viewmodel.mypage.NoticeViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NoticeScreen(
    onBackClick: () -> Unit = {},
    navController: NavController,
    viewModel: NoticeViewModel? = null // 기본 값
) {
    val isPreview = LocalInspectionMode.current

    // 프리뷰일때: 목데이터로 바로 그리기
    if (isPreview) {
        NoticeScaffold(
            navController = navController,
            state = NoticeUiState.Success(
                notices = listOf(
                    com.housweet.domain.model.Notice(
                        id = 1,
                        title = "프리뷰용 공지 제목",
                        content = "프리뷰 환경에서는 Hilt/네트워크를 우회합니다.",
                        isRead = false,
                        createdAt = "2025-08-03T06:23:17.201505Z"
                    ),
                    com.housweet.domain.model.Notice(
                        id = 2,
                        title = "두번째 공지",
                        content = "목데이터 2",
                        isRead = true,
                        createdAt = "2025-08-04T10:00:00Z"
                    )
                )
            )
        )
        return
    }

    // 런타임 시에만 hiltViewModel() 생성
    val vm = viewModel ?: hiltViewModel<NoticeViewModel>()
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    NoticeScaffold(
        navController = navController,
        state = state
    )
}

@Composable
fun NoticeItem(
    notice: NoticeUi,
    isLatest: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notice_inactive),
                contentDescription = "Notice",
                tint = if (isLatest) Color(0xFF665ED3) else Color(0xFFA5A5A5),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = notice.date.isoToDotDate(),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = notice.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Divider(
            color = Gray_CBCBCB,
            thickness = 0.5.dp,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoticeScaffold(
    navController: NavController,
    state: NoticeUiState
) {
    Scaffold(
        topBar = {
            TopBar(text = "공지사항", onBackBtnClick = { navController.popBackStack() })
        }
    ) { innerPadding ->
        when (state) {
            is NoticeUiState.Loading -> Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Text("불러오는 중...") }

            is NoticeUiState.Error -> Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Text("에러: ${state.message}", color = Color.Red) }

            is NoticeUiState.Success -> {
                val notices = state.notices
                if (notices.isEmpty()) {
                    Text(
                        "공지사항이 없습니다.",
                        modifier = Modifier.padding(innerPadding).padding(16.dp)
                    )
                } else {
                    // id 내림차순으로 정렬
                    val sortedNotices = notices.sortedByDescending { it.id }
                    // 가장 큰 id
                    val latestId = sortedNotices.firstOrNull()?.id

                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        items(sortedNotices, key = { it.id }) { notice ->
                            NoticeItem(
                                notice = NoticeUi(
                                    id = notice.id.toString(),
                                    date = notice.createdAt,
                                    title = notice.title,
                                    content = notice.content
                                ),
                                isLatest = notice.id == latestId,
                                onClick = { navController.navigate("notice_detail/${notice.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}


data class NoticeUi(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val title: String,
    val content: String
)

@Preview(showBackground = true)
@Composable
fun NoticeScreenPreview() {
    val navController = rememberNavController()
    NoticeScreen(
        navController = navController
    )
}