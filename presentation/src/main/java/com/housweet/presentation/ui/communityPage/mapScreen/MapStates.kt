package com.housweet.presentation.ui.communityPage.mapScreen

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.housweet.domain.model.community.NearByPostCountDataModel
import com.naver.maps.map.compose.MarkerState

@Stable
data class MapState(
    val markerData: List<NearByPostCountDataModel>,
    val markerStates: SnapshotStateMap<NearByPostCountDataModel, MarkerState>
)

enum class UserRoomState {
    IsNotBelong,
    IsHost,
    IsNotHost
}

sealed interface MapUiState {
    data object Idle : MapUiState
}

sealed interface MapEvent {
    data object Error: MapEvent
}