package com.housweet.domain.model.community

data class NearByPostCountDataModel(
    val siName: String,
    val guName: String,
    val dongName: String,
    val latitude: Double,
    val longitude: Double,
    val roomCount: Int
)