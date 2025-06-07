package com.housweet.presentation.ui.communityPage.mapScreen

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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
class MapViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Idle)
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<MapEvent>()
    val event: SharedFlow<MapEvent> = _event.asSharedFlow()

    private val _mapState = MutableStateFlow(
        MapState(
            markerDataByDong = emptyMap(),
            markerDataByGu = emptyMap(),
            markerStates = mutableStateMapOf(),
            isDisplayDongMarker = true,
            isMarkerAnimating = false,
            prevZoomState = true
        )
    )
    val mapState: StateFlow<MapState> = _mapState.asStateFlow()

    fun getDongPostInfo() {
        viewModelScope.launch {
            val dongPostInfo = mapOf(
                "강남구 역삼1동" to listOf(LatLng(37.501, 127.036), LatLng(37.501, 127.036)),
                "강남구 역삼2동" to listOf(LatLng(37.504, 127.045), LatLng(37.504, 127.045)),
                "강남구 역삼3동" to listOf(LatLng(37.508, 127.030), LatLng(37.508, 127.030)),
                "강남구 역삼4동" to listOf(LatLng(37.511, 127.041), LatLng(37.511, 127.041)),
            )

            setMarkerStates(dongPostInfo)
        }
    }

    fun freeMarkers() {
        _mapState.value = _mapState.value.copy(
            markerDataByDong = emptyMap(),
            markerDataByGu = emptyMap(),
            markerStates = mutableStateMapOf()
        )
    }

    private fun setMarkerStates(dongPostInfo: Map<String, List<LatLng>>) {
        val markerStates = mutableMapOf<String, MarkerState>()
        val markerDataByGu = MapUtils.groupByGu(dongPostInfo)

        dongPostInfo.forEach { (dongRegion, postList) ->
            markerStates[dongRegion] = MarkerState(position = postList[0])
        }

        markerDataByGu.forEach { (guRegion, postList) ->
            markerStates[guRegion] =
                MarkerState(position = MapUtils.getAvgLatLng(postList.toSet()))
        }

        _mapState.value = _mapState.value.copy(
            markerDataByDong = dongPostInfo,
            markerDataByGu = markerDataByGu,
            markerStates = mutableStateMapOf<String, MarkerState>().apply {
                putAll(markerStates)
            }
        )
    }

    fun updateZoomState(isDongZoom: Boolean, isMarkerAnimating: Boolean) {
        _mapState.value = _mapState.value.copy(
            isDisplayDongMarker = isDongZoom,
            isMarkerAnimating = isMarkerAnimating,
            prevZoomState = isDongZoom
        )
    }

    fun updateMarkerAnimation(animationProgressValue: Float, isDongZoom: Boolean, visibleBounds: LatLngBounds?) {
        if (isDongZoom) {
            _mapState.value.markerDataByDong.forEach { (dongRegion, postList) ->
                val guName = dongRegion.split(" ")[0]
                val guLatLng = MapUtils.getAvgLatLng((_mapState.value.markerDataByGu[guName] ?: postList).toSet())
                val dongLatLng = postList[0]

                val isVisible = visibleBounds?.contains(dongLatLng) ?: true
                val animatedLatLng = if (isVisible) {
                    MapUtils.interpolateLatLng(guLatLng, dongLatLng, animationProgressValue)
                } else {
                    dongLatLng
                }

                _mapState.value.markerStates[dongRegion]?.let {
                    it.position = animatedLatLng
                }
            }
        } else {
            _mapState.value.markerDataByGu.forEach { (guRegion, guPostList) ->
                val guLatLng = MapUtils.getAvgLatLng(guPostList.toSet())
                val markersOfDongOfGu = _mapState.value.markerDataByDong.filter { (dongRegion, _) ->
                    dongRegion.startsWith(guRegion)
                }

                markersOfDongOfGu.forEach { (dongOfGuRegion, dongPostList) ->
                    val isVisible = visibleBounds?.contains(dongPostList[0]) ?: true
                    val animatedLatLng = if (isVisible) {
                        MapUtils.interpolateLatLng(dongPostList[0], guLatLng, animationProgressValue)
                    } else {
                       guLatLng
                    }

                    _mapState.value.markerStates[dongOfGuRegion]?.let {
                        it.position = animatedLatLng
                    }
                }
            }
        }
    }
}