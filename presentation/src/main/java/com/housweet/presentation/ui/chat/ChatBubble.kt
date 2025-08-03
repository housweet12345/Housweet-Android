package com.housweet.presentation.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.housweet.presentation.R

@Composable
fun ChatBubble(
    message: String,
    isMine: Boolean,
    profileImage: Painter? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMine) {
            if (profileImage != null) {
                // 상대방이 프로필 이미지를 가지고 있을 때
                Image(
                    painter = profileImage,
                    contentDescription = "상대방 프로필",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                )
            } else {
                // 상대방 프로필 이미지가 없을 때
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF2F2F2))
                )
            }
        }

        Box(
            modifier = Modifier
                .background(
                    color = if (isMine) Color(0xFF6F3DD2) else Color(0xFFF2F2F2),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message,
                color = if (isMine) Color.White else Color.Black,
                fontSize = 12.sp
            )
        }
        if (isMine) {
            Spacer(modifier = Modifier.width(38.dp)) // 내 메시지일 땐 균형 맞추기
        }
    }
}