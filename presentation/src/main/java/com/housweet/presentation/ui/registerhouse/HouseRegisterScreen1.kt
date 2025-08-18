package com.housweet.presentation.ui.registerhouse

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.ui.common.StepIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.housweet.domain.local.RoomLocalDataSource
import com.housweet.domain.model.HouseRegisterModel
import com.housweet.domain.repository.HouseRegisterRepository
import com.housweet.domain.repository.ImageUploadRepository
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.common.TopBarWithBackButton
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModel
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModelBase
import java.io.File

@Composable
fun HouseRegisterScreen1(
    mode: RegisterModel,
    postingId: Int?,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: HouseRegisterViewModelBase
) {
    LaunchedEffect(mode, postingId) {
        viewModel.logRoomId()
        if(mode == RegisterModel.EDIT && postingId != null) {
            viewModel.loadForEdit(postingId) // 프리필 로딩
        }
    }

    BackHandler {
        onBackClick()
    }

    // 1) 섹션 정의
    val sections = listOf(
        "교통" to listOf("버스정류장 인근", "역세권", "버스, 지하철 더블 역세권", "자차 추천", "교통 좋음",
            "교통 나쁘지 않음"),
        "집 상태" to listOf("쾌적함", "채광 좋음", "뷰가 좋음", "환기 잘 됨", "리모델링함", "관리 잘 됨", "방음 잘 됨", "층간 소음 없음", "풀옵션", "냉난방 잘 됨", "저층", "고층", "엘레베이터", "벌레 없음"),
        "인프라" to listOf("마트", "편의점", "카페", "식당", "병원", "공원", "산책로", "숲세권", "약국", "전통시장", "헬스장", "대학가", "상권 많음"),
//        "기타" to listOf("치안 좋음", "밤길 안전함", "전입신고 가능", "전입신고 불가능", "단기 거주 가능", "장기 거주 가능", "장기 거주 희망", "즉시 입주 가능", "입주일 상의 필요", "월세 및 보증금 협의 가능", "월세 및 보증금 협의 불가")
    )

    // 2) 섹션별 선택 상태 저장
    val selectedBySection = remember {
        mutableStateMapOf<String, MutableSet<String>>().apply {
            sections.forEach { (title, _) -> put(title, mutableSetOf()) }
        }
    }

    // 3) 검증 & 다이얼로그 상태
    var showDialog by remember { mutableStateOf(false) }
    var missingSectionName by remember { mutableStateOf<String?>(null) }

    fun firstMissingSectionOrNull(): String? =
        sections.firstOrNull { (title, _) -> selectedBySection[title].isNullOrEmpty() }?.first

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
        // 상단바 + 제목
        TopBarWithBackButton(
            title = if (mode == RegisterModel.EDIT) "글 수정하기" else "하우스 올리기",
            currentStep = 1,
            onBackClick = onBackClick,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(innerPadding),
        ) {
            // 중앙 정렬된 콘텐츠 (텍스트 + 태그 선택)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "집에 대한 키워드를 선택해주세요.",
                    color = Color(0xFF6C4DFF),
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = 12.sp
                )

                sections.forEach { (title, tags) ->
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tags.forEach { tag ->
                            val isSelected = selectedBySection[title]?.contains(tag) == true
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (isSelected) Color(0xFF665ED3) else Color.White,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        val current = selectedBySection[title]?.toMutableSet() ?: mutableSetOf()
                                        if (isSelected) current.remove(tag) else current.add(tag)
                                        selectedBySection[title] = current
                                    }
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFF665ED3),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            ) {
                                Text(
                                    text = tag,
                                    color = if (isSelected) Color.White else Color.Black,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val missing = firstMissingSectionOrNull()
                    if (missing == null) {
                        val traffic = selectedBySection["교통"]?.toList() ?: emptyList()
                        val size    = selectedBySection["집 상태"]?.toList() ?: emptyList()
                        val infra   = selectedBySection["인프라"]?.toList() ?: emptyList()

                        viewModel.updateHouseTags(traffic)          // ✅ 교통 → traffic_tags
                        viewModel.updateSizeOfHouseTags(size)       // ✅ 집 상태 → size_of_house_tags
                        viewModel.updateInfraTags(infra)            // ✅ 인프라 → infra_tags
                        onNextClick()
                    } else {
                        missingSectionName = missing
                        showDialog = true
                    }
                },
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF665ED3),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "다음",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                border = BorderStroke(1.dp, Color(0xFF665ED3))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${missingSectionName ?: "항목"} 을(를) 선택해주세요.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = { showDialog = false }) {
                        Text("확인")
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HouseRegisterScreen1Preview() {
    val fakeViewModel = remember { PreviewHouseRegisterViewModel() }

    HouseRegisterScreen1(
        mode = RegisterModel.CREATE,
        postingId = 1,
        onNextClick = {},
        onBackClick = {},
        viewModel = fakeViewModel
    )
}

class PreviewHouseRegisterViewModel : HouseRegisterViewModelBase()