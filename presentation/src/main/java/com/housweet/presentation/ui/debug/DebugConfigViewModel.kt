package com.housweet.presentation.ui.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.debug.GetDebugConfigUseCase
import com.housweet.domain.usecase.debug.SetDebugConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DebugConfigUiState(
    val currentBaseUrl: String = "",
    val currentUserBaseUrl: String = "",
    val isDebugMode: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = true
)

@HiltViewModel
class DebugConfigViewModel @Inject constructor(
    private val getDebugConfigUseCase: GetDebugConfigUseCase,
    private val setDebugConfigUseCase: SetDebugConfigUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DebugConfigUiState())
    val uiState: StateFlow<DebugConfigUiState> = _uiState.asStateFlow()

    init {
        loadCurrentConfig()
    }

    private fun loadCurrentConfig() {
        viewModelScope.launch {
            try {
                val currentBaseUrl = getDebugConfigUseCase.getCurrentBaseUrl()
                val currentUserBaseUrl = getDebugConfigUseCase.getCurrentUserBaseUrl()
                val isDebugMode = getDebugConfigUseCase.isDebugMode()

                _uiState.value = _uiState.value.copy(
                    currentBaseUrl = currentBaseUrl,
                    currentUserBaseUrl = currentUserBaseUrl,
                    isDebugMode = isDebugMode,
                    message = ""
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Failed to load current configuration: ${e.message}",
                    isSuccess = false
                )
            }
        }
    }

    fun setBaseUrl(url: String) {
        if (url.trim().isEmpty()) return

        viewModelScope.launch {
            try {
                setDebugConfigUseCase.setBaseUrl(url.trim())
                loadCurrentConfig()
                _uiState.value = _uiState.value.copy(
                    message = "Base URL updated successfully",
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Failed to update base URL: ${e.message}",
                    isSuccess = false
                )
            }
        }
    }

    fun setUserBaseUrl(url: String) {
        if (url.trim().isEmpty()) return

        viewModelScope.launch {
            try {
                setDebugConfigUseCase.setUserBaseUrl(url.trim())
                loadCurrentConfig()
                _uiState.value = _uiState.value.copy(
                    message = "User Base URL updated successfully",
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Failed to update user base URL: ${e.message}",
                    isSuccess = false
                )
            }
        }
    }

    fun clearDebugUrls() {
        viewModelScope.launch {
            try {
                setDebugConfigUseCase.clearDebugUrls()
                loadCurrentConfig()
                _uiState.value = _uiState.value.copy(
                    message = "Debug URLs cleared successfully",
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "Failed to clear debug URLs: ${e.message}",
                    isSuccess = false
                )
            }
        }
    }
}