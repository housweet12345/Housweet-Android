package com.housweet.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.housweet.presentation.model.Region

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionBottomSheet(
    cities: List<String>,
    districtMap: Map<String, List<String>>,
    neighborhoodMap: Map<Pair<String, String>, List<String>>,
    onRegionSelected: (Region) -> Unit,
    onDismissRequest: () -> Unit,
    citiesWithCodes: List<Map<String, String>>,  // 시 정보
    districtsWithCodes: List<Map<String, String>>, // 구 정보
    neighborhoodsWithCodes: List<Map<String, String>>, // 동 정보
) {
    var currentStep by remember { mutableStateOf(1) }
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var selectedDistrict by remember { mutableStateOf<String?>(null) }

    fun norm(s: String?) = s?.replace("\uFEFF", "")?.trim().orEmpty()

//    val districtList = selectedCity?.let { districtMap[it] } ?: emptyList()
//    val neighborhoodList = selectedCity?.let { city ->
//        selectedDistrict?.let { district ->
//            neighborhoodMap[city to district]
//        }
//    } ?: emptyList()

    val districtList = selectedCity?.let { city ->
        districtMap[norm(city)] ?: emptyList()
    } ?: emptyList()

    val neighborhoodList = selectedCity?.let { city ->
        selectedDistrict?.let { district ->
            neighborhoodMap[norm(city) to norm(district)]
        }
    } ?: emptyList()

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 1) {
                    Text(
                        text = "← 뒤로",
                        modifier = Modifier.clickable { currentStep-- }
                    )
                }
                Text(
                    text = when (currentStep) {
                        1 -> "시/도 선택"
                        2 -> "시/군/구 선택"
                        3 -> "동 선택"
                        else -> ""
                    },
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(48.dp)) // 헤더 정렬용
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 리스트
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                when (currentStep) {
                    1 -> {
                        items(cities) { city ->
                            Text(
                                text = city,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCity = city
                                        selectedDistrict = null
                                        currentStep = 2
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }

                    2 -> {
                        items(districtList) { rawDistrict ->
                            val district = norm(rawDistrict)
                            Text(
                                text = district,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedDistrict = district
                                        currentStep = 3
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }

                    3 -> {
                        items(neighborhoodList ?: emptyList()) { rawNeighborhood ->
                            val neighborhood = norm(rawNeighborhood)
                            Text(
                                text = neighborhood,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val city = norm(selectedCity)
                                        val district = norm(selectedDistrict)

                                        // 이름 → 코드(Int) 매핑
                                        val siCode = citiesWithCodes.firstOrNull {
                                            norm(it["name"]) == city
                                        }?.let { norm(it["code"]).toIntOrNull() } ?: return@clickable

                                        val guCode = districtsWithCodes.firstOrNull {
                                            norm(it["si__name"]) == city && norm(it["name"]) == district
                                        }?.let { norm(it["code"]).toIntOrNull() } ?: return@clickable

                                        val dongCode = neighborhoodsWithCodes.firstOrNull {
                                            norm(it["si__name"]) == city &&
                                                    norm(it["gu__name"]) == district &&
                                                    norm(it["name"]) == neighborhood
                                        }?.let { norm(it["code"]).toLongOrNull() } ?: return@clickable

                                        // Region 조립 (코드는 Int)
                                        onRegionSelected(
                                            Region(
                                                sido = city,
                                                sigungu = district,
                                                dong = neighborhood,
                                                sidoCode = siCode,
                                                sigunguCode = guCode,
                                                dongCode = dongCode
                                            )
                                        )
//                                        selectedCity?.let { city ->
//                                            selectedDistrict?.let { district ->
//                                                onRegionSelected(
//                                                    Region(
//                                                        sido = city,
//                                                        sigungu = district,
//                                                        dong = neighborhood,
//                                                        sidoCode = citiesWithCodes.find { it["name"] == city }?.get("code") ?: "",
//                                                        sigunguCode = districtsWithCodes.find {
//                                                            it["si__name"] == city && it["name"] == district
//                                                        }?.get("code") ?: "",
//                                                        dongCode = neighborhoodsWithCodes.find {
//                                                            it["si__name"] == city && it["gu__name"] == district && it["name"] == neighborhood
//                                                        }?.get("code") ?: ""
//                                                    )
//                                                )
//
//                                            }
//                                        }
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}