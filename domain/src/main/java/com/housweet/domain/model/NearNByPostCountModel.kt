package com.housweet.domain.model

data class NearByPostCountModel(
    val siName: String,
    val guName: String,
    val dongName: String,
    val latitude: Double,
    val longitude: Double,
    val roomCount: Int
)