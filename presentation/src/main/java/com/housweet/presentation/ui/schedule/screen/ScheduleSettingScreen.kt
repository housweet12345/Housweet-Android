package com.housweet.presentation.ui.schedule.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.ColorGroup
import com.housweet.presentation.model.schedule.TaskInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSettingScreen(
    taskInfo: TaskInfo,
    isEditMode: Boolean = false,
    onBackClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onCompleteClick: () -> Unit = {},
    onTaskInfoUpdate: (TaskInfo) -> Unit = {}
) {
    var title by remember { mutableStateOf(taskInfo.title) }
    var selectedDayOfWeek by remember { mutableStateOf(taskInfo.selectedDayOfWeek) }
    var isRecurringSettingOpen by remember { mutableStateOf(false) }
    var selectedRecurringOption by remember { mutableStateOf(taskInfo.recurringOption) }
    var isDatePickerOpen by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf(taskInfo.dateRange) }
    var isScheduleModeEnabled by remember { mutableStateOf(taskInfo.isScheduleMode) }
    var isNotificationEnabled by remember { mutableStateOf(taskInfo.isNotificationEnabled) }
    var memo by remember { mutableStateOf(taskInfo.memo) }
    var isCompleted by remember { mutableStateOf(taskInfo.isCompleted) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = if (isEditMode) "할 일 수정" else "할 일 추가",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.arrowback),
                        contentDescription = "뒤로가기",
                        tint = Color.Black
                    )
                }
            },
            actions = {
                if (isEditMode) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "삭제",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    // 추가 모드일 때 빈 공간으로 중앙 정렬 유지
                    IconButton(onClick = {}, enabled = false) {
                        // 투명한 빈 아이콘으로 공간 확보
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            windowInsets = WindowInsets(0.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 제목 입력
            item {
                TitleSection(
                    title = title,
                    onTitleChange = { title = it }
                )
            }

            // 요일 선택 (일정 모드가 아닐 때만 표시)
            if (!isScheduleModeEnabled) {
                item {
                    DayOfWeekSection(
                        selectedDay = selectedDayOfWeek,
                        onDaySelected = { selectedDayOfWeek = it }
                    )
                }
            }

            if (!isCompleted) {
                // 반복 설정 (일정 모드가 아닐 때만 표시)
                if (!isScheduleModeEnabled) {
                    item {
                        RecurringSection(
                            selectedOption = selectedRecurringOption,
                            onClick = { isRecurringSettingOpen = true }
                        )
                    }
                }

                // 일정으로 변경
                item {
                    ScheduleModeSection(
                        isEnabled = isScheduleModeEnabled,
                        dateRange = selectedDateRange,
                        onClick = {
                            if (!isScheduleModeEnabled) {
                                isDatePickerOpen = true
                            }
                        },
                        onCancel = {
                            isScheduleModeEnabled = false
                            selectedDateRange = ""
                        }
                    )
                }

                // 알림
                item {
                    NotificationSection(
                        isEnabled = isNotificationEnabled,
                        onToggle = { isNotificationEnabled = it }
                    )
                }

                // 메모
                item {
                    MemoSection(
                        memo = memo,
                        onMemoChange = { memo = it }
                    )
                }
            } else {
                // 완료된 상태에서는 날짜 범위만 표시
                item {
                    CompletedScheduleSection(dateRange = selectedDateRange)
                }

                // 알림 (완료 상태)
                item {
                    NotificationSection(
                        isEnabled = isNotificationEnabled,
                        onToggle = { isNotificationEnabled = it }
                    )
                }

                // 메모 (완료 상태)
                item {
                    MemoSection(
                        memo = memo,
                        onMemoChange = { memo = it }
                    )
                }
            }
        }

        // 완료 버튼
        Button(
            onClick = {
                val updatedTaskInfo = taskInfo.copy(
                    title = title,
                    selectedDayOfWeek = selectedDayOfWeek,
                    recurringOption = selectedRecurringOption,
                    dateRange = selectedDateRange,
                    isScheduleMode = isScheduleModeEnabled,
                    isNotificationEnabled = isNotificationEnabled,
                    memo = memo,
                    isCompleted = true
                )
                onTaskInfoUpdate(updatedTaskInfo)
                onCompleteClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            colors = ButtonDefaults.buttonColors(
                containerColor = ColorGroup.Primary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "완료",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }

    // 반복 설정 다이얼로그
    if (isRecurringSettingOpen) {
        RecurringSettingDialog(
            selectedOption = selectedRecurringOption,
            onOptionSelected = {
                selectedRecurringOption = it
                isRecurringSettingOpen = false
            },
            onDismiss = { isRecurringSettingOpen = false }
        )
    }

    // 날짜 선택 다이얼로그  
    if (isDatePickerOpen) {
        DateRangePickerDialog(
            onDateRangeSelected = { range ->
                selectedDateRange = range
                isScheduleModeEnabled = true
                isDatePickerOpen = false
            },
            onDismiss = { isDatePickerOpen = false }
        )
    }
}

@Composable
fun TitleSection(
    title: String,
    onTitleChange: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "제목",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_noti_caution),
                    contentDescription = "정보",
                    tint = ColorGroup.Primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "할 일 목록은 10자 제한이 있습니다.",
                    fontSize = 12.sp,
                    color = ColorGroup.Primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            TextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = {
                    Text(
                        text = "할 일을 입력해주세요.",
                        color = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun DayOfWeekSection(
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    Column {
        Text(
            text = "요일 선택",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val days = listOf("월", "화", "수", "목", "금", "토", "일")
            days.forEach { day ->
                DayButton(
                    day = day,
                    isSelected = day == selectedDay,
                    onClick = { onDaySelected(day) }
                )
            }
        }
    }
}

@Composable
fun DayButton(
    day: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() },
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ColorGroup.Primary else Color.White
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RecurringSection(
    selectedOption: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_schedule_loop),
                contentDescription = "반복 설정",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "반복 설정",
                fontSize = 12.sp,
                color = Color.Black
            )
        }

        Text(
            text = selectedOption,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ScheduleModeSection(
    isEnabled: Boolean,
    dateRange: String,
    onClick: () -> Unit,
    onCancel: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    if (!isEnabled) onClick()
                }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_schedule),
                contentDescription = "일정",
                tint = if (isEnabled) ColorGroup.Primary else Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "일정으로 변경",
                fontSize = 16.sp,
                color = if (isEnabled) ColorGroup.Primary else Color.Black
            )
        }

        if (isEnabled && dateRange.isNotEmpty()) {
            Text(
                text = "취소",
                fontSize = 14.sp,
                color = ColorGroup.Primary,
                modifier = Modifier.clickable { onCancel() }
            )
        }
    }

    if (isEnabled && dateRange.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dateRange,
            fontSize = 14.sp,
            color = ColorGroup.Gray_7E7E7E,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CompletedScheduleSection(dateRange: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "일정",
            tint = ColorGroup.Primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "일정으로 변경",
            fontSize = 16.sp,
            color = ColorGroup.Primary
        )
    }

    if (dateRange.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dateRange,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 28.dp)
        )
    }
}

