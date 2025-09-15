package com.housweet.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.model.Region
import com.housweet.presentation.ui.communityPage.searchRegionScreen.RegionUtils
import com.housweet.presentation.ui.theme.Gray_A5A5A5
import com.housweet.presentation.ui.theme.White_F8F8F8

@Composable
fun RegionDialog(
    onRegionSelected: (Region) -> Unit,
    onDismissRequest: () -> Unit,
    citiesWithCodes: List<Map<String, String>>,  // 시 정보
    districtsWithCodes: List<Map<String, String>>, // 구 정보
    neighborhoodsWithCodes: List<Map<String, String>> // 동 정보
) {
    fun norm(s: String?) = s?.replace("\uFEFF", "")?.trim().orEmpty()

    val context = LocalContext.current

    var selectedCityIndex by remember { mutableIntStateOf(0) }
    var selectedDistrictIndex by remember { mutableIntStateOf(0) }
    var selectedNeighborhoodIndex by remember { mutableIntStateOf(0) }

    val districtsScrollState = rememberLazyListState()
    val neighborhoodsScrollState = rememberLazyListState()

    LaunchedEffect(selectedCityIndex) {
        districtsScrollState.animateScrollToItem(0)
        neighborhoodsScrollState.animateScrollToItem(0)
    }

    // 구가 변경될 때 동 스크롤을 맨 위로 이동
    LaunchedEffect(selectedDistrictIndex) {
        neighborhoodsScrollState.animateScrollToItem(0)
    }

    val cities by remember { mutableStateOf(RegionUtils(context).getDtgList()) }
    var districts by remember { mutableStateOf(RegionUtils(context).getSggList(cities[0])) }
    var neighborhoods by remember { mutableStateOf(RegionUtils(context).getDymList(cities[0], districts[0])) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray_A5A5A5.copy(alpha = 0.375f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismissRequest() }
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .height(256.dp),
            shape = RoundedCornerShape(6.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(cities) { index, city ->
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                districts = RegionUtils(context).getSggList(city)
                                neighborhoods = RegionUtils(context).getDymList(city, districts[0])
                                selectedCityIndex = index
                                selectedDistrictIndex = 0
                                selectedNeighborhoodIndex = 0
                            }
                            .background(
                                if (selectedCityIndex == index) White_F8F8F8
                                else Color.White
                            )
                        ) {
                            GuideText(
                                modifier = Modifier.padding(8.dp),
                                text = city,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = districtsScrollState
                ) {
                    itemsIndexed(districts) { index, district ->
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                neighborhoods = RegionUtils(context).getDymList(cities[selectedCityIndex], district)
                                selectedDistrictIndex = index
                                selectedNeighborhoodIndex = 0
                            }
                            .background(
                                if (selectedDistrictIndex == index) White_F8F8F8
                                else Color.White
                            )
                        ) {
                            GuideText(
                                modifier = Modifier.padding(8.dp),
                                text = district,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = neighborhoodsScrollState
                ) {
                    itemsIndexed(neighborhoods) { index, neighborhood ->
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedNeighborhoodIndex = index
                                val city = norm(cities[selectedCityIndex])
                                val district = norm(districts[selectedDistrictIndex])

                                val siCode: String = citiesWithCodes.firstOrNull {
                                    norm(it["name"]) == city
                                }?.let { norm(it["code"]) }
                                    ?.takeIf { it.isNotEmpty() }
                                    ?: return@clickable

                                val guCode: String = districtsWithCodes.firstOrNull {
                                    norm(it["si__name"]) == city && norm(it["name"]) == district
                                }?.let { norm(it["code"]) }
                                    ?.takeIf { it.isNotEmpty() }
                                    ?: return@clickable

                                val dongCode: String = neighborhoodsWithCodes.firstOrNull {
                                    norm(it["si__name"]) == city &&
                                            norm(it["gu__name"]) == district &&
                                            norm(it["name"]) == neighborhood
                                }?.let { norm(it["code"]) }
                                    ?.takeIf { it.isNotEmpty() }
                                    ?: return@clickable

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

                                onDismissRequest()
                            }
                        ) {
                            GuideText(
                                modifier = Modifier.padding(8.dp),
                                text = neighborhood,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }
            }
        }
    }
}