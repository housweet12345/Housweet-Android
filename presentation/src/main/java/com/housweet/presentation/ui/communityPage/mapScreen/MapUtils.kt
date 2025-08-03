package com.housweet.presentation.ui.communityPage.mapScreen

import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds

object MapUtils {
    fun isPositionVisible(position: LatLng, bounds: LatLngBounds): Boolean {
        return bounds.contains(position)
    }
}