package com.housweet.presentation.ui.chat

import android.R.attr.contentDescription
import android.R.attr.onClick
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.IconButton

@Composable
fun ChatScreen(chatName: String, navController: NavController) {
    val context = LocalContext.current
    val chatItems = remember { mutableStateListOf<ChatItem>() }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        chatItems.addAll(
            listOf(
                ChatItem.TextMessage("안녕하세요", false),
                ChatItem.TextMessage("안녕하세요~ 반갑습니다!", true),
                ChatItem.TextMessage("집 문의하고 싶어서 연락드렸어요. 지금도 메이트 구하시나요?", false),
                ChatItem.TextMessage("네! 아직 구하고 있어요 :)", true)
            )
        )
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            chatItems.add(ChatItem.ImageMessage(bitmap, true))
        }
    }

    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = chatName) },
                backgroundColor = Color.White,
                elevation = 0.dp,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                actions = {
                    IconButton(onClick = {expanded = true}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "메뉴"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false}
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                        }) {
                            Text("채팅방 삭제하기")
                        }
                        DropdownMenuItem(onClick = {
                            expanded = false
                        }) {
                            Text("차단하기")
                        }
                        DropdownMenuItem(onClick = {
                            expanded = false
                        }) {
                            Text("신고하기")
                        }
                    }
                }
            )
        },
        bottomBar = {
            ChatInput(
                inputText = inputText,
                onTextChange = { inputText = it },
                onSend = {
                    if (inputText.isNotBlank()) {
                        chatItems.add(ChatItem.TextMessage(inputText, true))
                        inputText = ""
                    }
                },
                onAddImageClick = {
                    imageLauncher.launch("image/*")
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9F9))
                    .padding(16.dp)
            ) {
                Text(
                    text = "연락처, 주소 등 민감한 개정보는 채팅을 통해 공유하지 마세요.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = "직접 만날 경우, 안전한 공공장소에서 만나시기 바랍니다.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "3월 8일",
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 12.sp,
                color = Color.Gray
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(chatItems) { item ->
                    when (item) {
                        is ChatItem.TextMessage -> {
                            ChatBubble(message = item.message, isMine = item.isMine)
                        }

                        is ChatItem.ImageMessage -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp),
                                horizontalArrangement = if (item.isMine) Arrangement.End else Arrangement.Start
                            ) {
                                Image(
                                    bitmap = item.bitmap.asImageBitmap(),
                                    contentDescription = "채팅 이미지",
                                    modifier = Modifier
                                        .size(width = 180.dp, height = 200.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
