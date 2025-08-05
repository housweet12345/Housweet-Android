package com.housweet.presentation.ui.chat

import GetGalleryImages
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R
import com.housweet.presentation.viewmodel.chatlist.ChatListViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatName: String,
    navController: NavController,
    receiverId: Int
) {
    RequestGalleryPermission()
    val context = LocalContext.current
    val viewModel = hiltViewModel<ChatListViewModel>()
    val chatItems = remember { mutableStateListOf<ChatItem>() }
    val galleryImages = remember { mutableStateListOf<Uri>() }
    var showGallery by remember { mutableStateOf(false) }

    val senderId = 1
    val listState = rememberLazyListState()

    // 채팅 메시지 polling
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.fetchChatMessages(senderId, receiverId) { messages ->
                chatItems.clear()
                chatItems.addAll(processChatMessagesWithDate(messages, senderId))
            }
            delay(3000)
        }
    }

    // 최신 메시지로 스크롤
    LaunchedEffect(chatItems.size) {
        if (chatItems.isNotEmpty()) {
            listState.animateScrollToItem(chatItems.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(chatName, fontSize = 14.sp) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Column {
                ChatInput(
                    senderId = senderId,
                    receiverId = receiverId,
                    onSendMessage = { sId, rId, message ->
                        if (message.isNotBlank()) {
                            chatItems.add(ChatItem.TextMessage(message, true))
                            viewModel.sendChatMessage(sId, rId, message) { success ->
                                Log.d("Chat", if (success) "전송 성공" else "전송 실패")
                            }
                        }
                    },
                    onAddImageClick = {
                        showGallery = !showGallery
                        if (showGallery && galleryImages.isEmpty()) {
                            val images = GetGalleryImages(context)
                            galleryImages.addAll(images)
                        }
                    }
                )

                if (showGallery) {
                    // 갤러리 미리보기
                    if (galleryImages.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(216.dp)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("갤러리 사진이 없습니다.", color = Color(0xFF6F3DD2))
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(216.dp)
                                .background(Color(0xFFF0F0F0))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(galleryImages) { uri ->
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(108.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                                                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                                            } else {
                                                val source = ImageDecoder.createSource(context.contentResolver, uri)
                                                ImageDecoder.decodeBitmap(source)
                                            }
                                            chatItems.add(ChatItem.ImageMessage(bitmap, true))
                                            showGallery = false
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        ChatScreenContent(
            chatItems = chatItems,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            listState = listState,
            navController = navController,
            chatName = "채팅 미리보기"
        )
    }
}


@Composable
fun ChatScreenContent(
    chatItems: List<ChatItem>,
    modifier: Modifier = Modifier,
    listState: LazyListState,
    navController: NavController,
    chatName: String
) {
    LazyColumn(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState
    ) {
        items(chatItems) { item ->
            when (item) {
                is ChatItem.TextMessage -> {
                    ChatBubble(
                        message = item.message,
                        isMine = item.isMine,
                        profileImage = item.profileImageRes?.let { painterResource(id = it) }
                    )
                }
                is ChatItem.DateHeader -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(item.date, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                else -> Unit
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val navController = rememberNavController()
    val chatItems = listOf(
        ChatItem.DateHeader("8월 6일"),
        ChatItem.TextMessage("안녕하세요!", isMine = false, profileImageRes = R.drawable.default_profile),
        ChatItem.TextMessage("안녕하세요~ 반가워요!", isMine = true)
    )

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("채팅 미리보기", fontSize = 14.sp) },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            ChatInput(
                senderId = 1,
                receiverId = 2,
                onSendMessage = { _, _, _ -> },
                onAddImageClick = {}
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        ChatScreenContent(
            chatName = "채팅 미리보기",
            chatItems = chatItems,
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            listState = listState
        )
    }
}