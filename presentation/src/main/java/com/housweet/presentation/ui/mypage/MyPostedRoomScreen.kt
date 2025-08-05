package com.housweet.presentation.ui.mypage

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.housweet.presentation.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.domain.model.RoomPost
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.navigation.Route
import com.housweet.presentation.viewmodel.roomposting.RoomPostingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostedRoomScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) } // 0: 게시중, 1: 숨김
    val tabTitles = listOf("게시중", "숨김")

    // 모달 상태
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<RoomPost?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: RoomPostingViewModel = hiltViewModel() // Hilt 또는 ViewModel에서 주입
    val roomPosts = viewModel.roomPosts

    // 게시글 목록 필터링
    val filteredPosts by remember {
        derivedStateOf {
            if (selectedTab == 0) {
                roomPosts.filter { !it.isHidden }
            } else {
                roomPosts.filter { it.isHidden }
            }
        }
    }

    // 화면이 처음 렌더링될 때, 데이터 불러오기
    LaunchedEffect(Unit) {
        Log.d("MyPostedRoomScreen", "내가 올린 방 목록 로딩")
        viewModel.loadMyRooms()
    }

    suspend fun hidePost(post: RoomPost) {
        viewModel.updatePostVisibility(post.id, false)
        delay(100) // 💡 상태 반영 시간 확보
        selectedTab = 1
        snackbarHostState.showSnackbar("숨김 처리되었습니다.")
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "올린 방 관리",
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
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).background(Color.White)) {
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
                                navController.navigate(Route.HouseRegisterRoute.Step1(mode = RegisterModel.EDIT))
                            } else {
                                coroutineScope.launch {
                                    viewModel.updatePostVisibility(room.id, true)
                                    delay(100) // 💡 살짝 기다렸다가
                                    selectedTab = 0
                                }
                            }
                        },
                        onUnhideClick = {
                            coroutineScope.launch {
                                viewModel.updatePostVisibility(room.id, true)
                                delay(100) // 💡 마찬가지로
                                selectedTab = 0
                            }
                        }
                    )
                }
            }
        }

        // 🛠️ Modal Bottom Sheet
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFFF8F8F8)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    if (selectedPost?.isHidden == false) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        selectedPost?.let {
                                            hidePost(it)
                                            selectedTab = 1 // "숨김" 탭으로 전환
                                        }
                                        showSheet = false
                                        snackbarHostState.showSnackbar("숨김 처리되었습니다.")
                                    }
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("게시글 숨기기", fontSize = 14.sp)
                        }
                    }
                    else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Route.HouseRegisterRoute.Step1(mode = RegisterModel.EDIT))
                                    showSheet = false
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("게시글 수정", fontSize = 14.sp)
                        }
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
                                            viewModel.loadMyRooms() // 🔄 삭제 후 목록 갱신
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
                        Text(
                            text = "게시글 삭제",
                            fontSize = 14.sp,
                            color = Color.Red
                        )
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
                    .size(70.dp)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(roomPost.title, fontSize = 12.sp)
                Text(roomPost.priceInfo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(roomPost.metaInfo, fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // 높이를 명확히 줘야 가운데 정렬이 잘 적용돼요
            contentAlignment = Alignment.Center
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.Center)
            ){
                if (!roomPost.isHidden) {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier.width(250.dp).height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F7F7)),
                        shape = RoundedCornerShape(6.dp)
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
                        .width(60.dp)
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