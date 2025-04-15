package com.housweet.presentation.ui.component

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.theme.ColorGroup

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
        // 상단 앱바
        ProfileTopBar()
        // 프로필 정보
        ProfileInfo()
        Spacer(modifier = Modifier.height(16.dp))
        // 태그 섹션
        TagSection()
        Spacer(modifier = Modifier.height(24.dp))
        // 하단 버튼
        EditProfileButton(false)
    }
}

@Composable
fun ProfileTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { /* 뒤로가기 */ },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "뒤로가기"
            )
        }

        Text(
            text = "프로필",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = { /* 더보기 메뉴 */ },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "더보기"
            )
        }
    }
}

@Composable
fun ProfileInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // 프로필 이미지
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // 실제 이미지로 교체 필요
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // 상태 표시 버튼들
            Row(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(ColorGroup.Primary)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "20대",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(ColorGroup.Primary)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "남자",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 이름
        Text(
            text = "김지안",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ColorGroup.Black_1E1E1E,
        )
        Spacer(Modifier.height(10.dp))
        // 상태 메시지
        Text(
            text = "잘부탁드립니다.",
            fontSize = 14.sp,
            color = Color.Gray,
        )
    }
}

@Composable
fun TagSection() {
    Row(
        horizontalArrangement = Arrangement.Start
    ) {
        TagChip(text = "ENTP")
        Spacer(modifier = Modifier.width(8.dp))
        TagChip(text = "아침형")
        Spacer(modifier = Modifier.width(8.dp))
        TagChip(text = "대화를 좋아함")
    }
    Spacer(Modifier.height(6.dp))
    Row(
        horizontalArrangement = Arrangement.Start
    ) {
        TagChip(text = "비흡연자")
        Spacer(modifier = Modifier.width(8.dp))
        TagChip(text = "정리도 적당히")
    }
}

@Composable
fun TagChip(text: String) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .height(32.dp)
            .border(
                width = 1.dp,
                color = ColorGroup.Primary,
                shape = RoundedCornerShape(6.dp)
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = ColorGroup.Black_313131
            )
        }
    }
}

@Composable
fun EditProfileButton(
    isMyProfile: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (isMyProfile){
            Button(
                onClick = { /* 프로필 편집 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorGroup.White_F8F8F8
                )
            ) {
                Text(
                    text = "프로필 수정",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorGroup.Black_1E1E1E
                )
            }
        } else {
            Button(
                onClick = { /* 프로필 편집 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorGroup.Primary
                )
            ) {
                Text(
                    text = "채팅하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}

@Preview
@Composable
private fun ChatProfileButtonPreview() {
    EditProfileButton(true)
}

@Preview
@Composable
private fun EditProfileButtonPreview() {
    EditProfileButton(false)
}