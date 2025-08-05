package com.housweet.presentation.viewmodel.mypage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.AppSettingCategory
import com.housweet.domain.repository.AppSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingViewModel @Inject constructor(
    private val repository: AppSettingRepository
) : ViewModel() {

    var appSettings by mutableStateOf<List<AppSettingCategory>>(emptyList())
        private set

    var isNotificationOn = mutableStateOf(true)
        private set

    fun fetchAppSettings() {
        viewModelScope.launch {
            try {
                val result = repository.getAppSettings()
                appSettings = result
                isNotificationOn.value = result.any { category ->
                    category.settings.any { it.key == "global_notifications_on_off" && it.isEnabled }
                }
            } catch (e: Exception) {
                Log.e("AppSettingViewModel", "앱 설정 조회 실패", e)
            }
        }
    }

    fun updateSetting(settingId: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                val updated = repository.updateAppSetting(settingId, isEnabled)

                // appSettings 업데이트
                appSettings = appSettings.map { category ->
                    category.copy(settings = category.settings.map {
                        if (it.id == settingId) it.copy(isEnabled = isEnabled) else it
                    })
                }

                // 전체 알림이라면 토글 상태도 갱신
                if (updated.key == "global_notifications_on_off") {
                    isNotificationOn.value = isEnabled
                }

            } catch (e: Exception) {
                Log.e("AppSettingViewModel", "알림 설정 변경 실패", e)
            }
        }
    }

    fun setNotificationToggle(value: Boolean) {
        isNotificationOn.value = value

        viewModelScope.launch {
            try {
                // 1. global_notifications_on_off 설정 변경
                val globalSetting = appSettings
                    .flatMap { it.settings }
                    .find { it.key == "global_notifications_on_off" }

                globalSetting?.let {
                    updateSetting(it.id, value)
                }

                // 2. 만약 알림을 끄는 경우 → 모든 세부 알림도 Off
                if (!value) {
                    appSettings.forEach { category ->
                        category.settings.forEach { setting ->
                            // global_notifications_on_off는 제외하고 전부 끄기
                            if (setting.key != "global_notifications_on_off" && setting.isEnabled) {
                                updateSetting(setting.id, isEnabled = false)
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("AppSettingViewModel", "알림 토글 처리 실패", e)
            }
        }
    }
}