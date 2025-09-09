package com.housweet.presentation.ui.mypage

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.CustomMenu
import com.housweet.presentation.ui.common.MenuItem
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.mypage.state.MyHouseUiState
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.viewmodel.mypage.MyHouseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHouseDetailScreen(
    navController: NavController,
    isHost: Boolean, // ✅ 방장 여부
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    inviteCode: String
) {
    var expanded by remember { mutableStateOf(false) }

    val viewModel: MyHouseViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    // 성공 상태일 때만 메뉴 노출
    val showMenu = state is MyHouseUiState.Success

    val menuItems = buildList {
        if (isHost) {
            add(MenuItem(text = "하우스 수정") {
                navController.navigate("edit_my_house")
                expanded = false
            })
        } else {
            add(MenuItem(text = "하우스 나가기") {
                // 나가기 로직 여기에 작성 (예: 다이얼로그 띄우기 등)
                expanded = false
            })
        }
    }

    // 메뉴 숨길 때 드롭다운도 닫아두기(상태 전환 시 안전)
    LaunchedEffect(showMenu) { if (!showMenu) expanded = false }

    // 최초 로드
    LaunchedEffect(Unit) { viewModel.load() }

    // 편집에서 돌아오면 화면이 RESUME 되므로 그때 새로고침
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = androidx.lifecycle.LifecycleEventObserver { _, e ->
            if (e == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            if (showMenu) {
                TopBar(
                    text = "마이하우스",
                    onBackBtnClick = { navController.popBackStack() }
                ) { modifier ->
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "menu",
                        modifier = modifier
                            .padding(end = 20.dp)
                            .clip(CircleShape)
                            .clickable { expanded = !expanded },
                        tint = Black
                    )
                }
            }
        }
    ) { paddingValues ->
        when (val s = state) {
            is MyHouseUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material.CircularProgressIndicator()
                }
            }

            is MyHouseUiState.Empty -> {  // ✅ 추가
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.dead_house),
                            contentDescription = null,
                            modifier = Modifier.size(140.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "마이하우스가 없습니다.\n마이하우스를 생성해주세요.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF222222),
                            textAlign = TextAlign.Center,         // ← 줄 내부 가운데 정렬
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            is MyHouseUiState.Error -> {
                val msg = s.message
                Box(
                    Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) { Text("에러: $msg") }
            }

            is MyHouseUiState.Success -> {
                val data = s.data
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().zIndex(1f)
                    ) {
                        if (expanded) {
                            CustomMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                menuItems = menuItems,
                                modifier = Modifier.align(Alignment.TopEnd)

                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                            .background(Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(60.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_house_smile),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(Modifier.height(32.dp))
                        Text(text = data.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "+01일째 함께 하는 중!",
                            color = Color(0xFF6C5CE7),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (isHost) {
                            Spacer(Modifier.height(32.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                tonalElevation = 0.dp,
                                shadowElevation = 0.dp,
                                color = Color.White
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 10.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "초대 코드",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = data.inviteCode,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}