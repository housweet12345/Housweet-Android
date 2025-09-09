package com.housweet.presentation.ui.mypage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.domain.model.RoomPost
import com.housweet.domain.repository.RoomPostingRepository
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.navigation.Route
import com.housweet.presentation.viewmodel.roomposting.RoomPostingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostedRoomScreen(
    navController: NavController,
    viewModel: RoomPostingViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: 게시중, 1: 숨김
    val tabTitles = listOf("게시중", "숨김")

    // 모달 상태
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<RoomPost?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val roomPosts = viewModel.roomPosts
    val isLoading = viewModel.isLoading

    // 게시글 목록 필터링
    val filteredPosts by remember(selectedTab, roomPosts) {
        derivedStateOf {
            if (selectedTab == 0) roomPosts.filter { !it.isHidden }
            else roomPosts.filter { it.isHidden }
        }
    }

    val inPreview = isPreview()
    LaunchedEffect(Unit) {
        if (!inPreview) {
            Log.d("MyPostedRoomScreen", "내가 올린 방 목록 로딩")
            viewModel.loadMyRooms()
        }
    }

    // ViewModel 이벤트 -> 스낵바 + 재시도
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RoomPostingViewModel.UiEvent.Error -> {
                    val res = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "재시도"
                    )
                    if (res == SnackbarResult.ActionPerformed) {
                        viewModel.loadMyRooms()
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                text = "내가 올린 방 관리",
                onBackBtnClick = { navController.popBackStack() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
                .fillMaxSize()
        ) {
            // 로딩 인디케이터 (상단 얇은 바)
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // 탭
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontSize = 12.sp) }
                    )
                }
            }

            // 목록
            if (filteredPosts.isEmpty() && !isLoading) {
                // 빈 상태
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("표시할 게시글이 없습니다.", color = Color.Gray, fontSize = 12.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredPosts) { room ->
                        RoomItem(
                            roomPost = room,
                            onMenuClick = {
                                selectedPost = room
                                showSheet = true
                            },
                            onEditClick = {
                                if (!room.isHidden) {
                                    navController.navigate(
                                        Route.HouseRegisterRoute.Step1(
                                            mode = RegisterModel.EDIT,
                                            postingId = room.id
                                        )
                                    )
                                } else {
                                    // 숨김 상태에서 "글 수정하기" → 우선 해제 후 이동
                                    coroutineScope.launch {
                                        viewModel.setHiddenLocally(room.id, false)
                                        val prevTab = selectedTab
                                        selectedTab = 0
                                        val ok = viewModel.updatePostVisibilityRemote(room.id, true)
                                        if (!ok) {
                                            viewModel.setHiddenLocally(room.id, true)
                                            selectedTab = prevTab
                                            snackbarHostState.showSnackbar("숨김 해제 실패. 다시 시도해주세요.")
                                        } else {
                                            snackbarHostState.showSnackbar("숨김 해제되었습니다.")
                                        }
                                    }
                                }
                            },
                            onUnhideClick = {
                                coroutineScope.launch {
                                    viewModel.setHiddenLocally(room.id, false)
                                    val prevTab = selectedTab
                                    selectedTab = 0
                                    val ok = viewModel.updatePostVisibilityRemote(room.id, true)
                                    if (!ok) {
                                        viewModel.setHiddenLocally(room.id, true)
                                        selectedTab = prevTab
                                        snackbarHostState.showSnackbar("숨김 해제 실패. 다시 시도해주세요.")
                                    } else {
                                        snackbarHostState.showSnackbar("숨김 해제되었습니다.")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        // Modal Bottom Sheet
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    if (selectedPost?.isHidden == false) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        val id = selectedPost?.id ?: return@launch
                                        // 즉시 숨김 + 탭 이동
                                        viewModel.setHiddenLocally(id, true)
                                        var ok = false
                                        runCatching {
                                            ok = viewModel.updatePostVisibilityRemote(id, false)
                                        }
                                        if (ok) {
                                            snackbarHostState.showSnackbar("숨김 처리되었습니다.")
                                        } else {
                                            viewModel.setHiddenLocally(id, false)
                                            snackbarHostState.showSnackbar("숨김 처리 실패. 다시 시도해주세요.")
                                        }
                                        showSheet = false
                                    }
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) { Text("게시글 숨기기", fontSize = 14.sp, modifier = Modifier.padding(vertical = 10.dp)) }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val id = selectedPost?.id ?: return@clickable
                                    navController.navigate(
                                        Route.HouseRegisterRoute.Step1(
                                            mode = RegisterModel.EDIT,
                                            postingId = id
                                        )
                                    )
                                    showSheet = false
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) { Text("게시글 수정", fontSize = 14.sp, modifier = Modifier.padding(vertical = 10.dp)) }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    selectedPost?.let {
                                        val success = viewModel.deletePost(it.id)
                                        if (success) {
                                            snackbarHostState.showSnackbar("게시글이 삭제되었습니다.")
                                            viewModel.loadMyRooms()
                                        } else {
                                            snackbarHostState.showSnackbar("삭제에 실패했습니다.")
                                        }
                                    }
                                    showSheet = false
                                }
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("게시글 삭제", fontSize = 14.sp, color = Color.Red, modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(
    roomPost: RoomPost,
    onMenuClick: () -> Unit,
    onEditClick: () -> Unit,
    onUnhideClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(roomPost.imageUri)           // ✅ 서버에서 내려오는 image_uri
                        .crossfade(true)                   // 페이드 효과
                        .build(),
                    contentDescription = "방 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop      // 이미지 꽉 차게
                )
            }
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(roomPost.title, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically){
                    Text("보증금 ${roomPost.deposit}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(6.dp))
                    Text("월세 ${roomPost.rent}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(roomPost.areaText.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Text(roomPost.ageRangeAndGender, fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                if (!roomPost.isHidden) {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier
                            .weight(4.5f)
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F7F7)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("글 수정하기", color = Color.Black, fontSize = 12.sp)
                    }
                } else {
                    Button(
                        onClick = onUnhideClick,
                        modifier = Modifier.width(250.dp).height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F7F7)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("숨김 해제", color = Color.Black, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(6.dp)) // ✅ radius 설정
                        .background(Color(0xFFF7F7F7)),  // ✅ 배경색
                    contentAlignment = Alignment.Center) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier.fillMaxSize(),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent // ✅ 배경 투명으로
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "More",
                            modifier = Modifier.size(12.dp) // ← 아이콘 크기 조절
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPostedRoomScreenPreview() {
    val dummyPosts = listOf(
        RoomPost(
            id = 1,
            userId = 3,
            title = "가까운 역세권 쉐어하우스",
            imageUri = "",
            rent = 30,
            deposit = 100,
            ageRangeAndGender = "20대 여성",
            isVisible = true,
            areaText = "서울시 강남구",
            isHidden = false
        ),
        RoomPost(
            id = 2,
            userId = 3,
            title = "조용한 동네 쉐어하우스",
            imageUri = "",
            rent = 40,
            deposit = 200,
            ageRangeAndGender = "30대 남성",
            isVisible = true,
            areaText = "서울시 송파구",
            isHidden = false
        )
    )

    val previewViewModel = object : RoomPostingViewModel(
        repository = object : RoomPostingRepository {
            override suspend fun updatePostVisibility(postingId: Int, isVisible: Boolean): Boolean { return true }
            override suspend fun getMyRoomPostings(): List<RoomPost> = dummyPosts
            override suspend fun deletePost(postingId: Int): Boolean { return true }
        }
    ) {
        init {
            roomPosts.addAll(dummyPosts)
        }
    }

    MyPostedRoomScreen(
        navController = rememberNavController(),
        viewModel = previewViewModel
    )
}

@Composable
fun isPreview(): Boolean {
    return LocalInspectionMode.current
}