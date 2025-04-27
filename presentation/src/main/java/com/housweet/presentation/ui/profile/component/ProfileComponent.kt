package com.housweet.presentation.ui.profile.component

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.housweet.presentaion.R
import com.housweet.presentation.ui.theme.ColorGroup


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
fun ProfileImage(
    imageUrl: String? = null,
    size: Int = 120,
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrEmpty()) {
            // 기본 프로필 이미지 표시
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .border(1.dp, ColorGroup.Gray_CBCBCB, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery), // 프로필 기본 아이콘
                    contentDescription = "기본 프로필",
                    modifier = Modifier.size((size / 2).dp),
                    tint = ColorGroup.Gray_CBCBCB
                )
            }
        } else {
            // Coil을 사용하여 서버에서 이미지 로드
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .border(1.dp, ColorGroup.Gray_CBCBCB, CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    //로딩 처리 필요시 코드 추가
                },
                error = {
                    // 이미지 로드 실패 시 기본 이미지 표시
                    Box(
                        modifier = Modifier
                            .size(size.dp)
                            .clip(CircleShape)
                            .border(1.dp, ColorGroup.Gray_CBCBCB, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "이미지 로드 실패",
                            modifier = Modifier.size((size / 2).dp),
                            tint = ColorGroup.Gray_CBCBCB
                        )
                    }
                }
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
            ProfileImage(size = 40)

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiSelectableTagSection(
    modifier: Modifier = Modifier,
    tags: List<String>,
    selectedTags: Set<String> = emptySet(),
    onSelectionChanged: (Set<String>) -> Unit = {},
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Center,
        maxItemsInEachRow = Int.MAX_VALUE,
        content = {
            tags.forEach { tag ->
                SelectableTagChip(
                    text = tag,
                    isSelected = selectedTags.contains(tag),
                    onClick = {
                        val updatedSelection = if (selectedTags.contains(tag)) {
                            selectedTags.minus(tag)
                        } else {
                            selectedTags.plus(tag)
                        }
                        onSelectionChanged(updatedSelection)
                    }
                )
                Spacer(modifier = Modifier.padding(end = 8.dp))
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSection(
    tags: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Center,
        maxItemsInEachRow = Int.MAX_VALUE,
        content = {
            tags.forEach { tag ->
                TagChip(text = tag)
                Spacer(modifier = Modifier.padding(end = 8.dp))
            }
        }
    )
}
@Composable
fun TagChip(
    text: String,
    modifier: Modifier = Modifier,
    isSelectable: Boolean = false,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = if (isSelected) ColorGroup.Primary.copy(alpha = 0.1f) else Color.White,
    borderColor: Color = ColorGroup.Primary,
    textColor: Color = if (isSelected) ColorGroup.Primary else ColorGroup.Black_313131
) {
    val clickModifier = if (isSelectable && onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Surface(
        color = backgroundColor,
        modifier = modifier
            .padding(vertical = 3.dp)
            .height(32.dp)
            .border(
                width = 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .then(clickModifier),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = textColor
            )
        }
    }
}

// 선택 가능한 태그 컴포넌트를 위한 편의 함수
@Composable
fun SelectableTagChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TagChip(
        text = text,
        modifier = modifier,
        isSelectable = true,
        isSelected = isSelected,
        onClick = onClick
    )
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

// 프리뷰
@Preview(showBackground = true)
@Composable
fun TagSectionPreview() {
    val sampleTags = listOf(
        "미술연자", "지적형", "초음파 활용 선호",
        "음악, 수음 OK", "정화를 자주함", "비흡연자",
        "음연자", "정돈 적당히", "술을 즐기는 편",
        "요리를 자주 함", "음식은 서먹한 편",
        "냉장고 음식 공유 가능"
    )

    TagSection(tags = sampleTags)
}