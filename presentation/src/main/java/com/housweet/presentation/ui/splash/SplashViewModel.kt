package com.housweet.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    private val _isLogin = MutableSharedFlow<Boolean>(replay = 1)
    val isLogin = _isLogin.asSharedFlow()

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            useCases.checkLoginUseCase().collectLatest {
                delay(1000)
                it.onSuccess { isAutoLogin ->
                    _isLogin.emit(isAutoLogin)
                }
                it.onFailure { e ->
                    e.printStackTrace()
                    _isLogin.emit(false)
                }
            }
        }
    }
}