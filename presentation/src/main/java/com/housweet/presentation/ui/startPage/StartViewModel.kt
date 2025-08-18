package com.housweet.presentation.ui.startPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    fun logout(
        onLogout: () -> Unit
    ) {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                return@logout
            }

            viewModelScope.launch {
                useCases.logoutUseCase()
                onLogout()
            }
        }
    }

    fun deleteAccount(
        onDeleteAccount: () -> Unit
    ) {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                return@unlink
            }

            viewModelScope.launch {
                onDeleteAccount()
            }
        }
    }
}