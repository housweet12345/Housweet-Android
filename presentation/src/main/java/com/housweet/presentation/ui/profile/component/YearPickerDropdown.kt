package com.housweet.presentation.ui.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.housweet.presentation.ui.theme.ColorGroup
import java.util.Calendar

@Composable
fun YearPickerDropdown(
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (1900..currentYear).map { it.toString() }.reversed()

    Box(modifier = modifier) {
        // 드랍다운 트리거 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(6.dp),
                )
                .clip(RoundedCornerShape(6.dp))
                .border(
                    width = 0.2.dp,
                    color = if (enabled) ColorGroup.Gray_CBCBCB else ColorGroup.Gray_CBCBCB,
                    shape = RoundedCornerShape(6.dp)
                )
                .background(if (enabled) Color.White else ColorGroup.White_F8F8F8)
                .clickable(enabled = enabled) { expanded = true },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = if (selectedYear.isEmpty()) "태어난 년도를 선택해주세요." else selectedYear,
                color = if (selectedYear.isEmpty()) Color.Gray else if (enabled) Color.Black else ColorGroup.Gray_7E7E7E,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // 드랍다운 메뉴
        if (expanded) {
            Popup(
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                Card(
                    modifier = Modifier
                        .width(200.dp)
                        .height(190.dp)
                        .zIndex(1000f),
                    shape = RoundedCornerShape(6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    LazyColumn(
                        reverseLayout = true, // 리스트를 뒤집어서 현재 연도가 상단에 오도록
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(years) { year ->
                            Text(
                                text = year,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onYearSelected(year)
                                        expanded = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}