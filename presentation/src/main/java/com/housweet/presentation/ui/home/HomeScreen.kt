package com.housweet.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.ColorGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 바 (오른쪽 3개 아이콘)
        TopAppBar(
            title = { },
            actions = {
                Row {
                    IconButton(onClick = { /* 채틷 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_navigate_chat),
                            contentDescription = "채팅"
                        )
                    }
                    IconButton(onClick = { /* 알림 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_navigate_notification),
                            contentDescription = "알림"
                        )
                    }
                    IconButton(onClick = { /* 프로필 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_navigate_profile),
                            contentDescription = "프로필"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // 스크롤 가능한 콘텐츠
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 방 제목 섹션
            item {
                RoomTitleSection()
            }

            // 공지사항 섹션
            item {
                NoticeSection()
            }

            // 룸메이트 기분 섹션
            item {
                RoommatesMoodSection()
            }

            // 내가 할 일 섹션
            item {
                MyTodoSection()
            }
        }
    }
}

@Composable
fun RoomTitleSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "곰돌이방",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "+01일째 함께 하는 중!",
            fontSize = 16.sp,
            color = ColorGroup.Primary
        )
    }
}

@Composable
fun NoticeSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = ColorGroup.Primary, // Primary 색상 테두리
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { /* 공지사항 상세보기 */ },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_notice),
                contentDescription = "공지",
                tint = ColorGroup.Primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "23일 집에 일찍 돌아오기",
                fontSize = 14.sp,
                color = ColorGroup.Primary,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "더보기",
                tint = ColorGroup.Primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun RoommatesMoodSection() {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "오늘 룸메이트의 기분이에요!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "더보기",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 룸메이트 프로필들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    RoommateProfile(name = "김지안", moodIconRes = R.drawable.ic_normal)
                    RoommateProfile(name = "김지안", moodIconRes = R.drawable.ic_normal)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // 기분 이모지들
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val moods = listOf(
                    MoodData(R.drawable.ic_happy, "행복"),
                    MoodData(R.drawable.ic_normal, "무난"),
                    MoodData(R.drawable.ic_sad, "슬픔"),
                    MoodData(R.drawable.ic_angry, "화남"),
                    MoodData(R.drawable.ic_love, "애정"),
                    MoodData(R.drawable.ic_congrat, "축하"),
                    MoodData(R.drawable.ic_outside, "외출")
                )

                moods.fastForEach {
                    MoodItem(mood = it)
                }
            }
        }
    }
}

@Composable
fun MyTodoSection() {
    // 할 일 상태 관리
    var todoItems by remember {
        mutableStateOf(
            listOf<TodoItem>(
//                TodoItem("청소기 돌리기", false),
//                TodoItem("빨래하기", true)
            )
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(3.dp))
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "체크",
                        tint = ColorGroup.Primary,
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = "내가 할 일",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "더보기",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // 할 일이 있을 때만 구분선과 할 일 목록 표시
            if (todoItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                // 할 일 목록
                todoItems.forEach { item ->
                    TodoItemRow(
                        item = item,
                        onCheckedChange = { isChecked ->
                            todoItems = todoItems.map {
                                if (it.text == item.text) it.copy(isCompleted = isChecked)
                                else it
                            }
                        }
                    )

                    if (item != todoItems.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                // 할 일이 없을 때
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_empty_todo),
                        contentDescription = "할 일 없음",
                        tint = Color.Gray,
                        modifier = Modifier.size(width = 135.dp, height = 103.dp)
                    )

                    Text(
                        text = "오늘은 할일이 없어요",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RoommateProfile(name: String, moodIconRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(70.dp)
        ) {
            // 프로필 사진 영역 (배경)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFFEAA8A8), // 연한 분홍색 (프로필 사진 자리)
                        shape = CircleShape
                    )
            ) {
                // 여기에 실제 프로필 이미지가 들어갈 예정
                // Image(painter = painterResource(profileImageRes), contentDescription = ...)
            }

            // 기분 아이콘 (오른쪽 아래 뱃지)
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = moodIconRes),
                    contentDescription = "기분",
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MoodItem(mood: MoodData) {
    Column(
        modifier = Modifier.padding(vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                )
                .clickable { /* 기분 선택 */ },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = mood.iconRes),
                contentDescription = mood.name,
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = mood.name,
            fontSize = 10.sp,
            color = Color.Black
        )
    }
}

@Composable
fun LocationButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF6C5CE7) else Color(0xFFF8F9FA),
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 0.dp else 0.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun TodoItemRow(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.text,
            fontSize = 16.sp,
            color = if (item.isCompleted) Color.Gray else Color.Black,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = { onCheckedChange(!item.isCompleted) }
        ) {
            Icon(
                imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = if (item.isCompleted) "완료됨" else "미완료",
                tint = if (item.isCompleted) Color(0xFF6C5CE7) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


data class TodoItem(
    val text: String,
    val isCompleted: Boolean
)

data class MoodData(
    val iconRes: Int, // 이미지 리소스 ID
    val name: String
)

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}