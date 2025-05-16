package com.housweet.presentation.ui.communityPage.searchRegionScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.Coordinate
import com.housweet.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRegionViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {
    private val _uiState = MutableStateFlow<SearchRegionUiState>(SearchRegionUiState.Idle)
    val uiState: StateFlow<SearchRegionUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchRegionEvent>()
    val event: SharedFlow<SearchRegionEvent> = _event.asSharedFlow()

    fun geoCodingWithNaver(query: String) {
        isLoading()
        viewModelScope.launch(Dispatchers.IO) {
            useCases.geoCodingWithNaverUseCase(query).collectLatest {
                it.onSuccess { coordinate ->
                    success(coordinate)
                }.onFailure {
                    isIdle()
                    error()
                }
            }
        }
    }

    private fun isIdle() {
        _uiState.value = SearchRegionUiState.Idle
    }

    fun error() {
        viewModelScope.launch {
            _uiState.value = SearchRegionUiState.Idle
            _event.emit(SearchRegionEvent.Error)
        }
    }

    private suspend fun success(coordinate: Coordinate) {
        _event.emit(SearchRegionEvent.Success(coordinate))
    }

    private fun isLoading() {
        _uiState.value = SearchRegionUiState.IsLoading
    }
}