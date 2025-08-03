package com.housweet.data.network.dto

import com.housweet.domain.model.NearByPostCountModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetNearbyPostCountResponseListDto(
    @SerialName("data")
    val data: List<GetNearbyPostCountResponseDto>
)

@Serializable
data class GetNearbyPostCountResponseDto(
    @SerialName("si_id")
    val siId: Int,
    @SerialName("si_name")
    val siName: String,
    @SerialName("gu_id")
    val guId: Int,
    @SerialName("gu_name")
    val guName: String,
    @SerialName("dong_id")
    val dongId: Int,
    @SerialName("dong_name")
    val dongName: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("room_count")
    val roomCount: Int
)

fun GetNearbyPostCountResponseListDto.toNearByPostCountModel(): List<NearByPostCountModel> {
    return this.data.map {
        NearByPostCountModel(
            siName = it.siName,
            guName = it.guName,
            dongName = it.dongName,
            latitude = it.latitude,
            longitude = it.longitude,
            roomCount = it.roomCount
        )
    }
}
