package com.housweet.presentation.ui.schedule.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import com.housweet.presentation.ui.navigation.BottomNavigation
import com.housweet.presentation.ui.theme.ColorGroup
import com.housweet.presentation.ui.common.ScheduleRow
import com.housweet.presentation.ui.common.TodoRow
import com.housweet.presentation.model.schedule.TaskType
import com.housweet.presentation.model.schedule.TaskItem
import com.housweet.presentation.model.schedule.OwnerTaskGroup
import com.housweet.presentation.model.schedule.ScheduleInfo
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.housweet.presentation.R

@Composable
fun ScheduleMainScreen(
    onMenuClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    scheduleInfo: ScheduleInfo?,
    allTasks: List<TaskItem>,
    currentMonth: YearMonth,
    onDateSelected: (LocalDate) -> Unit = {},
    onMonthChanged: (YearMonth) -> Unit = {},
    navController: NavController = rememberNavController()
) {
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        painter = painterResource(R.drawable.menu),
                        contentDescription = "메뉴",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onAddClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "추가",
                        tint = Color.Black
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp), // 바텀 네비게이션 높이만큼 추가 패딩
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 캘린더 섹션
                item {
                    CalendarSection(
                        currentMonth = currentMonth,
                        taskDates = allTasks,
                        scheduleInfo = scheduleInfo,
                        onDateSelected = onDateSelected,
                        onMonthChanged = onMonthChanged
                    )
                }
                
                // 날짜 표시 및 할일 목록
                item {
                    ScheduleListSection(
                        scheduleInfo = scheduleInfo
                    )
                }
            }
        }
        
        // 바텀 네비게이션
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavigation(navController = navController)
        }
    }
}

