package com.housweet.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.zIndex
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R
import com.housweet.presentation.ui.home.state.HomeInfo
import com.housweet.presentation.ui.home.state.MoodType
import com.housweet.presentation.ui.home.state.NoticeItem
import com.housweet.presentation.ui.home.state.RoommateInfo
import com.housweet.presentation.ui.home.state.TodoInfo
import com.housweet.presentation.ui.navigation.BottomNavigation
import com.housweet.presentation.ui.theme.ColorGroup
import com.housweet.presentation.ui.profile.component.ProfileImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeInfo: HomeInfo = HomeInfo(),
    onChatClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMyPageClick: () -> Unit = {},
    onNoticeClick: (Int) -> Unit = {},
    onTodoClick: () -> Unit = {},
    onMoodSectionClick: () -> Unit = {},
    onTodoToggle: (Int) -> Unit = {},
    onMoodSelect: (MoodType) -> Unit = {},
    navController: NavController = rememberNavController()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 스크롤 가능한 콘텐츠
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(start = 4.dp, end = 4.dp, bottom = 150.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
        // 상단 바 (오른쪽 3개 아이콘)
        item {
            TopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title = { },
                actions = {
                    Row {
                        IconButton(onClick = onChatClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_navigate_chat),
                                contentDescription = "채팅"
                            )
                        }
                    }
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_navigate_notification),
                            contentDescription = "알림"
                        )
                    }
                    IconButton(onClick = onMyPageClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_navigate_profile),
                            contentDescription = "프로필"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }

            // 방 제목 섹션
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    RoomTitleSection(homeInfo.roomName, homeInfo.daysTogether)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // 공지사항 섹션
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    NoticeSection(homeInfo.notices, onNoticeClick)
                }
            }

            // 룸메이트 기분 섹션
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    RoommatesMoodSection(homeInfo.members, onMoodSelect, onMoodSectionClick)
                }
            }

            // 내가 할 일 섹션
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    MyTodoSection(homeInfo.todos, onTodoClick, onTodoToggle)
                }
            }
        }
        
        // 바텀 네비게이션 (고정)
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigation(
                navController = navController
            )
        }
    }
}

@Composable
fun RoomTitleSection(
    roomName: String,
    daysLiving: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = roomName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "+${daysLiving}일째 함께 하는 중!",
            fontSize = 16.sp,
            color = ColorGroup.Primary
        )
    }
}

@Composable
fun NoticeSection(
    notices: List<NoticeItem>,
    onNoticeClick: (Int) -> Unit
) {
    if (notices.isNotEmpty()) {
        val latestNotice = notices.first()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = ColorGroup.Primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onNoticeClick(latestNotice.id) },
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
                    text = latestNotice.title,
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
}

@Composable
fun RoommatesMoodSection(
    roommates: List<RoommateInfo>,
    onMoodSelect: (MoodType) -> Unit,
    onMoodSectionClick: () -> Unit,
) {
    var showMoodCard by remember { mutableStateOf(false) }
    Column {
        Card(
            onClick = onMoodSectionClick,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
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
                    roommates.forEach { roommate ->
                        RoommateProfile(
                            roommate = roommate,
                            onMoodIconClick = { showMoodCard = !showMoodCard }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 기분 이모지들 (애니메이션과 함께 조건부 표시)
        AnimatedVisibility(
            visible = showMoodCard,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 300)
            ),
            modifier = Modifier.zIndex(-1f)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val moods = listOf(
                        MoodData(R.drawable.ic_happy, "행복"),
                        MoodData(R.drawable.ic_good, "무난"),
                        MoodData(R.drawable.ic_sad, "슬픔"),
                        MoodData(R.drawable.ic_angry, "화남"),
                        MoodData(R.drawable.ic_heart, "애정"),
                        MoodData(R.drawable.ic_congratulation, "축하"),
                        MoodData(R.drawable.ic_none, "외출")
                    )

                    moods.fastForEach {
                        MoodItem(mood = it, onMoodSelect = onMoodSelect)
                    }
                }
            }
        }
    }
}

@Composable
fun MyTodoSection(
    todos: List<TodoInfo>,
    onTodoClick: () -> Unit,
    onTodoToggle: (Int) -> Unit
) {

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
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onTodoClick() }
                )
            }

            // 할 일이 있을 때만 구분선과 할 일 목록 표시
            if (todos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                // 할 일 목록
                todos.forEach { item ->
                    TodoInfoRow(
                        todo = item,
                        onToggle = onTodoToggle
                    )

                    if (item != todos.last()) {
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
                        painter = painterResource(R.drawable.no_plan),
                        contentDescription = "할 일 없음",
                        tint = Color.Unspecified,
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
fun RoommateProfile(
    roommate: RoommateInfo,
    onMoodIconClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(70.dp)
        ) {
            // 프로필 사진 영역
            ProfileImage(
                imageUrl = roommate.profileImageUrl,
                size = 70
            )

            // 기분 아이콘 (오른쪽 아래 뱃지)
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd)
                    .clickable { onMoodIconClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = getMoodIconRes(roommate.mood)),
                    contentDescription = "기분",
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = roommate.nickname,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MoodItem(
    mood: MoodData,
    onMoodSelect: (MoodType) -> Unit
) {
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
                .clickable { 
                    val moodType = getMoodTypeFromName(mood.name)
                    moodType?.let { onMoodSelect(it) }
                },
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
fun TodoInfoRow(
    todo: TodoInfo,
    onToggle: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = todo.title,
            fontSize = 16.sp,
            color = if (todo.isCompleted) Color.Gray else Color.Black,
            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = { onToggle(todo.id) }
        ) {
            Icon(
                imageVector = if (todo.isCompleted) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = if (todo.isCompleted) "완료됨" else "미완료",
                tint = if (todo.isCompleted) Color(0xFF6C5CE7) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class MoodData(
    val iconRes: Int, // 이미지 리소스 ID
    val name: String
)

fun getMoodIconRes(moodType: MoodType): Int {
    return when (moodType) {
        MoodType.HAPPY -> R.drawable.ic_happy
        MoodType.NORMAL -> R.drawable.ic_good
        MoodType.SAD -> R.drawable.ic_sad
        MoodType.ANGRY -> R.drawable.ic_angry
        MoodType.LOVE -> R.drawable.ic_heart
        MoodType.CONGRAT -> R.drawable.ic_congratulation
        MoodType.OUTSIDE -> R.drawable.ic_none
    }
}

fun getMoodTypeFromName(name: String): MoodType? {
    return when (name) {
        "행복" -> MoodType.HAPPY
        "무난" -> MoodType.NORMAL
        "슬픔" -> MoodType.SAD
        "화남" -> MoodType.ANGRY
        "애정" -> MoodType.LOVE
        "축하" -> MoodType.CONGRAT
        "외출" -> MoodType.OUTSIDE
        else -> null
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}