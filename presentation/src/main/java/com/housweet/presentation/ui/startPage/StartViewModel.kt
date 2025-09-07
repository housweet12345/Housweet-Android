package com.housweet.presentation.ui.startPage

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
        onLogout: () -> Unit
    ) {
        UserApiClient.instance.logout {
            viewModelScope.launch {
                logoutUseCase()
                onLogout()
            }
        }
    }
}