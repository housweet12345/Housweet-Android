package com.housweet.presentation.ui.startPage.accessRoomPage.searchRoomScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchRoomViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow<SearchRoomUiState>(SearchRoomUiState.IDlE)
    val uiState = _uiState.asStateFlow()

    private fun error() {
        _uiState.value = SearchRoomUiState.Error
    }

    private fun isLoading() {
        _uiState.value = SearchRoomUiState.IsLoading
    }
}