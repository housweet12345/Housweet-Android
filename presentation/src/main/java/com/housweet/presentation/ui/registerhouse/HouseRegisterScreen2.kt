package com.housweet.presentation.ui.registerhouse

import android.R.attr.lineHeight
import android.R.attr.text
import android.R.attr.textColor
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.model.Region
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.common.GuideText
import com.housweet.presentation.ui.common.RegionBottomSheet
import com.housweet.presentation.ui.common.TopBarWithBackButton
import com.housweet.presentation.ui.theme.Black
import com.housweet.presentation.ui.theme.Gray_7E7E7E
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.Gray_CBCBCB
import com.housweet.presentation.ui.theme.White
import com.housweet.presentation.ui.theme.nanumSquareFontFamily
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModelBase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

private fun Region?.isComplete(): Boolean {
    return this != null &&
            sido.isNotBlank() && sigungu.isNotBlank() && dong.isNotBlank() &&
            sidoCode.isNotBlank() && sigunguCode.isNotBlank() && dongCode.isNotBlank()
}

@Composable
fun HouseRegisterScreen2(
    mode: RegisterModel,
    postingId: Int?,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: HouseRegisterViewModelBase
) {
    BackHandler {
        onBackClick()
    }

    var region by remember { mutableStateOf("") }
    var inputTitle by remember { mutableStateOf("") }
    var inputDescription by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var managementFee by remember { mutableStateOf("") }
    var moveInDate by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val regionBundle = remember { loadRegionDataBundle(context) }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedRegion by remember { mutableStateOf<Region?>(null) }

    var missingField by remember { mutableStateOf<String?>(null) }

    val isStep2Valid by remember(
        selectedRegion, inputTitle, inputDescription,
        deposit, monthlyRent, managementFee, moveInDate
    ) {
        mutableStateOf(
            selectedRegion.isComplete() &&
                    inputTitle.isNotBlank() &&
                    inputDescription.isNotBlank() &&
                    deposit.isNotBlank() &&
                    monthlyRent.isNotBlank() &&
                    managementFee.isNotBlank() &&
                    moveInDate.isNotBlank()
        )
    }

    fun firstMissingFieldOrNull(): String? {
        val r = selectedRegion
        return when {
            r == null -> "지역(시/군/구/동)"
            r.sido.isBlank() -> "지역(시/도)"
            r.sigungu.isBlank() -> "지역(시/군/구)"
            r.dong.isBlank() -> "지역(동)"
            r.sidoCode.isBlank() || r.sigunguCode.isBlank() || r.dongCode.isBlank() -> "지역(코드 매핑)"
            inputTitle.isBlank() -> "제목"
            inputDescription.isBlank() -> "자세한 설명"
            deposit.isBlank() -> "보증금"
            monthlyRent.isBlank() -> "월세"
            managementFee.isBlank() -> "관리비"
            moveInDate.isBlank() -> "입주 가능일"
            else -> null
        }
    }

    LaunchedEffect(mode, postingId, regionBundle) {
        if (mode == RegisterModel.EDIT && selectedRegion == null) {
            val vmRegion = viewModel.region
            if (vmRegion != null) {
                val siName = regionBundle.siCodeToName[vmRegion.sidoCode].orEmpty()
                val guName = regionBundle.guCodeToName[vmRegion.sigunguCode].orEmpty()
                val dongName = regionBundle.dongCodeToName[vmRegion.dongCode].orEmpty()
                selectedRegion = vmRegion.copy(
                    sido = siName,
                    sigungu = guName,
                    dong = dongName
                )
            }
        }
    }

    val regionDisplay = selectedRegion?.let { r ->
        listOf(r.sido, r.sigungu, r.dong)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }?.takeIf { it.isNotBlank() } ?: "지역을 선택해주세요."

    Scaffold (
        topBar = {
            TopBarWithBackButton(
                title = if (mode == RegisterModel.EDIT) "글 수정하기" else "하우스 올리기",
                currentStep = 2,
                onBackClick = onBackClick
            )
        },
        contentWindowInsets = WindowInsets(0)
    ){ innerPadding ->
        KeyboardClosableContainer{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(scrollState)
                    .background(Color.White)
                    .padding(innerPadding)
            ) {
                val focusRequester = remember { FocusRequester() }
                val keyboardController = LocalSoftwareKeyboardController.current

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "자세한 정보를 입력해주세요.",
                            color = Color(0xFF6C4DFF),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .border(0.5.dp, Color.Gray, RoundedCornerShape(6.dp))
                            .clickable { showBottomSheet = true }
                            .background(Color.White, shape = RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = regionDisplay,
                            color = if (regionDisplay == "지역을 선택해주세요.") Color.Gray else Color.Black,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("제목", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    RegisterContentTextField(
                        text = inputTitle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    keyboardController?.show()
                                }
                            }
                            .autoBringIntoViewOnFocus(),
                        height = 30.dp,
                        hint = "제목을 입력해주세요.",
                        alignment = Alignment.CenterStart,
                        onValueChange = {
                            inputTitle = it
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "자세한 설명",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    RegisterContentTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    keyboardController?.show()
                                }
                            }
                            .autoBringIntoViewOnFocus(),
                        text = inputDescription,
                        textLength = 9999,
                        hint = "자세한 설명을 입력해주세요.",
                        height = 90.dp,
                        singleLine = false,
                        alignment = Alignment.TopStart,
                        topPaddingOnText = 8.dp,
                        onValueChange = {
                            inputDescription = it
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "보증금",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                contentAlignment = Alignment.CenterStart
                            ) {
                                RegisterCostTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .autoBringIntoViewOnFocus(),
                                    text = deposit,
                                    height = 30.dp,
                                    onValueChange = { deposit = it }
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "월세",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            RegisterCostTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .autoBringIntoViewOnFocus(),
                                text = monthlyRent,
                                height = 30.dp,
                                onValueChange = { monthlyRent = it }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("관리비", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    RegisterCostTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 10.dp)
                            .autoBringIntoViewOnFocus(),
                        text = managementFee,
                        height = 30.dp,
                        onValueChange = { managementFee = it }
                    )


                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "입주 가능일",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    RegisterContentTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    keyboardController?.show()
                                }
                            }
                            .autoBringIntoViewOnFocus(),
                        text = moveInDate,
                        textLength = 50,
                        height = 30.dp,
                        hint = "가능한 날짜 또는 시기를 작성해주세요.",
                        alignment = Alignment.CenterStart,
                        onValueChange = { moveInDate = it }
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = "ⓘ 경고문 블라블라",
                        color = Color(0xFF6C4DFF),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    enabled = isStep2Valid,
                    onClick = {
                        // 검증
                        val missing = firstMissingFieldOrNull()
                        if (missing != null) {
                            missingField = missing
                            return@Button
                        }

                        // 통과 시 ViewModel 저장 & 다음 단계
                        viewModel.setStep2Data(
                            region = selectedRegion,
                            title = inputTitle.trim(),
                            desc = inputDescription.trim(),
                            deposit = deposit.trim(),
                            rent = monthlyRent.trim(),
                            fee = managementFee.trim(),
                            moveIn = moveInDate.trim()
                        )
                        onNextClick()
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
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
            }
        }
    }

    if (showBottomSheet) {
        RegionBottomSheet(
            cities = regionBundle.cities,
            districtMap = regionBundle.districtMap,
            neighborhoodMap = regionBundle.neighborhoodMap,
            citiesWithCodes = regionBundle.siList,
            districtsWithCodes = regionBundle.guList,
            neighborhoodsWithCodes = regionBundle.dongList,
            onRegionSelected = { region ->
                selectedRegion = region
                showBottomSheet = false
            },
            onDismissRequest = {
                showBottomSheet = false
            }
        )
    }
}

