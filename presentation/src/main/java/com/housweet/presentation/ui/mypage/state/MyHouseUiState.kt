package com.housweet.presentation.ui.mypage.state

import com.housweet.domain.model.MyHouse

sealed interface MyHouseUiState {
    object Loading : MyHouseUiState
    data class Success(val data: MyHouse) : MyHouseUiState
    data class Error(val message: String) : MyHouseUiState
}