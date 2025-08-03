import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.housweet.presentation.R

data class Notification(
    val title: String,
    val message: String,
    val date: String
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    var notifications by remember {
        mutableStateOf(
            listOf(
                Notification("룸메이트 찾기", "문정동에서 룸메이트를 구하고 있어요", "오전 11:30"),
                Notification("마이 하우스", "'김지안'님께서 게시글을 작성했어요.", "오전 11:30"),
                Notification("알람 출력(?)", "'김지안'님께서 게시글을 작성했어요...", "1일 전")
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title={
                    Text(
                        text = "알림",
                        fontSize = 14.sp
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.back_black),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { navController.popBackStack() }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // ✅ 배경색 흰색 지정
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(
                items = notifications,
                key = { it.hashCode() } // 혹은 it.title + it.message 도 가능
            ) { notification ->
                val dismissState = rememberDismissState(
                    confirmStateChange = { dismissValue ->
                        if (dismissValue == DismissValue.DismissedToStart || dismissValue == DismissValue.DismissedToEnd) {
                            notifications = notifications.toMutableList().apply {
                                remove(notification)
                            }
                            true
                        } else false
                    }
                )

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    },
                    dismissContent = {
                        NotificationItem(notification = notification)
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = notification.title,
            color = Color(0xFF665ED3),
            fontSize = 10.sp
        )
        Text(
            text = notification.message,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = notification.date,
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
        )
        Divider(modifier = Modifier.padding(top = 12.dp))
    }
}