@Composable
private fun RegisterContentTextField(
    modifier: Modifier,
    text: String,
    height: Dp,
    hint: String,
    textLength: Int = 20,
    textColor: Color = Black,
    singleLine: Boolean = true,
    alignment: Alignment,
    topPaddingOnText: Dp = 0.dp,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = {
            if (it.length <= textLength) {
                onValueChange(it)
            }
        },
        modifier = modifier,
        textStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.W400,
            fontFamily = nanumSquareFontFamily,
            color = textColor
        ),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                shape = RoundedCornerShape(6.dp),
                color = White,
                border = BorderStroke(width = 0.5.dp, color = Color.Gray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = topPaddingOnText),
                    contentAlignment = alignment
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = hint,
                            color = Color.Gray,
                            fontSize = 12.sp,
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun RegisterCostTextField(
    modifier: Modifier,
    text: String,
    height: Dp,
    textLength: Int = 10,
    textColor: Color = Black,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = {
            if (it.length <= textLength) {
                onValueChange(it)
            }
        },
        modifier = modifier,
        textStyle = TextStyle(
            fontSize = 12.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.W400,
            fontFamily = nanumSquareFontFamily,
            color = textColor
        ),
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height),
                shape = RoundedCornerShape(6.dp),
                color = White,
                border = BorderStroke(width = 0.5.dp, color = Color.Gray)
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp),
                        text = "만원",
                        fontSize = 12.sp,
                        color = Color(0xFFA5A5A5)
                    )
                }
            }
        }
    )
}

