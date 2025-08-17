package com.housweet.presentation.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.ColorGroup


@Composable
fun ProfileTopBar(
    title: String = "프로필",
    moreIconButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "뒤로가기"
            )
        }

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center)
        )
        if (moreIconButton){
            IconButton(
                onClick = onMoreClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "더보기"
                )
            }
        }
    }
}

@Composable
fun ProfileImage(
    imageUrl: String? = null,
    size: Int = 100,
    showCameraIcon: Boolean = false,
    onCameraClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        // 프로필 이미지 (원형으로 클립)
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
        
        // 카메라 아이콘 (오른쪽 아래)
        if (showCameraIcon) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    modifier = Modifier.size(25.dp),
                    onClick = onCameraClick,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "사진 변경",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileInfoSection(
    nickname: String,
    age: String,
    gender: String,
    introduction: String,
    imageUrl: String? = null,
) {
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
            ProfileImage(imageUrl = imageUrl, size = 40)

            // 상태 표시 버튼들
            Row(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Box(
                    modifier = Modifier
                        .height(19.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ColorGroup.Primary)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = age,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .height(19.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ColorGroup.Primary)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = gender,
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        // 이름
        Text(
            text = nickname,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ColorGroup.Black_1E1E1E,
        )
        Spacer(Modifier.height(10.dp))
        // 상태 메시지
        Text(
            text = introduction,
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Center,
        maxItemsInEachRow = Int.MAX_VALUE,
    ) {
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
        }
    }
}

@Composable
fun TagSection(
    tags: List<String>,
    mbti: String = "",
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Center,
        maxItemsInEachRow = Int.MAX_VALUE,
    ) {
        tags.forEachIndexed { index, tag ->
            TagChip(text = tag, isMbti = (mbti == tags[0] && index == 0))
        }
    }
}
@Composable
fun TagChip(
    text: String,
    isMbti: Boolean = false,
    modifier: Modifier = Modifier,
    isSelectable: Boolean = false,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val backgroundColor = if (isSelected || isMbti) ColorGroup.Primary else Color.White
    val textColor = if (isSelected || isMbti) ColorGroup.White_F8F8F8 else ColorGroup.Black_313131

    val clickModifier = if (isSelectable && onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .padding(vertical = 3.dp)
            .height(32.dp)
            .border(
                width = 0.5.dp,
                color = ColorGroup.Primary,
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
    isMyProfile: Boolean,
    editButtonOnClick: () -> Unit = {},
    chatButtonOnClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (isMyProfile){
            Button(
                onClick = editButtonOnClick,
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
                onClick = chatButtonOnClick,
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

@Preview(showBackground = true)
@Composable
private fun ProfileInfoSectionPreview() {
    ProfileInfoSection(
        nickname = "김아무개",
        gender = "남자",
        age = "20대",
        introduction = "안녕하세요, 잘부탁드립니다",
        imageUrl = null
    )
}

// 프리뷰
@Preview(showBackground = true)
@Composable
fun TagSectionPreview() {
    val sampleTags = listOf(
       "ISTP","음악 OK", "정화를 자주함", "비흡연자",
        "흡연자", "정돈 적당히", "술을 즐기는 편",
        "요리를 자주 함", "냉장고 음식 공유 가능"
    )

    TagSection(tags = sampleTags)
}