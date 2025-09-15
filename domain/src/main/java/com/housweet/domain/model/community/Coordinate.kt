package com.housweet.domain.model.community

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val x: Double,
    val y: Double
)