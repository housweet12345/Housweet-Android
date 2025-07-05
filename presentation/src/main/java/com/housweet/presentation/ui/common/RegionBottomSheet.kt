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
    regions: List<Region>,
    onRegionSelected: (Region) -> Unit,
    onDismissRequest: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) } // 1: 시도, 2: 시군구, 3: 동
    var selectedSido by remember { mutableStateOf<String?>(null) }
    var selectedSigungu by remember { mutableStateOf<String?>(null) }

    val sidoList = regions.map { it.sido }.distinct()
    val sigunguList = regions.filter { it.sido == selectedSido }.map { it.sigungu }.distinct()
    val dongList = regions.filter { it.sido == selectedSido && it.sigungu == selectedSigungu }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f) // 최대 높이 제한해서 스크롤 가능하게
                .padding(16.dp)
        ) {

            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 1) {
                    Text(
                        text = "← 뒤로",
                        modifier = Modifier.clickable {
                            currentStep--
                        }
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
                Spacer(modifier = Modifier.width(48.dp)) // 균형 맞춤용
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 스크롤 가능한 목록
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                when (currentStep) {
                    1 -> {
                        items(sidoList) { sido ->
                            Text(
                                text = sido,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedSido = sido
                                        currentStep = 2
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }

                    2 -> {
                        items(sigunguList) { sigungu ->
                            Text(
                                text = sigungu,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedSigungu = sigungu
                                        currentStep = 3
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }

                    3 -> {
                        items(dongList) { region ->
                            Text(
                                text = region.dong,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onRegionSelected(region)
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