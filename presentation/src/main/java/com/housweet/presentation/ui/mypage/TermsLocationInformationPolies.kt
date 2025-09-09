package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.ui.theme.Black

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsLocationInformationPolies(
    onBackClick: () -> Unit
) {
    Scaffold (
        containerColor = Color.White,
        topBar = { TopBar(text = "위치정보이용약관", onBackBtnClick = onBackClick) }
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
                    text = "위치정보이용약관",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )
            }

            item {
                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제1조 (목적)",
                ) {
                    GuideText(
                        color = Black,
                        text = "본 약관은 팀 하우스잇(이하”하우스잇”)이 제공하는 룸메이트 찾기 및 소통 서비스(이하 “서비스”라 함)를 이용하는 고객 사이의 서비스 이용에 관한 제반사항을 정함을 목적으로 합니다. ",
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Start
                    )
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제2조 (서비스 내용 및 이용 목적)"
                ) {
                    GuideText(
                        color = Black,
                        text = "1. 하우스잇은 수집한 개인 위치정보를 활용하여 아래의 서비스를 제공합니다\n" +
                                "- 하우스 매물 등에 관한 정보 제공 서비스\n" +
                                "- 하우스 매물 등록 서비스\n" +
                                "2. 서비스는 실시간 위치정보를 저장하지 않으며, 서비스 이용 종료 시 즉시 파기합니다.\n" +
                                "3. 하우스잇은 GPS 자동 수집이 아닌 사용자가 입력한 행정 구역 정보만 사용합니다.\n" +
                                "4. 이 약관은  「위치정보의보호및이용등에관한법률」 및 관계 법령 등에서 정하는 바에 따릅니다.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Start
                    )
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제3조 (계약의 체결 및 해지)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 하우스잇의 서비스를 이용하고자 하는 경우 회원가입을 진행해야 하며 약관의 고지 내용에 따라 개인위치정보 서비스에 가입하게 됩니다. \n" +
                                    "2. 회원은 위치정보를 알리고 싶지 않을 경우 회원탈퇴를 하거나, 하우스잇의 고객센터 이메일을 통해 해지신청을 하여야 합니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제4조 (개인 위치정보의 보유 및 이용)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 하우스잇은 회원이 등록한 매물의 위치 정보만 다른 회원에게 제공합니다. 등록하신 위치 정보를 다른 정보와 결합하여 개인 위치 정보로 이용하지 않습니다.\n" +
                                    "2. 회원은 게시글 작성 시 시, 구, 동 단위의 위치를 입력할 수 있으며 이는 게시글 노출 지역 설정 목적에만 사용됩니다.\n" +
                                    "3. 단, 서비스 개선을 위한 비식별 통계정보로 전환하여 저장하는 경우가 있습니다.\n" +
                                    "4. 서비스의 이용은 24시간 가능합니다. 다만 시스템 장애 및 프로그램 오류 보수 등으로 서비스 제공이 불가한 경우 사전 공지를 통해 안내드립니다.\n" +
                                    "5. 위치 정보는 탈퇴 즉시 파기됩니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제5조 (서비스 이용요금)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 회사가 제공하는 위치정보 서비스는 무료입니다.\n" +
                                    "2. 단, 무선 서비스 이용 시 발생하는 데이터 통신료는 별도이며, 사용자가 가입한 각 이동통신사의 정책에 따릅니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제6조 (개인 위치정보의 제3자 제공)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "하우스잇은 회원의 동의 없이는 위치정보를 제3자에게 제공하지 않습니다.\n" +
                                    "단, 아래의 경우는 예외로 합니다:\n" +
                                    "- 관련 법령에 따른 수사기관이나 법원 등 국가기관의 요구가 있는 경우\n" +
                                    "- 통계 목적의 비식별화 정보 제공 (개인 식별 불가)\n" +
                                    "※ 제3자 제공 시, 제공받는 자, 목적, 보유 기간 등을 별도 고지하고 동의를 받습니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제7조 (권리와 의무)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 회원은 자신의 위치정보 열람, 제공내역 통지요구, 오류 정정 등을 요청할 수 있습니다.\n" +
                                    "2. 회사는 해당 요청에 성실히 응하며, 필요한 경우 법령에 따라 열람 제한이 가능합니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제8조 (손해배상)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 하우스잇은 위치정보법 등 관련 법령을 위반하여 이용자에게 손해가 발생한 경우 그 손해를 배상합니다.\n" +
                                    "2. 다만, 다음 각 호의 경우에는 손해배상을 책임을 지지 않습니다.\n" +
                                    "- 천재지번, 전쟁, 폭동, 해킹 등 불가항력으로 서비스를 제공할 수 없는 경우\n" +
                                    "- 이용자의 귀책사유로 발생한 경우\n" +
                                    "- 서비스 제공자가 합리적인 보안 절차를 이행하였음에도 불구하고 제 3자의 불법 행위로 발생한 경우\n" +
                                    "- 회원 또는 개인간의 교류에서 발생한 문제일 경우\n" +
                                    "3. 서비스 제공자는 고의 또는 중대한 과실이 없는 한 손해배상 책임을 지지 않습니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제 9조 (면책)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "1. 하우스잇은 위치정보를 이용한 서비스 제공과 관련하여 이용자에게 발생한 손해 중 서비스 제공자의 고의 또는 중대한 과실이 없는 경우 책임을 지지 않습니다.\n" +
                                    "2. 이용자가 본 약관을 위반하여 발생한 모든 손해에 대해서는 이용자가 책임을 집니다.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Policy(
                    modifier = Modifier.padding(top = 32.dp),
                    title = "제10조 (위치정보관리책임자)",
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
                    title = "제11조 (약관 변경)",
                ) {
                    Column {
                        GuideText(
                            color = Black,
                            text = "본 약관은 2025년 9월 1일부터 적용됩니다.\n" +
                                    "변경 시 사전 고지하며, 동의 후 서비스를 계속 이용할 수 있습니다.",
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
fun TermsLocationInformationPoliesPreview() {
    TermsLocationInformationPolies { }
}