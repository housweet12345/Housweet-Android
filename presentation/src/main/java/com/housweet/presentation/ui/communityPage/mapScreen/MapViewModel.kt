package com.housweet.presentation.ui.communityPage.mapScreen

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.community.NearByPostCountDataModel
import com.housweet.domain.usecase.community.GetNearbyPostCountUseCase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getNearbyPostCountUseCase: GetNearbyPostCountUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Idle)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MapEvent>()
    val event: SharedFlow<MapEvent> = _event.asSharedFlow()

    private val _mapState = MutableStateFlow(
        MapState(
            markerData = listOf(),
            markerStates = mutableStateMapOf()
        )
    )

    val mapState: StateFlow<MapState> = _mapState.asStateFlow()

    fun getDongPostInfo(latitude: Double, longitude: Double, zoomLevel: Double) {
        viewModelScope.launch {
            val result = getNearbyPostCountUseCase(
                latitude,
                longitude,
                getFilteringDistance(zoomLevel = zoomLevel)
            )
            result.onSuccess {
                setMarkerStates(it)
            }

            result.onFailure {
                _event.emit(MapEvent.Error)
            }
        }
    }

    fun freeMarkers() {
        _mapState.value = _mapState.value.copy(
            markerData = listOf(),
            markerStates = mutableStateMapOf()
        )
    }

    private fun setMarkerStates(dongPostInfo: List<NearByPostCountDataModel>) {
        val markerStates = mutableMapOf<NearByPostCountDataModel, MarkerState>()

        dongPostInfo.forEach {
            markerStates[it] = MarkerState(position = LatLng(it.latitude, it.longitude))
        }

        _mapState.value = _mapState.value.copy(
            markerData = dongPostInfo,
            markerStates = mutableStateMapOf<NearByPostCountDataModel, MarkerState>().apply {
                putAll(markerStates)
            }
        )
    }

    private fun getFilteringDistance(zoomLevel: Double): Int {
        return when {
            zoomLevel < 11.275 -> 16500
            zoomLevel < 11.7 -> 12200
            zoomLevel < 12.125 -> 9200
            zoomLevel < 12.55 -> 6800
            zoomLevel < 13 -> 5000
            zoomLevel < 13.4 -> 3800
            zoomLevel < 13.825 -> 2900
            else -> 2100
        }
    }
}