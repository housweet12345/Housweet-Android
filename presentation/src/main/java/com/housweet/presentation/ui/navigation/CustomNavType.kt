package com.housweet.presentation.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.housweet.domain.model.community.Coordinate
import kotlinx.serialization.json.Json

val CoordinateType = object : NavType<Coordinate?>(isNullableAllowed = true) {
    private val coordinateSerializer = Coordinate.serializer()

    override fun get(bundle: Bundle, key: String): Coordinate? {
        bundle.getString(key)?.let {
            return if(it == "null")  null
            else Json.decodeFromString(coordinateSerializer, it)
        } ?: return null
    }

    override fun parseValue(value: String): Coordinate? {
        return if (value == "null") null else Json.decodeFromString(coordinateSerializer, Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: Coordinate?) {
        bundle.putString(key, value?.let { Json.encodeToString(coordinateSerializer, it) } ?: "null")
    }

    override fun serializeAsValue(value: Coordinate?): String {
        return value?.let { Uri.encode(Json.encodeToString(coordinateSerializer, it)) } ?: "null"
    }
}