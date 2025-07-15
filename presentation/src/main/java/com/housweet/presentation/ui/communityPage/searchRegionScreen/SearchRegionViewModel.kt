package com.housweet.presentation.ui.communityPage.searchRegionScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.Coordinate
import com.housweet.domain.usecase.UseCases
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRegionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    private val regionUtils = RegionUtils(context = context)
    private val _uiState = MutableStateFlow<SearchRegionUiState>(SearchRegionUiState.Idle)
    val uiState: StateFlow<SearchRegionUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchRegionEvent>()
    val event: SharedFlow<SearchRegionEvent> = _event.asSharedFlow()

    fun geoCoding(address: String) {
        viewModelScope.launch {
            val coordinate = regionUtils.getCoordinateByAddress(address)
            if (coordinate.latitude == 0.0 && coordinate.longitude == 0.0) error()
            successGeoCoding(coordinate)
        }
    }

    fun getFullAddress(address: String) {
        viewModelScope.launch {
            val autoCompleteAddressList = regionUtils.getFullAddress(address)
            successAutoComplete(autoCompleteAddressList)
        }
    }

    fun isValidFullAddress(address: String) {
        viewModelScope.launch {
            val isValid = regionUtils.isValidFullAddress(address)
            addressIsValid(isValid)
        }
    }

    fun error() {
        viewModelScope.launch {
            _uiState.value = SearchRegionUiState.Idle
            _event.emit(SearchRegionEvent.Error)
        }
    }

    private suspend fun successGeoCoding(latLng: LatLng) {
        _event.emit(SearchRegionEvent.GeoCodingSuccess(latLng))
    }

    private suspend fun successAutoComplete(autoCompleteAddressList: List<String>) {
        _event.emit(SearchRegionEvent.AutoCompleteSuccess(autoCompleteAddressList))
    }

    private suspend fun addressIsValid(isValid: Boolean) {
        _event.emit(SearchRegionEvent.IsValidAddress(isValid))
    }
}