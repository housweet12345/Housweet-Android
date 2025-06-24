package com.housweet.presentation.util

// presentation/util/RegionParser.kt

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RegionParser {
    fun loadRegions(context: Context): Map<String, Map<String, List<String>>> {
        val inputStream = context.assets.open("korea_regions.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        val type = object : TypeToken<Map<String, Map<String, List<String>>>>() {}.type
        return Gson().fromJson(json, type)
    }
}