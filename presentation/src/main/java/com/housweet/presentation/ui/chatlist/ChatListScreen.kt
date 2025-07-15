package com.housweet.presentation.ui.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.housweet.domain.model.dummyChatList
import com.housweet.presentation.R
import androidx.compose.ui.graphics.Color


//UI 컴포저블
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        CenterAlignedTopAppBar(
            title={Text("채팅")},
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
        LazyColumn(
            modifier = Modifier.background(Color.White)
        ) {
            itemsIndexed(dummyChatList) { _, chat ->
                ChatListItem(chat = chat, onClick = {
                    navController.navigate("chat_detail/${chat.name}")
                })
            }
        }
    }
}