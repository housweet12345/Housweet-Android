package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsConditionsPolicies(
    navController: NavController
) {
    Scaffold (
        containerColor = Color.White,
        topBar = {
            // 상단 AppBar
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                title = {
                    androidx.compose.material.Text(
                        text = "서비스 이용 약관",
                        fontSize = 14.sp
                    )
                },
                navigationIcon = {
                    Icon(
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "서비스 이용 약관",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            )
            Text(
                text = "제 1장 안녕하세요.",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            )
            Text(
                text = "제 1조 목적",
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Text(
                text = "본 약관은 팀 하우스잇(이하 “하우스잇\")이 제공하는 룸메이트 찾기 및 소통 모바일 서비스(이하\"서비스)의 이용과 관련하여 회사와 회원 간의 권리, 의미 및 책임사항, 이용조건 및 절차 등 기본 사항을 규정함을 목적으로 합니다.",
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Text(
                text = "제 2조 정의",
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Text(
                text = "1. “서비스\"란 하우스잇에서 제공하는 모바일 애플리케이션을 통해 룸메이트를 찾거나 연결할 수 있도록 지원하는 플랫폼을 의미합니다.\n" +
                        "2. “회원”이란 본 약관에 동의하고 회사가 제공하는 서비스를 이용하는 자를 의미합니다.\n" +
                        "3. “계약\"이란 회원 간 개별 합의에 따라 진행되는 거주 계약, 임대차, 동거 등의 협의를 의미하며, 회사는 이에 관여하지 않습니다.\n",
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Text(
                text = "위 항에서 정의되지 않은 이 약관 상의 용어는 일반적인 거래 관행에 의합니다.",
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsConditionsPoliciesPreview() {
    val navController = rememberNavController()
    TermsConditionsPolicies(
        navController = navController
    )
}