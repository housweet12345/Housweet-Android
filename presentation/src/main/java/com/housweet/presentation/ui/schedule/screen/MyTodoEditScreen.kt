package com.housweet.presentation.ui.schedule.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.housweet.presentation.R
import com.housweet.presentation.ui.theme.ColorGroup
import com.housweet.presentation.model.schedule.EditableTodoInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTodoEditScreen(
    currentTodos: List<EditableTodoInfo>,
    pastTodos: List<EditableTodoInfo>,
    onBackClick: () -> Unit = {},
    onTodoClick: (EditableTodoInfo) -> Unit = {},
    onAddTodo: () -> Unit = {},
    onDeleteTodo: () -> Unit = {}
) {
    var showDropdownMenu by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "나의 할 일 목록",
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
                    IconButton(onClick = { showDropdownMenu = !showDropdownMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "메뉴",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(0.dp)
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 할 일 고정 헤더
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(
                                color = ColorGroup.Primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(start = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "할 일",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
                
                // 현재 할 일 항목들
                items(currentTodos.size) { index ->
                    ShadowRow(
                        text = currentTodos[index].title,
                        onClick = { onTodoClick(currentTodos[index]) }
                    )
                }
                
                // 지난 일정 섹션
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "지난 일정",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
                
                // 지난 일정 헤더
                if (pastTodos.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .background(
                                    color = ColorGroup.Primary.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(start = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "지난 할 일",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                    
                    // 지난 할 일 항목들
                    items(pastTodos.size) { index ->
                        ShadowRow(
                            text = pastTodos[index].title,
                            onClick = { onTodoClick(pastTodos[index]) }
                        )
                    }
                }
            }
        }
        
        // Dropdown menu positioned outside the AppBar
        if (showDropdownMenu) {
            CustomDropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { showDropdownMenu = false },
                onAddTodo = {
                    onAddTodo()
                    showDropdownMenu = false
                },
                onDeleteTodo = {
                    onDeleteTodo()
                    showDropdownMenu = false
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 56.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onAddTodo: () -> Unit,
    onDeleteTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (expanded) {
        // Invisible background that captures touches outside the dropdown
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { onDismissRequest() }
                }
        )
        
        Card(
            modifier = modifier
                .width(160.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "할 일 추가하기",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddTodo() }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
                
                Text(
                    text = "할 일 삭제하기",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDeleteTodo() }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
fun ShadowRow(
    text: String,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick ?: {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun MyTodoEditScreenPreview() {
    val sampleCurrentTodos = listOf(
        EditableTodoInfo(1, "설거지하기", isCompleted = false, isPast = false),
        EditableTodoInfo(2, "분리수거하기", isCompleted = true, isPast = false)
    )
    val samplePastTodos = listOf(
        EditableTodoInfo(3, "청소기 돌리기", isCompleted = true, isPast = true),
        EditableTodoInfo(4, "빨래하기", isCompleted = true, isPast = true)
    )
    
    MyTodoEditScreen(
        currentTodos = sampleCurrentTodos,
        pastTodos = samplePastTodos
    )
}