@Composable
fun CalendarSection(
    currentMonth: YearMonth,
    taskDates: List<TaskItem>,
    scheduleInfo: ScheduleInfo?,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(6.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 월/년 표시 with navigation arrows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onMonthChanged(currentMonth.minusMonths(1)) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar_prev),
                        contentDescription = "이전 달",
                        tint = ColorGroup.Gray_E2E2E2,
                        modifier = Modifier.size(9.dp),
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = currentMonth.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { onMonthChanged(currentMonth.plusMonths(1)) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar_next),
                        contentDescription = "다음 달",
                        tint = ColorGroup.Gray_E2E2E2,
                        modifier = Modifier.size(9.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 요일 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val dayHeaders = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
                dayHeaders.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 달력 그리드 - 현재 월만 표시
            CalendarGrid(
                currentMonth = currentMonth,
                taskDates = taskDates,
                scheduleInfo = scheduleInfo,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    taskDates: List<TaskItem>,
    scheduleInfo: ScheduleInfo?,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    // 월요일을 1로 시작하는 요일 계산 (1=월, 2=화, ..., 7=일)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value - 1 // 0=월요일, 6=일요일
    
    // 달력 그리드 생성 (주별로 정렬) - remember를 사용하여 재계산 방지
    val calendarDays = remember(currentMonth) {
        val weeks = mutableListOf<List<LocalDate?>>()
        var currentWeek = mutableListOf<LocalDate?>()
        
        // 첫 번째 주의 빈 공간 채우기 (월요일 시작)
        repeat(firstDayOfWeek) {
            currentWeek.add(null)
        }
        
        // 날짜들 추가
        for (day in 1..daysInMonth) {
            if (currentWeek.size == 7) {
                weeks.add(currentWeek.toList())
                currentWeek = mutableListOf()
            }
            currentWeek.add(currentMonth.atDay(day))
        }
        
        // 마지막 주의 빈 공간 채우기
        while (currentWeek.size < 7) {
            currentWeek.add(null)
        }
        weeks.add(currentWeek.toList())
        
        weeks
    }
    
    // isMine=true인 Task들만 필터링하여 날짜별로 그룹핑
    val myTasks = scheduleInfo?.ownerTaskGroups
        ?.filter { it.isMine }
        ?.flatMap { it.tasks } ?: emptyList()
    
    val todoTaskDates = myTasks.filter { it.taskType == TaskType.TODO }.map { it.date }.toSet()
    val scheduleTaskDates = myTasks.filter { it.taskType == TaskType.SCHEDULE }.map { it.date }.toSet()
    
    Column {
        calendarDays.forEachIndexed { weekIndex, week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                week.forEachIndexed { _, date ->
                    if (date != null) {
                        CalendarDay(
                            day = date.dayOfMonth,
                            isCurrentMonth = true,
                            hasTodoTask = todoTaskDates.contains(date),
                            hasScheduleTask = scheduleTaskDates.contains(date),
                            onClick = { onDateSelected(date) }
                        )
                    } else {
                        // 빈 공간 - 고정 크기로 레이아웃 안정성 확보
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
            // 주간 간격
            if (weekIndex < calendarDays.size - 1) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun CalendarDay(
    day: Int,
    isCurrentMonth: Boolean,
    hasTodoTask: Boolean,
    hasScheduleTask: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                when {
                    hasScheduleTask -> ColorGroup.Primary
                    else -> Color.Transparent
                }
            )
            .then(
                if (hasTodoTask) Modifier.border(1.dp, ColorGroup.Primary, CircleShape)
                else Modifier
            )
            .clickable(enabled = isCurrentMonth) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 16.sp,
            color = when {
                hasScheduleTask -> Color.White
                isCurrentMonth -> Color.Black
                else -> Color.Gray
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ScheduleListSection(
    scheduleInfo: ScheduleInfo?
) {
    Column {
        // 날짜 표시
        Text(
            text = scheduleInfo?.title ?: "날짜를 선택해주세요",
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 할일 그룹 목록
        val ownerTaskGroups = scheduleInfo?.ownerTaskGroups ?: emptyList()
        
        ownerTaskGroups.forEach { group ->
            // TaskType별로 그룹핑
            val tasksByType = group.tasks.groupBy { it.taskType }
            
            listOf(TaskType.SCHEDULE, TaskType.TODO).forEach { taskType ->
                val tasks = tasksByType[taskType]
                if (!tasks.isNullOrEmpty()) {
                    // 소유자 정보 헤더 표시
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .then(
                                if (!group.isMine) Modifier else Modifier
                            )
                    ) {
                        // 프로필 아이콘
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(ColorGroup.Primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = group.ownerName.take(1),
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${group.ownerName}의 ${if (taskType == TaskType.SCHEDULE) "일정" else "할 일"}",
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = if (!group.isMine) Modifier.alpha(0.5f) else Modifier
                        )
                    }
                    
                    // 해당 TaskType의 모든 항목들 표시
                    tasks.forEach { task ->
                        Box(
                            modifier = if (!group.isMine) Modifier.alpha(0.5f) else Modifier
                        ) {
                            when (taskType) {
                                TaskType.SCHEDULE -> {
                                    ScheduleRow(
                                        title = task.taskName
                                    )
                                }
                                TaskType.TODO -> {
                                    TodoRow(
                                        title = task.taskName,
                                        isCompleted = task.isCompleted
                                    )
                                }
                            }
                        }
                        
                        if (task != tasks.last()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


@Preview
@Composable
private fun ScheduleMainScreenPreview() {
    val sampleScheduleInfo = ScheduleInfo(
        id = 1,
        title = "03.01.TUE",
        date = "2024-03-01",
        ownerTaskGroups = listOf(
            OwnerTaskGroup(
                ownerName = "김지안님",
                isMine = true,
                tasks = listOf(
                    TaskItem(1, TaskType.SCHEDULE, "청소기 돌리기", false, LocalDate.now()),
                    TaskItem(2, TaskType.TODO, "설거지하기", true, LocalDate.now())
                )
            )
        )
    )
    
    val sampleTasks = listOf(
        TaskItem(1, TaskType.SCHEDULE, "청소기 돌리기", false, LocalDate.now().plusDays(1)),
        TaskItem(2, TaskType.TODO, "설거지하기", true, LocalDate.now().plusDays(2))
    )
    
    ScheduleMainScreen(
        scheduleInfo = sampleScheduleInfo,
        allTasks = sampleTasks,
        currentMonth = YearMonth.now(),
    )
}