@Composable
fun NotificationSection(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "알림",
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "할 일은 나에게만, 일정은 모두에게 알림이 갑니다.",
                fontSize = 12.sp,
                color = ColorGroup.Primary
            )
        }

        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ColorGroup.Primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}

@Composable
fun MemoSection(
    memo: String,
    onMemoChange: (String) -> Unit
) {
    Column {
        Text(
            text = "메모",
            fontSize = 16.sp,
            color = Color.Black
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_noti_caution),
                contentDescription = "정보",
                tint = ColorGroup.Primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "메모는 1000자 제한이 있습니다.",
                fontSize = 12.sp,
                color = ColorGroup.Primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            TextField(
                value = memo,
                onValueChange = onMemoChange,
                placeholder = {
                    Text(
                        text = "메모를 작성하는 곳입니다.",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = false,
                maxLines = 5
            )
        }
    }
}

@Composable
fun RecurringSettingDialog(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                val options = listOf("반복 안함", "1주마다 반복", "2주마다 반복", "3주마다 반복", "4주마다 반복")

                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = option == selectedOption,
                                onClick = { onOptionSelected(option) }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = ColorGroup.Primary,
                                unselectedColor = Color.Gray
                            )
                        )
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateRangePickerDialog(
    onDateRangeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val today = java.time.LocalDate.now()
    val tomorrow = today.plusDays(1)
    
    var startDate by remember { mutableStateOf("${today.year} ${"%02d".format(today.monthValue)} ${"%02d".format(today.dayOfMonth)}") }
    var endDate by remember { mutableStateOf("${tomorrow.year} ${"%02d".format(tomorrow.monthValue)} ${"%02d".format(tomorrow.dayOfMonth)}") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                // 시작일과 마지막일 섹션
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 시작일
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "시작일",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        DateSelector(
                            date = startDate,
                            onDateChange = { startDate = it }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 마지막일
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "마지막일",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        DateSelector(
                            date = endDate,
                            onDateChange = { endDate = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                // 완료 버튼
                Button(
                    onClick = {
                        val startFormatted = startDate.replace(" ", ".")
                        val endFormatted = endDate.replace(" ", ".")
                        onDateRangeSelected("$startFormatted - $endFormatted")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorGroup.Basic
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "완료",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DateSelector(
    date: String,
    onDateChange: (String) -> Unit
) {
    val parts = date.split(" ")
    var year by remember { mutableStateOf(parts[0].toInt()) }
    var month by remember { mutableStateOf(parts[1].toInt()) }
    var day by remember { mutableStateOf(parts[2].toInt()) }

    // 날짜 변경시 콜백 호출
    fun updateDate() {
        val formattedDate = String.format("%04d %02d %02d", year, month, day)
        onDateChange(formattedDate)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 년도 선택기
        DateNumberSelector(
            value = year,
            onValueChange = {
                year = it
                updateDate()
            },
            minValue = 2020,
            maxValue = 2030
        )

        // 월 선택기
        DateNumberSelector(
            value = month,
            onValueChange = {
                month = it
                updateDate()
            },
            minValue = 1,
            maxValue = 12
        )

        // 일 선택기
        DateNumberSelector(
            value = day,
            onValueChange = {
                day = it
                updateDate()
            },
            minValue = 1,
            maxValue = 31
        )
    }
}

@Composable
fun DateNumberSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 위쪽 화살표
        IconButton(
            onClick = {
                if (value < maxValue) {
                    onValueChange(value + 1)
                }
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_gray_up),
                contentDescription = "증가",
                tint = ColorGroup.Gray_E2E2E2,
                modifier = Modifier.size(8.dp)
            )
        }

        // 숫자 표시
        Text(
            text = String.format("%02d", value),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // 아래쪽 화살표
        IconButton(
            onClick = {
                if (value > minValue) {
                    onValueChange(value - 1)
                }
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_gray_down),
                contentDescription = "감소",
                tint = ColorGroup.Gray_E2E2E2,
                modifier = Modifier.size(8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TaskSettingScreenPreview() {
    val sampleTaskInfo = TaskInfo(
        id = 1,
        title = "청소기 돌리기",
        selectedDayOfWeek = "금",
        recurringOption = "1주마다 반복",
        dateRange = "",
        isScheduleMode = false,
        isNotificationEnabled = true,
        memo = "",
        isCompleted = false
    )
    TaskSettingScreen(taskInfo = sampleTaskInfo)
}
