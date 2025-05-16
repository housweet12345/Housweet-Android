package com.housweet.presentation.ui.communityPage.searchRegionScreen

import com.housweet.domain.model.Coordinate

sealed interface SearchRegionUiState {
    data object Idle : SearchRegionUiState
    data object IsLoading : SearchRegionUiState
}

sealed interface SearchRegionEvent {
    data class Success(val coordinate: Coordinate) : SearchRegionEvent
    data object Error : SearchRegionEvent
}