package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.ui.common.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsConditionsPolicies(
    navController: NavController
) {
    Scaffold (
        containerColor = Color.White,
        topBar = { TopBar(text = "서비스 이용 약관", onBackBtnClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        val bodyStyle = androidx.compose.ui.text.TextStyle(
            textIndent = TextIndent(firstLine = 0.sp, restLine = 16.sp)
        )
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(start = 24.dp, end = 24.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "서비스 이용 약관",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
            )
            Text(
                text = "제 1장 안녕하세요.",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Text(
                text = "제 1조 목적",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "본 약관은 팀 하우스잇(이하 “하우스잇\")이 제공하는 룸메이트 찾기 및 소통 모바일 서비스(이하\"서비스)의 이용과 관련하여 회사와 회원 간의 권리, 의미 및 책임사항, 이용조건 및 절차 등 기본 사항을 규정함을 목적으로 합니다.",
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "제 2조 정의",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "1. “서비스\"란 하우스잇에서 제공하는 모바일 애플리케이션을 통해 룸메이트를 찾거나 연결할 수 있도록 지원하는 플랫폼을 의미합니다.\n" +
                        "2. “회원”이란 본 약관에 동의하고 회사가 제공하는 서비스를 이용하는 자를 의미합니다.\n" +
                        "3. “계약\"이란 회원 간 개별 합의에 따라 진행되는 거주 계약, 임대차, 동거 등의 협의를 의미하며, 회사는 이에 관여하지 않습니다",
                fontSize = 13.sp,
                style = bodyStyle,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = "위 항에서 정의되지 않은 이 약관 상의 용어는 일반적인 거래 관행에 의합니다.",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "제 3조 약관의 게시 및 변경",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "1. 하우스잇은 본 약관을 앱 내에 게시하며, 개정 시 하우스잇 어플리케이션 내의 공지사항을 통해 사전 게시합니다.\n" +
                        "2. 하우스잇은 「약관의규제에관한법률」, 「정보통신망이용촉진및정보보호등에관한법률(이하”정보통신망법”)」등 관련법을 위배하지 않는 범위에서 이 약관을 개정할 수 있습니다.\n" +
                        "3. 회사가 약관을 개정할 경우에는 적용일자 및 개정사유를 명시하여 현행약관과 함께 제 1항의 방식에 따라 그 개정약관의 적용일자 7일전부터 적용일자 전일까지 공지합니다.\n" +
                        "4. 변경된 약관은 게시 시 명시된 시행일부터 효력이 발생합니다.\n" +
                        "5. 회원은 개정된 약관에 동의하지 않을 경우 서비스 이용을 중단하고 탈퇴할 수 있습니다.",
                fontSize = 13.sp,
                style = bodyStyle,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = "제 2장 하우스잇 이용계약",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 32.dp, bottom = 12.dp)
            )
            Text(
                text = "제 1조 서비스의 제공",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "1. 회사는 다음과 같은 서비스를 제공합니다:\n" +
                        "    - 룸메이트 매물에 관한 정보 제공 서비스\n" +
                        "    - 룸메이트 매물 등록 서비스\n" +
                        "    - 1:1 채팅 기능 제공\n" +
                        "    - 룸메이트 소통 서비스\n" +
                        "    - 기타 회원을 위한 서비스\n" +
                        "2. 회사는 필요 시 서비스의 일부를 추가, 변경, 중단할 수 있으며, 이 경우 사전 공지합니다.",
                fontSize = 13.sp,
                style = bodyStyle,
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
            )
            Text(
                text = "제 2조 회원의 의무",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "1. 회원은 다음 행위를 해서는 안 됩니다. \n" +
                        "    - 타인 명의 도용, 허위 정보 입력\n" +
                        "    - 타인의 명예를 손상시키거나 불이익을 주는 행위\n" +
                        "    - 욕설, 비방, 성희롱, 협박 등 부적절한 커뮤니케이션\n" +
                        "    - 게시판 등에 음란물을 게재하거나 음란사이트를 연결(링크)하는 행위\n" +
                        "    - 회사 또는 제3자의 저작권 등 기타 권리를 침해하는 행위\n" +
                        "    - 공공질서 및 미풍양속에 위반되는 내용의 정보, 문장, 도형, 음성 등을 타인에게 유포하는 행위\n" +
                        "    - 하우스잇 서비스와 관련된 설비의 오작동이나 정보 등의 파괴 및 혼란을 유발시키는 컴퓨터 바이러스 감염 자료를 유포하는 행위\n" +
                        "    - 타인으로 가장하는 행위 및 타인과의 관계를 허위로 명시하는 행위\n" +
                        "    - 다른 회원의 개인정보 수집, 저장, 공개하는 행위\n" +
                        "    - 윤락행위를 알선하거나 음행을 매개하는 내용의 정보를 유통시키는 행위\n" +
                        "    - 수치심이나 혐오감 또는 공포심을 일으키는 말이나 음향, 글 등 어떤 매체를 통해 지속적으로 상대방에게 노출시켜 일상을 방해하는 행위\n" +
                        "    - 하우스잇을 가장하거나 사칭하여 글을 게시하거나 채팅을 발송하는 행위\n" +
                        "    - 무단 상업 행위 또는 광고\n" +
                        "    - 불법 거래 유도, 범죄 목적의 접촉\n" +
                        "2. 위반 시 하우스잇은 회원의 위반 행위를 조사할 수 있고 이용 제한, 재가입 제한, 탈퇴 조치, 관계기관 신고 등의 조치를 취할 수 있습니다. 조치 결과가 불만족스러울 경우 고객센터를 통해 이의를 제기할 수 있습니다.",
                fontSize = 13.sp,
                style = bodyStyle,
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
            )
            Text(
                text = "제 3조 회사의 면책 및 손해배상",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "1. 하우스잇은 서비스에 게재된 정보, 자료, 사실의 신뢰도, 정확성 등에 대해서는 보증을 하지 않습니다. 이로 인해 회원에게 발생한 손해에 대해서는 책임을 부담하지 아니합니다.\n" +
                        "2. 하우스잇은 회원 간의 직접적인 연락, 만남, 계약 체결 등에 개입하지 않으며, 이에 대한 책임을 지지 않습니다.\n" +
                        "3. 회원 간 이루어진 모든 정보 교환, 계약, 분쟁은 해당 당사자들 간의 책임 하에 해결되어야 합니다.\n" +
                        "4. 하우스잇은 다음의 경우 책임을 지지 않습니다:\n" +
                        "    - 회원이 등록한 정보의 진위 여부\n" +
                        "    - 회원 간 합의된 계약의 법적 효력\n" +
                        "    - 실제 동거 과정에서 발생한 분쟁, 손해 등\n" +
                        "    - 천재 지변 또는 이에 준하는 불가항력의 상태에서 발생한 손해\n" +
                        "    - 서비스 접속 또는 이용과정에서 발생하는 개인적인 손해\n" +
                        "    - 제3자가 불법적으로 하우스잇의 서버에 접속하거나 서버를 이용함으로써 발생하는 손해\n" +
                        "    - 제3자가 악성 프로그램을 전송 또는 유포함으로써 발생하는 손해\n" +
                        "    - 전송된 데이터의 생략, 누락, 파괴 등으로 발생한 손해, 명예훼손 등 제3자가 서비스를 이용하는 과정에서 발생된 손해\n" +
                        "    - 기타 회사의 고의 또는 고실이 없는 사유로 인해 발생한 손해",
                fontSize = 13.sp,
                style = bodyStyle,
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
            )
            Text(
                text = "제 4조 개인정보의 보호",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "회사는 “개인정보 보호법” 및 관련 법령에 따라 회원의 정보를 안전하게 보호합니다. 자세한 사항은 [개인정보처리방침]에서 확인할 수 있습니다.",
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
            )
            Text(
                text = "제 5조 서비스 이용의 제한 및 중단",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "회사는 다음의 경우 사전 통지 없이 서비스 이용을 제한하거나 중단할 수 있습니다:",
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "1. 시스템 점검, 유지보수 등 기술적 사유\n" +
                        "2. 불가항력(천재지변, 정전 등)\n" +
                        "3. 회원이 약관을 위반한 경우",
                fontSize = 13.sp,
                style = bodyStyle,
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 8.dp)
            )
            Text(
                text = "제 6조 지적 재산권",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "서비스 내 제공된 콘텐츠 및 기술은 회사에 귀속되며, 무단 복제, 배포, 사용을 금지합니다.",
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "제 7조 준거법 및 관할",
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "1. 본 약관은 대한민국 법률을 따릅니다.\n" +
                        "2. 본 약관 및 서비스와 관련된 분쟁은 하우스잇 소재지 관할 법원을 1심 법원으로 합니다.\n" +
                        "3. 이 약관은 2025년 9월 1일 공고, 9월 1일부터 적용됩니다.",
                fontSize = 13.sp,
                style = bodyStyle,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))
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