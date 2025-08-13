package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.text.style.TextDecoration

private data class Faq(val q: String, val a: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    val categories = listOf("하우스메이트 찾기", "마이 하우스", "기타")

    val faqData: List<List<Faq>> = listOf(
        listOf(
            Faq("집 올리는 방법이 궁금해요.",
                "하우스 찾기 화면의 좌상단 ‘하우스 올리기’ 버튼을 이용해 게시할 수 있어요."),
            Faq("마이하우스를 생성/참여 했더니 하우스 찾는 화면이 안보여요.",
                "마이하우스를 생성/참여하신 분께서는 현재 함께 살고있는 룸메이트가 있다고 가정하고 하우스메이트 찾는 화면 사용률이 낮을 것으로 예상하여 메인에 보이지 않도록 설정하였습니다.\n" +
                        "\n" +
                        "마이하우스에 있으면서 하우스메이트 찾는 화면을 보고싶으실 경우,\n" +
                        "\n" +
                        "마이하우스 > 유저목록 (룸메이트 기분에서 우측 화살표 클릭) > 하우스메이트 찾기\n" +
                        "\n" +
                        "위 경로로 접속부탁드립니다."),
            Faq("방이 있는 사람이 아닌, 방을 구하는 사람은 게시글 작성이 안될까요?",
                "하우스잇은 부동산 탐색처럼 룸메이트를 구하는 방식으로 초기 기획하여 현재는 방을 구하는 게시글 작성은 지양하여 주시길 바랍니다.")
        ),
        listOf(
            Faq("다른 사용자 초대는 어떻게 할 수 있나요?",
                "마이페이지 > 마이하우스\n" +
                        "\n" +
                        "위 경로로 들어가시면 초대 코드를 확인하실 수 있습니다!\n" +
                        "\n" +
                        "다른 사용자가 초대 코드를 받고 “하우스 찾기”에서 입력하면 입장이 가능합니다!"),
            Faq("마이하우스는 동시에 2개 생성이 안되나요?",
                "마이하우스는 한 계정당 하나의 방만 등록될 수 있습니다.\n" +
                        "\n" +
                        "때문에 초대를 원하는 다른 사용자가 방에 들어올려면 기존 마이하우스를 나가거나 생성한 마이하우스가 없어야 합니다.\n" +
                        "\n" +
                        "마이하우스 나가기 및 삭제는 마이페이지 > 마이하우스 페이지의 상단 버튼을 누르시면 하우스 수정 및 나가기가 가능합니다."),
            Faq("현재 공사중으로 나와있는 페이지는 언제 사용가능한가요?",
                "마이하우스는 한 계정당 하나의 방만 등록될 수 있습니다.\n" +
                        "\n" +
                        "팀 하우스잇에서 열심히 개발중에 있습니다. 최대한 빠른 시일 내로 제공할 수 있도록 노력하겠습니다!"),
            Faq("‘룸메이트 기분’ 에서 자신의 기분 설정을 어떻게 하나요?",
                "프로필 이미지 하단 이모지를 눌러주시면 다양한 이모지 선택이 가능합니다!"),
        ),
        listOf(
            Faq("채팅하면서 오디오 및 영상이 전송이 안돼요. 오류인가요?",
                "현재 채팅에서 오디오 및 영상 전송 기능을 제공하지 않습니다. 양해부탁드립니다."),
            Faq("프로필 성별을 잘못 선택했습니다. 다시 선택할려면 어떻게 해야하나요?",
                "무분별한 성별 변경을 방지하기 위하여 정정이 필요할 경우 고객센터(snuoai@gmail.com)로 변경 요청 부탁드립니다."),
            Faq("프로필 설정에 원하는 키워드가 없어요. 키워드가 추가되면 좋겠어요.",
                "원하시는 키워드를 말씀주시면 의견을 반영하여 최대한 반영될 수 있도록 하겠습니다!\n" +
                        "\n" +
                        "좋은 의견은 고객센터(snuoai@gmail.com)으로 말씀부탁드립니다."),
            Faq("광고 및 제휴를 제안하고 싶어요.",
                "관련 내용과 함께 고객센터(snuoai@gmail.com)으로 연락주시면 빠른 시일 내로 안내 드리겠습니다!")
        )
    )

    var selectedIndex by remember { mutableStateOf(0) }
    val faqList = faqData[selectedIndex]

    val expandedStates = remember(selectedIndex) {
        mutableStateListOf(*Array(faqList.size) { false })
    }

    var showContactDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title={
                    androidx.compose.material.Text(
                        text = "도움말",
                        fontSize = 14.sp
                    )
                },
                navigationIcon = {
                    androidx.compose.material.Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // ✅ 배경색 흰색 지정
                )
            )
        }
    ) { paddingValues ->
        val listState = rememberLazyListState()
        if (showContactDialog) {
            ContactDialog(onDismiss = { showContactDialog = false })
        }
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

        ) {
            item{
                // 카테고리 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEachIndexed { index, label ->
                        val isSelected = selectedIndex == index
                        val stroke = if (isSelected) Color(0xFF665ED3) else Color(0xFFE0E0E0)
                        val bg = Color.White
                        val text = if (isSelected) Color(0xFF665ED3) else Color(0xFF666666)

                        OutlinedButton(
                            onClick = { selectedIndex = index },
                            modifier = Modifier
                                .defaultMinSize(minHeight = 38.dp) // 높이만 보장, 폭은 내용만큼
                                .padding(vertical = 2.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, stroke),
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = bg
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(label, fontSize = 14.sp, color = text)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // FAQ 리스트
                faqList.forEachIndexed { index, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedStates[index] = !expandedStates[index] }
                            .padding(vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.q,
                                modifier = Modifier
                                    .weight(1f)              // 남은 영역만큼만 차지
                                    .padding(end = 8.dp),     // 아이콘과 간격
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 15.sp
                            )
                            Icon(
                                painter = if (expandedStates[index]) painterResource(id = R.drawable.arrow_up)
                                else painterResource(id = R.drawable.arrow_down),
                                modifier = Modifier.padding(end = 8.dp),
                                contentDescription = null,
                                tint = Color(0xFF888888)
                            )
                        }

                        if (expandedStates[index]) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.a,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF757575),
                                fontSize = 13.sp
                            )
                        }
                    }
                    Divider()
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 문의 안내 박스
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFF5B51FE),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { showContactDialog = true }
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "그 외 궁금한 점이 있으신가요?",
                            color = Color(0xFF665ED3),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "1:1 문의로 해결하세요!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF665ED3),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ContactDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "필요하신 문의는",
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "snuoai@gmail.com",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFF222222),
                    modifier = Modifier.clickable {
                        clipboard.setText(AnnotatedString("snuoai@gmail.com"))
                        Toast.makeText(context, "이메일이 복사되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "*이메일을 누르시면 자동 복사가 됩니다.",
                    fontSize = 12.sp,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    val navController = rememberNavController()
    HelpScreen(
        navController = navController
    )
}