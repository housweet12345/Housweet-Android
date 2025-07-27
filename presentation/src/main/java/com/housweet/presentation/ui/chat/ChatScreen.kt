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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.housweet.presentation.ui.chat.ChatBubble
import com.housweet.presentation.ui.chat.ChatInput
import com.housweet.presentation.ui.chat.ChatItem
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import com.housweet.presentation.R
import com.housweet.presentation.ui.chat.RequestGalleryPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatName: String, navController: NavController) {
    RequestGalleryPermission()
    val context = LocalContext.current
    val chatItems = remember { mutableStateListOf<ChatItem>() }
    val galleryImages = remember { mutableStateListOf<Uri>() }
    var inputText by remember { mutableStateOf("") }
    var showGallery by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    // 샘플 채팅 데이터
    LaunchedEffect(Unit) {
        chatItems.addAll(
            listOf(
                ChatItem.TextMessage("안녕하세요", false, R.drawable.default_profile),
                ChatItem.TextMessage("안녕하세요~ 반갑습니다!", true),
                ChatItem.TextMessage("집 문의하고 싶어서 연락드렸어요. 지금도 메이트 구하시나요?", false, R.drawable.default_profile),
                ChatItem.TextMessage("네! 아직 구하고 있어요 :)", true)
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = chatName,
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
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "메뉴")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(onClick = { expanded = false }) { Text("채팅방 삭제하기", fontSize = 12.sp) }
                        DropdownMenuItem(onClick = { expanded = false }) { Text("차단하기", fontSize = 12.sp) }
                        DropdownMenuItem(onClick = { expanded = false }) { Text("신고하기", fontSize = 12.sp) }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .background(Color.White)
        ) {
            // 채팅 리스트
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 안내 문구 (Composable 함수이므로 item 블록 안에 넣어야 함)
                item {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(top = 12.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = "notification",
                            modifier = Modifier
                                .size(16.dp),
//                            tint = Color(0xFF665ED3)
                        )
                        Text(
                            "연락처, 주소 등 민감한 개정보는 채팅을 통해 공유하지 마세요.",
                            fontSize = 10.sp,
                            color = Color(0xFF6F3DD2)
                        )
                        Text(
                            "직접 만날 경우, 안전한 공공장소에서 만나시기 바랍니다.",
                            fontSize = 10.sp,
                            color = Color(0xFF6F3DD2)
                        )
                    }
                }

                // 날짜
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "3월 8일",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                items(chatItems) { item ->
                    when (item) {
                        is ChatItem.TextMessage -> {
                            ChatBubble(
                                message = item.message,
                                isMine = item.isMine,
                                profileImage = item.profileImageRes?.let { painterResource(id = it) }
                            )
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
                                    modifier = Modifier.size(width = 180.dp, height = 200.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ✅ ChatInput은 항상 하단 고정 + 갤러리 뜨면 그 위로
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
                    showGallery = !showGallery
                    if (showGallery && galleryImages.isEmpty()) {
                        val images = GetGalleryImages(context)
                        Log.d("ChatScreen", "불러온 이미지 수: ${images.size}")
                        galleryImages.addAll(images)
                    }
                }
            )

            // ✅ 갤러리 미리보기 (ChatInput 아래)
            if (showGallery) {
                if (galleryImages.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // 세로 더 넓게
                            .background(Color.White), // 배경 흰색
                        contentAlignment = Alignment.Center // 텍스트 정중앙
                    ) {
                        Text(
                            text = "갤러리 사진이 없습니다.",
                            color = Color(0xFF6F3DD2)
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFFF0F0F0))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(galleryImages) { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(model = uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
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
                                contentScale = ContentScale.Crop // ✅ 꽉 채우기 + 자르기
                            )
                        }
                    }
                }
            }
        }
    }
}