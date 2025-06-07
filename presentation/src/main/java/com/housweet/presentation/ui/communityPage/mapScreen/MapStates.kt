package com.housweet.presentation.ui.communityPage.mapScreen

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.MarkerState

@Stable
data class MapState(
    val markerDataByDong: Map<String, List<LatLng>>,
    val markerDataByGu: Map<String, List<LatLng>>,
    val markerStates: SnapshotStateMap<String, MarkerState>,
    val isDisplayDongMarker: Boolean,
    val isMarkerAnimating: Boolean,
    val prevZoomState: Boolean
) {
    val renderMarkers: Map<String, List<LatLng>>
        get() = when {
            isMarkerAnimating -> markerDataByDong // 애니메이션 중에는 항상 동 마커 표시
            isDisplayDongMarker -> markerDataByDong
            else -> markerDataByGu
        }
}

sealed interface MapUiState {
    data object Idle : MapUiState
}

sealed interface MapEvent {
    data object Error: MapEvent
}