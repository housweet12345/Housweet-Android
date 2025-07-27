package com.housweet.presentation.ui.registerhouse

import android.content.Context
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.model.Region
import com.housweet.presentation.ui.common.RegionBottomSheet
import com.housweet.presentation.ui.common.StepIndicator
import com.housweet.presentation.ui.common.TopBarWithBackButton
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun HouseRegisterScreen2(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    var region by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var managementFee by remember { mutableStateOf("") }
    var moveInDate by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val regionBundle = remember { loadRegionDataBundle(context) }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedRegion by remember { mutableStateOf<Region?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        TopBarWithBackButton(
            title = "하우스 올리기",
            onBackClick = onBackClick
        )

        StepIndicator(currentStep = 2)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "자세한 정보를 입력해주세요.",
                color = Color(0xFF6C4DFF),
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(vertical = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                .clickable { showBottomSheet = true }
                .background(Color.White, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = selectedRegion?.let { "${it.sido} ${it.sigungu} ${it.dong}" } ?: "지역을 선택해주세요.",
                color = if (selectedRegion == null) Color.Gray else Color.Black,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("제목", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("제목을 입력해주세요.", color = Color(0xFFA5A5A5), fontSize = 12.sp) },
            modifier = Modifier
                .fillMaxWidth()
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
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("자세한 설명을 입력해주세요.", color = Color(0xFFA5A5A5), fontSize = 12.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("보증금", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
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
                                .padding(end = 8.dp),
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
                        .height(56.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
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
                                .padding(end = 8.dp),
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
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
                        .padding(end = 8.dp),
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

        Spacer(modifier = Modifier.height(12.dp))
        Text("입주 가능일", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = moveInDate,
            onValueChange = { moveInDate = it },
            placeholder = { Text("가능한 날짜 또는 시기를 작성해주세요.", color = Color(0xFFA5A5A5), fontSize = 12.sp) },
            modifier = Modifier
                .fillMaxWidth()
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
            onClick = onNextClick,
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

    if (showBottomSheet) {
        RegionBottomSheet(
            cities = regionBundle.cities,
            districtMap = regionBundle.districtMap,
            neighborhoodMap = regionBundle.neighborhoodMap,
            onRegionSelected = {
                selectedRegion = it
                showBottomSheet = false
            },
            onDismissRequest = {
                showBottomSheet = false
            }
        )
    }
}

fun readCsv(context: Context, fileName: String): List<Map<String, String>> {
    val inputStream = context.assets.open(fileName)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val headers = reader.readLine()?.split(",") ?: return emptyList()
    return reader.lineSequence().mapNotNull { line ->
        val values = line.split(",")
        if (values.size == headers.size) {
            headers.zip(values).toMap()
        } else null
    }.toList()
}

data class RegionDataBundle(
    val cities: List<String>,
    val districtMap: Map<String, List<String>>,
    val neighborhoodMap: Map<Pair<String, String>, List<String>>
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

    return RegionDataBundle(cities, districtMap, neighborhoodMap)
}