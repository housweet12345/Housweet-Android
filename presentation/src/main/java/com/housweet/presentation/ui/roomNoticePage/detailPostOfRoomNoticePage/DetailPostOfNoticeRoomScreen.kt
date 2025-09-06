package com.housweet.presentation.ui.roomNoticePage.detailPostOfRoomNoticePage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.Gray_D9D9D9
import com.housweet.presentation.ui.theme.Purple
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.White_F8F8F8
import com.housweet.presentation.ui.theme.nanumSquareFontFamily
import kotlinx.coroutines.launch

data class TempComment(
    val comment: String,
    val replies: SnapshotStateList<String>
)

@Composable
fun DetailPostOfNoticeScreen(
    onBackBtnClick: () -> Unit
) {
    var commentTextValue by remember { mutableStateOf(TextFieldValue("")) }
    var commentList by remember { mutableStateOf(listOf(TempComment("확인", mutableStateListOf("ㅁㅁㅁ")))) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedCommentIndex by remember { mutableIntStateOf(-1) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    DetailPostOfNoticeContent(
        commentTextValue = commentTextValue,
        commentList = commentList,
        isMenuExpanded = isMenuExpanded,
        selectedCommentIndex = selectedCommentIndex,
        listState = listState,
        onCommentTextChanged = {
            commentTextValue = it
        },
        onAddNewComment = {
            if (commentTextValue.text.isEmpty()) return@DetailPostOfNoticeContent

            val targetIndex = if (selectedCommentIndex == -1) {
                commentList = commentList + TempComment(commentTextValue.text, mutableStateListOf())
                commentList.lastIndex + 4
            } else {
                commentList[selectedCommentIndex].replies.add(commentTextValue.text)
                selectedCommentIndex + 4
            }

            commentTextValue = TextFieldValue("")

            coroutineScope.launch {
                listState.animateScrollToItem(targetIndex)
            }
        },
        onMenuClick = {
            isMenuExpanded = !isMenuExpanded
        },
        onScreenClick = {
            isMenuExpanded = false
        },
        onReplyBtnClick = {
            if (selectedCommentIndex == -1) {
                selectedCommentIndex = it
                return@DetailPostOfNoticeContent
            }

            selectedCommentIndex = if (selectedCommentIndex != it) it else -1
        },
        onBackBtnClick = onBackBtnClick
    )
}

@Composable
private fun DetailPostOfNoticeContent(
    commentTextValue: TextFieldValue,
    commentList: List<TempComment>,
    isMenuExpanded: Boolean,
    selectedCommentIndex: Int,
    listState: LazyListState,
    onCommentTextChanged: (TextFieldValue) -> Unit,
    onAddNewComment: () -> Unit,
    onMenuClick: () -> Unit,
    onScreenClick: () -> Unit,
    onReplyBtnClick: (index: Int) -> Unit,
    onBackBtnClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    onScreenClick()
                }
            },
        containerColor = White,
        topBar = {
            DetailPostOfNoticeTopBar(
                onMenuClick = onMenuClick,
                onBackBtnClick = onBackBtnClick
            )
        }
    ) { innerPadding ->
        PostOfNoticeContent(
            modifier = Modifier.padding(innerPadding),
            commentList = commentList,
            selectedCommentIndex = selectedCommentIndex,
            commentTextValue = commentTextValue,
            listState = listState,
            onCommentTextChanged = {
                onCommentTextChanged(it)
            },
            onAddNewComment = {
                onAddNewComment()
            },
            onReplyBtnClick = {
                onReplyBtnClick(it)
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isMenuExpanded) {
            MenuDropdownMenu(
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun DetailPostOfNoticeTopBar(
    onMenuClick: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(start = 20.dp, bottom = 17.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "back",
            modifier = Modifier
                .padding(top = 12.dp)
                .clip(CircleShape)
                .clickable { onBackBtnClick() },
            tint = Black,
        )

        GuideText(
            modifier = Modifier.padding(top = 12.dp),
            color = Black,
            text = "게시글",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            textAlign = TextAlign.Center
        )

        Icon(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = "menu",
            tint = Black,
            modifier = Modifier
                .padding(top = 12.dp, end = 20.dp)
                .clip(CircleShape)
                .clickable { onMenuClick() }
        )
    }
}

@Composable
private fun MenuDropdownMenu(
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .padding(top = 10.dp, end = 10.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = Gray_A5A5A5,
                ambientColor = Gray_CBCBCB
            ),
        shape = RoundedCornerShape(6.dp),
        color = White
    ) {
        Column {
            GuideText(
                modifier = Modifier.padding(start = 16.dp, end = 90.dp, top = 12.dp, bottom = 12.dp),
                color = Black,
                text = "수정하기",
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center
            )

            GuideText(
                modifier = Modifier.padding(start = 16.dp, end = 90.dp, top = 12.dp, bottom = 12.dp),
                color = Black,
                text = "삭제하기",
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PostOfNoticeContent(
    modifier: Modifier,
    commentList: List<TempComment>,
    selectedCommentIndex: Int,
    commentTextValue: TextFieldValue,
    listState: LazyListState,
    onCommentTextChanged: (TextFieldValue) -> Unit,
    onAddNewComment: () -> Unit,
    onReplyBtnClick: (index: Int) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .consumeWindowInsets(WindowInsets.navigationBars)
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState
        ) {
            // 게시글 제목
            item {
                GuideText(
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                    color = Black,
                    text = "오늘 집에 일찍 들어오기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Start
                )
            }

            // 게시글 내용
            item {
                GuideText(
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp),
                    color = Black,
                    text = "저녁 같이 먹어야함\n필수필수",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )
            }

            // 작성자 정보
            item {
                Row(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 28.dp,
                        bottom = 16.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(context)
                            .data("https://picsum.photos/300/300")
                            .build(),
                        contentDescription = "profileImage",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )

                    Column {
                        GuideText(
                            modifier = Modifier.padding(start = 8.dp),
                            color = Black,
                            text = "홍길동",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 12.sp,
                            textAlign = TextAlign.Start
                        )

                        GuideText(
                            modifier = Modifier.padding(start = 8.dp),
                            color = Gray_A5A5A5,
                            text = "2024.09.12",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            // 구분선
            item {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Gray_CBCBCB
                )
            }

            // 댓글 목록
            itemsIndexed(items = commentList) { index, comment ->
                CommentItemWithReplies(
                    index = index,
                    comment = comment,
                    selectedCommentIndex = selectedCommentIndex,
                ) {
                    onReplyBtnClick(it)
                }
            }
        }

        DetailPostOfNoticeBottom(
            modifier = Modifier.fillMaxWidth(),
            commentTextValue = commentTextValue,
            onCommentTextChanged = {
                onCommentTextChanged(it)
            },
            onAddNewComment = {
                onAddNewComment()
            }
        )
    }
}

@Composable
private fun DetailPostOfNoticeBottom(
    modifier: Modifier,
    commentTextValue: TextFieldValue,
    onCommentTextChanged: (TextFieldValue) -> Unit,
    onAddNewComment: () -> Unit
) {
    CommentTextField(
        modifier = modifier,
        textValue = commentTextValue,
        onCommentTextChanged = {
            onCommentTextChanged(it)
        },
        onAddNewComment = {
            onAddNewComment()
        }
    )
}

@Composable
private fun CommentTextField(
    modifier: Modifier,
    textValue: TextFieldValue,
    onCommentTextChanged: (TextFieldValue) -> Unit,
    onAddNewComment: () -> Unit
) {
    Column(
        modifier = modifier.background(color = White)
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Gray_CBCBCB
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = textValue,
                onValueChange = {
                    onCommentTextChanged(it)
                },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = Black,
                    fontFamily = nanumSquareFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    ),
                    letterSpacing = 0.sp,
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .background(color = White_F8F8F8, shape = RoundedCornerShape(17.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        if (textValue.text.isEmpty()) {
                            GuideText(
                                color = Gray_7E7E7E,
                                text = "댓글을 입력해주세요.",
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Start
                            )
                        }

                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(22.dp))

            Surface (
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onAddNewComment() },
                shape = RoundedCornerShape(6.dp),
                color = Purple
            ) {
                GuideText(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = White_F8F8F8,
                    text = "등록",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CommentItemWithReplies(
    index: Int,
    comment: TempComment,
    selectedCommentIndex: Int,
    onReplyBtnClick: (index: Int) -> Unit
) {
    CommentItem(
        comment = comment,
        isSelectedForReply = selectedCommentIndex == index
    ) {
        onReplyBtnClick(index)
    }

    comment.replies.forEach {
        ReplyItem(it)
    }

    if (index != 2) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            thickness = 0.5.dp,
            color = Gray_CBCBCB
        )
    }
}

@Composable
private fun CommentItem(
    comment: TempComment,
    isSelectedForReply: Boolean,
    onReplyBtnClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(start = 20.dp, top = 12.dp, bottom = 12.dp)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data("https://picsum.photos/300/300")
                .build(),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            GuideText(
                color = Black,
                text = "홍길동",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 12.sp,
                textAlign = TextAlign.Start
            )

            GuideText(
                modifier = Modifier.padding(top = 4.dp),
                color = Black,
                text = comment.comment,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 12.sp,
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GuideText(
                    color = Gray_A5A5A5,
                    text = "2024.09.12",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 8.sp,
                    textAlign = TextAlign.Start
                )

                Icon(
                    painter = painterResource(id = R.drawable.rectangle),
                    contentDescription = "reply",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            onReplyBtnClick()
                        },
                    tint = if (isSelectedForReply) Purple else Gray_D9D9D9
                )

                GuideText(
                    modifier = Modifier.padding(start = 4.dp, top = 1.5.dp, bottom = 1.5.dp),
                    color = if (isSelectedForReply) Purple else Gray_7E7E7E,
                    text = "대댓글",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 8.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun ReplyItem(
    reply: String
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White_F8F8F8)
            .padding(start = 40.dp, top = 12.dp, bottom = 12.dp)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context)
                .data("https://picsum.photos/300/300")
                .build(),
            contentDescription = "profileImage",
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            GuideText(
                color = Black,
                text = "홍길동",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 12.sp,
                textAlign = TextAlign.Start
            )

            GuideText(
                modifier = Modifier.padding(top = 4.dp),
                color = Black,
                text = reply,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 12.sp,
                textAlign = TextAlign.Start
            )

            GuideText(
                modifier = Modifier.padding(top = 8.dp),
                color = Gray_A5A5A5,
                text = "2024.09.12",
                fontSize = 8.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 8.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailPostOfNoticeScreenPreview() {
    DetailPostOfNoticeContent(
        commentTextValue = TextFieldValue(""),
        commentList = listOf(),
        isMenuExpanded = false,
        selectedCommentIndex = -1,
        listState = rememberLazyListState(),
        onCommentTextChanged = {},
        onAddNewComment = {},
        onMenuClick = {},
        onScreenClick = {},
        onReplyBtnClick = {},
        onBackBtnClick = {}
    )
}

@Preview
@Composable
private fun MenuDropdownMenuPreview() {
    MenuDropdownMenu(
        modifier = Modifier
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun CommentItemPreview() {
    CommentItem(comment = TempComment("", mutableStateListOf("asdasdasd")), isSelectedForReply = true) {}
}

@Preview
@Composable
private fun ReplyItemPreview() {
    ReplyItem(
        reply = "asdasdasd"
    )
}
