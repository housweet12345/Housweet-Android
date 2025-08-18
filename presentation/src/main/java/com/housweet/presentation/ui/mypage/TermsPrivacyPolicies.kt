package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.theme.Black

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsPrivacyPolicies(
    onBackClick: () -> Unit
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
                    Text(
                        text = "개인정보처리방침",
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                GuideText(
                    modifier = Modifier.padding(top = 30.dp),
                    color = Black,
                    text = "개인정보처리방침",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )

                GuideText(
                    modifier = Modifier.padding(top = 10.dp),
                    color = Black,
                    text = "팀 하우스잇 (이하 “하우스잇”)는 「개인정보 보호법」 등 관련 법령에 따라 회원의 개인정보를 보호하고, 이와 관련한 고지 및 권리 보호를 위하여 다음과 같이 개인정보처리방침을 수립·공개합니다.",
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Start
                )
            }

            item {
                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제1조 (수집하는 개인정보 항목 및 수집 방법)",
                ) {
                    DataTable(
                        data = listOf(
                            "구분" to "수집 항목",
                            "필수" to "이름, 생년월일, 이메일, SNS 계정",
                            "선택" to "관심 지역, 성별, 자기소개, 거주 희망 조건, 선호 룸메 조건, 사진 등",
                            "자동 수집" to "IP주소, 기기 정보, 접속 일시, 서비스 이용기록, 오류 로그 등"
                        )
                    )
                }

                Policy(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "1. 수집 방법"
                ) {
                    GuideText(
                        color = Black,
                        text = "- 회원가입 및 프로필 입력 시\n" +
                                "- 서비스 이용 중 입력 또는 자동 생성\n" +
                                "- 고객센터 문의, 이벤트 응모 시",
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Start
                    )
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제2조 (개인정보의 수집 및 이용 목적)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "하우스잇은 수집한 개인정보를 다음의 목적에 한하여 이용합니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )

                        DataTable(
                            data = listOf(
                                "목적" to "세부 내용",
                                "서비스 제공" to "룸메이트 찾기, 채팅 등 핵심 기능 제공",
                                "회원 관리" to "본인 확인, 불량회원 방지, 서비스 이용 이력 관리",
                                "알림 및 공지" to "시스템 알림, 약관 변경 등 고지 사항 전달",
                                "고객 지원" to "문의 대응, 분쟁 처리 등"
                            )
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제3조 (개인정보의 보유 및 이용 기간)",
                ) {
                    Column {
                        DataTable(
                            data = listOf(
                                "목적" to "보유 기간",
                                "회원 정보" to "회원 탈퇴 시 즉시 파기 (단, 관련 법령에 따라 보관할 필요가 있는 경우는 제외)",
                                "채팅 기록" to "회원 탈퇴 시 즉시 파기 (단, 관련 법령에 따라 보관할 필요가 있는 경우는 제외)",
                                "로그기록, 접속 IP 등" to "3개월 (통신비밀보호법)",
                            )
                        )

                        GuideText(
                            modifier = Modifier.padding(top = 20.dp),
                            color = Black,
                            text = "※ 관계 법령에 따른 예외적 보관\n" +
                                    "- 계약 또는 청약 철회 기록: 5년 (전자상거래법)\n" +
                                    "- 소비자 불만/분쟁 처리 기록: 3년 (전자상거래법)\n" +
                                    "- 본인 확인 기록: 6개월 (정보통신망법)",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제4조 (개인정보 제3자 제공)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "하우스잇은 회원의 동의 없이 개인정보를 외부에 제공하지 않습니다.\n" +
                                    "다만 다음의 경우 예외로 합니다:\n" +
                                    "- 법령에 따른 수사기관 요청 시",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제5조 (개인정보 처리의 위탁)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "하우스잇은 원활한 서비스 제공을 위해 일부 업무를 외부 전문 업체에 위탁할 수 있습니다.\n" +
                                    "위탁 시 계약을 통해 개인정보 보호 조치를 명확히 규정합니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )

                        DataTable(
                            data = listOf(
                                "수탁 업체" to "위탁 업무",
                                "[Amazon AWS 등]" to "클라우드 서버 운영 및 데이터 보관",
                                "[Firebase 등]" to "푸시 알림 발송, 오류 로그 분석",
                            )
                        )

                        GuideText(
                            modifier = Modifier.padding(top = 20.dp),
                            color = Black,
                            text = "※ 위탁 현황은 변경 시 본 방침을 통해 고지합니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제6조 (개인정보의 파기)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 개인정보 보유기간이 경과하거나 처리 목적이 달성된 경우, 해당 정보를 즉시 파기합니다.\n" +
                                    "2. 전자적 파일은 복구 불가능한 방법으로 삭제하며, 종이 문서는 분쇄 또는 소각 방식으로 파기합니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제7조 (이용자의 권리 및 행사 방법)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 회원은 언제든지 본인의 개인정보에 대해 열람, 수정, 삭제, 처리 정지를 요청할 수 있습니다.\n" +
                                    "2. 탈퇴 시 수집된 개인정보는 관련 법령을 제외하고 즉시 삭제됩니다.\n" +
                                    "3. 앱 내 ‘문의하기’ 기능을 통해 요청할 수 있습니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제8조 (개인정보 자동 수집 장치의 설치·운영 및 거부)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "하우스잇은 사용자 맞춤형 서비스를 제공하기 위해 쿠키(cookie)를 사용할 수 있습니다.\n" +
                                    "- 쿠키 거부 시 일부 기능 이용에 제한이 있을 수 있습니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제9조 (개인정보 보호 책임자)",
                ) {
                    DataTable(
                        data = listOf(
                            "구분" to "내용",
                            "책임자" to "김선유",
                            "연락처" to "snuoai@gmail.com",
                            "문의시간" to "평일 10:00 ~ 18:00"
                        )
                    )
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제10조 (개인정보처리방침의 변경)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "- 본 방침은 2025년 9월 1일부터 적용됩니다.\n" +
                                    "- 법령 변경 또는 내부 방침 변경에 따라 내용이 수정될 수 있으며, 변경 시 사전 공지합니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun Policy(
    modifier: Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    GuideText(
        modifier = modifier,
        color = Black,
        text = title,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 24.sp,
        textAlign = TextAlign.Start
    )

    content()
}

@Composable
private fun DataTable(
    data: List<Pair<String, String>>
) {
    Surface(
        modifier = Modifier
            .padding(top = 10.dp)
            .border(width = 1.dp, color = Color.Black.copy(alpha = 0.3f))
            .fillMaxWidth(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            data.forEachIndexed { index, (category, items) ->
                TableRow(
                    category = category,
                    items = items,
                    isHeader = index == 0
                )

                if (index < data.size - 1) {
                    Divider(
                        color = Color.Black.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun TableRow(
    category: String,
    items: String,
    isHeader: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top
    ) {

        GuideText(
            modifier = Modifier
                .padding(start = 10.dp , top = 10.dp, bottom = 10.dp)
                .width(80.dp),
            text = category,
            color = Color.Black,
            fontSize = 13.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )

        Divider(
            color = Color.Black.copy(alpha = 0.3f),
            modifier = Modifier
                .padding(start = 5.dp, end = 20.dp)
                .fillMaxHeight()
                .width(1.dp)
        )

        GuideText(
            modifier = Modifier
                .padding(end = 10.dp , top = 10.dp, bottom = 10.dp)
                .weight(1f),
            text = items,
            color = Color.Black,
            fontSize = 13.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsPrivacyPoliciesPreview() {
    TermsPrivacyPolicies { }
}