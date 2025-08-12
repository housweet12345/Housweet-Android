package com.housweet.presentation.ui.startPage.loginPage.termsOfServicePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsOfServiceViewModel @Inject constructor(
    private val useCase: UseCases
): ViewModel() {
    private val _uiState = MutableStateFlow<TermsOfServiceState>(TermsOfServiceState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TermsOfServiceEvent>()
    val event = _event.asSharedFlow()

    fun agreeTerms() {
        isLoading()
        viewModelScope.launch {
            useCase.agreeTermsOfServiceUseCase().collect {
                it.onSuccess {
                    success()
                }
                it.onFailure {
                    error()
                }
            }
        }
    }

    private suspend fun success() {
        _event.emit(TermsOfServiceEvent.Success)
    }

    private suspend fun error() {
        isIdle()
        _event.emit(TermsOfServiceEvent.Error)
    }

    private fun isIdle() {
        _uiState.value = TermsOfServiceState.Idle
    }

    private fun isLoading() {
        _uiState.value = TermsOfServiceState.IsLoading
    }
}