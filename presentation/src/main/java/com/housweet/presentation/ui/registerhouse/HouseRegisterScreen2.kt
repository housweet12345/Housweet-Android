package com.housweet.presentation.ui.registerhouse

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.housweet.presentation.model.Region
import com.housweet.presentation.model.RegisterModel
import com.housweet.presentation.ui.common.RegionBottomSheet
import com.housweet.presentation.ui.common.StepIndicator
import com.housweet.presentation.ui.common.TopBarWithBackButton
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModel
import com.housweet.presentation.viewmodel.registerhouse.HouseRegisterViewModelBase
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun HouseRegisterScreen2(
    mode: RegisterModel,
    postingId: Int?,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: HouseRegisterViewModelBase
) {
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

    var showDialog by remember { mutableStateOf(false) }
    var missingField by remember { mutableStateOf<String?>(null) }

    fun firstMissingFieldOrNull(): String? {
        return when {
            selectedRegion == null -> "지역"
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
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current

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

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(4.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
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

            Spacer(modifier = Modifier.height(12.dp))
            Text("제목", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = inputTitle,
                onValueChange = { inputTitle = it },
                placeholder = { Text("제목을 입력해주세요.", color = Color(0xFFA5A5A5), fontSize = 12.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    },
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text("자세한 설명", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = inputDescription,
                onValueChange = { inputDescription = it },
                placeholder = { Text("자세한 설명을 입력해주세요.", color = Color(0xFFA5A5A5), fontSize = 12.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(4.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    }
                ,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("보증금", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = deposit,
                                onValueChange = { deposit = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                placeholder = { Text("") }, // placeholder 제거
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                            Text(
                                text = "만원",
                                fontSize = 12.sp,
                                color = Color(0xFFA5A5A5)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("월세", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = monthlyRent,
                                onValueChange = { monthlyRent = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                placeholder = { Text("") }, // placeholder 제거
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                            Text(
                                text = "만원",
                                fontSize = 12.sp,
                                color = Color(0xFFA5A5A5)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("관리비", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = managementFee,
                            onValueChange = { managementFee = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp),
                            placeholder = { Text("") }, // placeholder 제거
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                        Text(
                            text = "만원",
                            fontSize = 12.sp,
                            color = Color(0xFFA5A5A5)
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))
            Text("입주 가능일", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = moveInDate,
                onValueChange = { moveInDate = it },
                placeholder = { Text("가능한 날짜 또는 시기를 작성해주세요.", color = Color(0xFFA5A5A5), fontSize = 12.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    }
                ,
                shape = RoundedCornerShape(6.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "ⓘ 경고문 블라블라",
                color = Color(0xFF6C4DFF),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // 검증
                    val missing = firstMissingFieldOrNull()
                    if (missing != null) {
                        missingField = missing
                        showDialog = true
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

            Spacer(modifier = Modifier.height(16.dp))
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
                        text = "${missingField ?: "항목"}을(를) 입력(선택)해주세요.",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = { showDialog = false }) { Text("확인") }
                }
            }
        }
    }

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