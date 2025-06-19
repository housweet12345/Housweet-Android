package com.housweet.presentation.ui.communityPage.searchRegionScreen

import com.naver.maps.geometry.LatLng

sealed interface SearchRegionUiState {
    data object Idle : SearchRegionUiState
}

sealed interface SearchRegionEvent {
    data class AutoCompleteSuccess(val addressList: List<String>) : SearchRegionEvent
    data class GeoCodingSuccess(val latLng: LatLng) : SearchRegionEvent
    data class IsValidAddress(val isValid: Boolean) : SearchRegionEvent
    data object Error : SearchRegionEvent
}