package com.housweet.presentation.ui.home.state

import com.housweet.presentation.model.home.HomeInfo

sealed interface HomeState {
    data object Loading : HomeState
    data class Success(val homeInfo: HomeInfo) : HomeState
    data class Error(val message: String) : HomeState
}