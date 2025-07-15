package com.housweet.presentation.ui.communityPage.mapScreen

import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds

object MapUtils {
    fun groupByGu(dongPostInfo: Map<String, List<LatLng>>): Map<String, List<LatLng>> {
        return dongPostInfo.entries
            .groupBy(
                keySelector = { entry -> entry.key.split(" ")[0] },
                valueTransform = { entry -> entry.value }
            )
            .mapValues { (_, coordLists) ->
                coordLists.flatten()
            }
    }

    fun getAvgLatLng(positions: Set<LatLng>): LatLng {
        val avgLat = positions.sumOf { it.latitude } / positions.size
        val avgLng = positions.sumOf { it.longitude } / positions.size
        return LatLng(avgLat, avgLng)
    }

    fun interpolateLatLng(start: LatLng, end: LatLng, progress: Float): LatLng {
        return LatLng(
            lerp(start.latitude, end.latitude, progress),
            lerp(start.longitude, end.longitude, progress)
        )
    }

    private fun lerp(start: Double, end: Double, fraction: Float): Double {
        return start + (end - start) * fraction
    }

    fun isPositionVisible(position: LatLng, bounds: LatLngBounds): Boolean {
        return bounds.contains(position)
    }
}