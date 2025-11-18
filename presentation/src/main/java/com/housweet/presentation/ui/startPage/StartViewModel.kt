package com.housweet.presentation.ui.startPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.LogoutUseCase
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
): ViewModel() {
    fun logout(
        onLogout: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("StartViewModel", "Kakao logout failed", error)
                onError("로그아웃에 실패했습니다")
            } else {
                viewModelScope.launch {
                    try {
                        logoutUseCase()
                        onLogout()
                    } catch (e: Exception) {
                        Log.e("StartViewModel", "Server logout failed", e)
                        onError("로그아웃에 실패했습니다")
                    }
                }
            }
        }
    }
}