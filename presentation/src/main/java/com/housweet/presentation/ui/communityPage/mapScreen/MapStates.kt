package com.housweet.presentation.ui.communityPage.mapScreen

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.housweet.domain.model.NearByPostCountModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.MarkerState

@Stable
data class MapState(
    val markerData: List<NearByPostCountModel>,
    val markerStates: SnapshotStateMap<NearByPostCountModel, MarkerState>
)

sealed interface MapUiState {
    data object Idle : MapUiState
}

sealed interface MapEvent {
    data object Error: MapEvent
}