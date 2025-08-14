package com.housweet.presentation.ui.mypage

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    navController: NavController,
    onBackClick: () -> Unit,
) {
    BackHandler {
        onBackClick()
    }

    var showContactDialog by remember { mutableStateOf(false) }

    Scaffold (
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title={
                    Text(
                        text = "마이페이지",
                        fontSize = 14.sp
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { onBackClick() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // ✅ 배경색 흰색 지정
                )
            )
        }
    ){ innerPadding ->
        if (showContactDialog) {
            ContactDialog(onDismiss = { showContactDialog = false })
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFFF7F7F7), shape = RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "김지안", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "20대 남자", fontSize = 12.sp, color = Color.Gray)
                }
            }

            // Roommate Section
            SectionTitle("하우스잇")
            MyPageMenuItem("북마크") {
                navController.navigate("bookmark")
            }
            MyPageMenuItem("올린 방 관리") {
                navController.navigate("posted_my_room")
            }
            MyPageMenuItem("마이하우스") {
                navController.navigate("myhousedetail")
            }
            MyPageMenuItem("앱 설정") {
                Log.d("MyPage", "navigate to app_setting")
                try {
                    navController.navigate("app_setting")
                } catch (e: Exception) {
                    Log.e("MyPage", "Navigation error", e)
                }
            }

            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

            // Support Section
            SectionTitle("고객 지원")
            MyPageMenuItem("공지사항") {
                navController.navigate("notice") {
                    launchSingleTop = true
                }
            }
            MyPageMenuItem("도움말") {
                navController.navigate("help")
            }

            MyPageMenuItem("개인정보처리방침") {}

            MyPageMenuItem("서비스 이용약관") {
                navController.navigate("terms_conditions_policies")
            }

            MyPageMenuItem("위치정보 이용약관") {}

            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { showContactDialog = true }
                    .fillMaxWidth()
                    .background(Color(0xFF6A5ACD), shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("하우스잇이 도움이 되었나요?", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("여러분의 의견을 들려주세요!", color = Color.White, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun MyPageMenuItem(text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontSize = 12.sp)

        Icon(
            painter = painterResource(id = R.drawable.right_back_black),
            contentDescription = "메뉴이동",
            modifier = Modifier
                .padding(start = 16.dp)
        )
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
                    text = "소중한 의견은",
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    Text(
                        text = "으로",
                        fontSize = 15.sp,
                        color = Color(0xFF333333)
                    )
                }

                Text(
                    text = "전송부탁드립니다!",
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
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
fun MyPageScreenPreview() {
    val navController = rememberNavController()
    MyPageScreen(
        navController = navController,
        onBackClick = {}
    )
}
