package com.housweet.presentation.ui.communityPage.mapScreen

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.housweet.domain.model.NearByPostCountModel
import com.housweet.domain.usecase.UseCases
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
    private val useCases: UseCases
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
            useCases.getNearbyPostCountUseCase(
                latitude = latitude,
                longitude = longitude,
                filteringDistance = getFilteringDistance(zoomLevel = zoomLevel)
            ).collect { result ->
                result.onSuccess {
                    setMarkerStates(it)
                }

                result.onFailure {
                    _event.emit(MapEvent.Error)
                }
            }
        }
    }

    fun freeMarkers() {
        _mapState.value = _mapState.value.copy(
            markerData = listOf(),
            markerStates = mutableStateMapOf()
        )
    }

    private fun setMarkerStates(dongPostInfo: List<NearByPostCountModel>) {
        val markerStates = mutableMapOf<NearByPostCountModel, MarkerState>()

        dongPostInfo.forEach {
            markerStates[it] = MarkerState(position = LatLng(it.latitude, it.longitude))
        }

        _mapState.value = _mapState.value.copy(
            markerData = dongPostInfo,
            markerStates = mutableStateMapOf<NearByPostCountModel, MarkerState>().apply {
                putAll(markerStates)
            }
        )
    }

    // 검색 반경 계산 함수
    private fun getFilteringDistance(zoomLevel: Double): Int {
        return when {
            zoomLevel < 10.425 -> 35000
            zoomLevel < 10.8 -> 27500
            zoomLevel < 11.174 -> 21000
            zoomLevel < 11.548 -> 16000
            zoomLevel < 11.922 -> 12500
            zoomLevel < 12.296 -> 9550
            zoomLevel < 12.78 -> 6900
            zoomLevel < 13.27 -> 4500
            zoomLevel < 13.76 -> 3500
            else -> 2500
        }
    }
}