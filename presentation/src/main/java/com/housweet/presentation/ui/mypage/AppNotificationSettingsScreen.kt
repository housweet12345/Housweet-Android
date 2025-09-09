package com.housweet.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.housweet.domain.model.AppSettingItem
import com.housweet.presentation.ui.common.TopBar
import com.housweet.presentation.viewmodel.mypage.AppSettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNotificationSettingsScreen(
    navController: NavController
) {
    val viewModel: AppSettingViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.fetchAppSettings()
    }

    val appSettings = viewModel.appSettings

    val isNotificationOn by viewModel.isNotificationOn

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBar(
                text = "앱 설정",
                onBackBtnClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp, top = 16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.Start
        ) {

            // 알림 켜기 토글
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "알림 켜기", fontSize = 14.sp)
                Switch(
                    checked = isNotificationOn,
                    onCheckedChange = {
                        viewModel.setNotificationToggle(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFF8F8F8),
                        checkedTrackColor = Color(0xFF665ED3),
                        uncheckedThumbColor = Color(0xFFF8F8F8),
                        uncheckedTrackColor = Color(0xFFD9D9D9)
                    )
                )
            }

            // 세부 알림 토글들
            if (isNotificationOn) {
                val generalCategory = appSettings.find { it.categoryName == "일반" }
                val roommateCategory = appSettings.find { it.categoryName == "하우스메이트" }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "일반", fontSize = 12.sp, color = Color.Gray)
                generalCategory?.settings?.forEach { setting ->
                    SettingToggleItem(
                        setting = setting,
                        onToggle = { isChecked ->
                            viewModel.updateSetting(
                                settingId = setting.id,
                                isEnabled = isChecked
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "하우스메이트", fontSize = 12.sp, color = Color.Gray)
                roommateCategory?.settings?.forEach { setting ->
                    SettingToggleItem(
                        setting = setting,
                        onToggle = { isChecked ->
                            viewModel.updateSetting(
                                settingId = setting.id,
                                isEnabled = isChecked
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingToggleItem(
    setting: AppSettingItem,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = setting.displayName, fontSize = 14.sp)
        Switch(
            checked = setting.isEnabled,
            onCheckedChange = { onToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFF8F8F8),
                checkedTrackColor = Color(0xFF665ED3),
                uncheckedThumbColor = Color(0xFFF8F8F8),
                uncheckedTrackColor = Color(0xFFD9D9D9)
            )
        )
    }
}