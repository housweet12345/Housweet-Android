package com.housweet.presentation.ui.userlist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.ColorGroup
import com.housweet.presentation.ui.userlist.state.UserItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    userItems: List<UserItem>,
    onBackClick: () -> Unit = {},
    navigateToProfile: (String) -> Unit = {},
    onWorkspaceInvite: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 앱바
        CenterAlignedTopAppBar(
            windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            ),
            title = {
                Text(
                    text = "유저 목록",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            )
        )

        // 사용자 목록과 버튼
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(userItems.size) { index ->
                val user = userItems[index]
                UserListItem(
                    userItem = user,
                    onClick = { navigateToProfile(user.id) }
                )
                if (index < userItems.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        thickness = 1.dp,
                        color = Color(0xFFEEEEEE)
                    )
                }
            }

            item {
                // 하단 버튼
                Button(
                    onClick = onWorkspaceInvite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorGroup.Primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "하우스메이트 찾기",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun UserListItem(
    userItem: UserItem,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO 프로필 이미지
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFB3BA)),
            contentAlignment = Alignment.Center
        ) {
            // 실제 이미지가 있다면 AsyncImage 사용
            // AsyncImage(...)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 사용자 이름과 왕관 아이콘
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = userItem.name,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            if (userItem.isHost) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_host_marker),
                    contentDescription = "방장",
                    tint = ColorGroup.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 화살표 아이콘
        Icon(
            painter = painterResource(R.drawable.ic_next),
            contentDescription = "상세보기",
            tint = ColorGroup.Gray_878787,
            modifier = Modifier.size(20.dp)
        )
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun UserListScreenPreview() {
    val mockUserItems = listOf(
        UserItem(
            id = "1",
            name = "홍길동",
            profileImageUrl = null,
            isHost = false
        ),
        UserItem(
            id = "2",
            name = "홍길동",
            profileImageUrl = null,
            isHost = true // 방장
        )
    )

    UserListScreen(
        userItems = mockUserItems,
        onBackClick = {},
        navigateToProfile = {},
        onWorkspaceInvite = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun UserListItemPreview() {
    val mockUserItem = UserItem(
        id = "1",
        name = "홍길동",
        profileImageUrl = null,
        isHost = false
    )

    UserListItem(
        userItem = mockUserItem,
        onClick = {}
    )
}