private fun norm(s: String?): String =
    s?.replace("\uFEFF", "")?.trim().orEmpty()

fun readCsv(context: Context, fileName: String): List<Map<String, String>> {
    context.assets.open(fileName).use { inputStream ->
        BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
            val rawHeader = reader.readLine() ?: return emptyList()
            val headers = rawHeader.split(",").map { norm(it) }

            return reader.lineSequence()
                .filter { it.isNotBlank() }
                .map { line ->
                    val cols = line.split(",").map { norm(it) }
                    // 컬럼 개수가 모자라도 안전하게 매핑
                    headers.zip(cols + List((headers.size - cols.size).coerceAtLeast(0)) { "" })
                        .toMap()
                }
                .toList()
        }
    }
}



data class RegionDataBundle(
    val cities: List<String>,
    val districtMap: Map<String, List<String>>,
    val neighborhoodMap: Map<Pair<String, String>, List<String>>,

    //원본 리스트
    val siList: List<Map<String, String>>,
    val guList: List<Map<String, String>>,
    val dongList: List<Map<String, String>>,

    // 코드 -> 이름 매핑 추가
    val siCodeToName: Map<String, String>,
    val guCodeToName: Map<String, String>,
    val dongCodeToName: Map<String, String>
)


fun loadRegionDataBundle(context: Context): RegionDataBundle {
    val siList = readCsv(context, "시_정보.csv")
    val guList = readCsv(context, "구_정보.csv")
    val dongList = readCsv(context, "동_정보.csv")

    val cities = siList.mapNotNull { it["name"] }.distinct()

    val districtMap = guList.groupBy(
        keySelector = { it["si__name"] ?: "" },
        valueTransform = { it["name"] ?: "" }
    ).mapValues { it.value.filter { it.isNotEmpty() } }

    val neighborhoodMap = dongList.groupBy(
        keySelector = { Pair(it["si__name"] ?: "", it["gu__name"] ?: "") },
        valueTransform = { it["name"] ?: "" }
    ).mapValues { it.value.filter { it.isNotEmpty() } }

    val siCodeToName = siList.mapNotNull {
        val code = norm(it["code"]).takeIf { it.isNotEmpty() } ?: return@mapNotNull null
        val name = norm(it["name"]).takeIf { it.isNotEmpty() } ?: return@mapNotNull null
        code to name
    }.toMap()

    val guCodeToName = guList.mapNotNull {
        val code = norm(it["code"]).takeIf { it.isNotEmpty() } ?: return@mapNotNull null
        val name = norm(it["name"]).takeIf { it.isNotEmpty() } ?: return@mapNotNull null
        code to name
    }.toMap()

    val dongCodeToName = dongList.mapNotNull {
        val code = norm(it["code"]).takeIf { it.isNotEmpty() } ?: return@mapNotNull null
        val name = norm(it["name"]).takeIf { it.isNotEmpty() } ?: return@mapNotNull null
        code to name
    }.toMap()

    return RegionDataBundle(
        cities = cities,
        districtMap = districtMap,
        neighborhoodMap = neighborhoodMap,
        siList = siList,
        guList = guList,
        dongList = dongList,
        siCodeToName = siCodeToName,
        guCodeToName = guCodeToName,
        dongCodeToName = dongCodeToName
    )
}

@Preview(showBackground = true)
@Composable
fun HouseRegisterScreen2Preview() {
    val fakeViewModel = remember { PreviewHouseRegisterViewModel2() }

    HouseRegisterScreen2(
        mode = RegisterModel.CREATE,
        postingId = 1,
        onNextClick = {},
        onBackClick = {},
        viewModel = fakeViewModel
    )
}

class PreviewHouseRegisterViewModel2 : HouseRegisterViewModelBase()

@Composable private fun KeyboardClosableContainer(content: @Composable () -> Unit) {
    val fm = LocalFocusManager.current
    Box(modifier = Modifier
        .fillMaxSize()
        .imePadding() // 키보드 높이만큼 안전 패딩
        .navigationBarsPadding() // 제스처/네비바 대응
        .pointerInput(Unit) {
            detectTapGestures(onTap = { fm.clearFocus() }) // 바깥 탭 → 키보드 닫힘
        }
    ) { content() }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.autoBringIntoViewOnFocus(): Modifier {
    val requester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    return this
        .bringIntoViewRequester(requester)
        .onFocusChanged { state ->
            if (state.isFocused) {
                scope.launch {
                    // 키보드가 뜨기 시작한 뒤 스크롤되도록 살짝 지연
                    delay(120)
                    requester.bringIntoView()
                }
            }
        }
}
