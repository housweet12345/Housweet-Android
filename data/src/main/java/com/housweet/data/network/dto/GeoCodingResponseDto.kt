package com.housweet.data.network.dto

import com.housweet.domain.model.Coordinate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodingResponseDto(
    @SerialName("addresses")
    val addresses: List<Addresses>,
    @SerialName("errorMessage")
    val errorMessage: String,
    @SerialName("meta")
    val meta: Meta,
    @SerialName("status")
    val status: String
)

@Serializable
data class Addresses(
    @SerialName("addressElements")
    val addressElements: List<AddressElement>,
    @SerialName("distance")
    val distance: Double,
    @SerialName("englishAddress")
    val englishAddress: String,
    @SerialName("jibunAddress")
    val jibunAddress: String,
    @SerialName("roadAddress")
    val roadAddress: String,
    @SerialName("x")
    val x: String,
    @SerialName("y")
    val y: String
)

@Serializable
data class Meta(
    @SerialName("count")
    val count: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("totalCount")
    val totalCount: Int
)

@Serializable
data class AddressElement(
    @SerialName("code")
    val code: String,
    val longName: String,
    @SerialName("shortName")
    val shortName: String,
    @SerialName("types")
    val types: List<String>
)

fun GeoCodingResponseDto.toCoordinate(): Coordinate {
    return Coordinate(
        x = this.addresses[0].x.toDouble(),
        y = this.addresses[0].y.toDouble()
    )
}