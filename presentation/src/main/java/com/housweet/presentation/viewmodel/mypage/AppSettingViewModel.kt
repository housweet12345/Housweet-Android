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

        // "알림 켜기" id 찾아서 설정
        val globalSettingId = appSettings
            .flatMap { it.settings }
            .find { it.key == "global_notifications_on_off" }
            ?.id

        if (globalSettingId != null) {
            updateSetting(globalSettingId, value)
        } else {
            Log.e("AppSettingViewModel", "global_notifications_on_off 설정을 찾을 수 없습니다.")
        }
    }
}