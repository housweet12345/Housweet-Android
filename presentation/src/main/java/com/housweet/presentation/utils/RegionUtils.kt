package com.housweet.presentation.utils

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class RegionUtils(private val context: Context) {
    private var regionData: JSONObject? = null

    enum class Pattern {
        SI_ONLY,         // 시/도만 입력 (예: "서울특별시")
        GU_ONLY,         // 구/군만 입력 (예: "강남구")
        DONG_ONLY,     // 동/읍/면만 입력 (예: "삼성동")
        SI_GU,     // 시/도 + 구/군 (예: "서울특별시 강남구")
        GU_DONG, // 구/군 + 동/읍/면 (예: "강남구 삼성동")
        FULL_ADDRESS,          // 전체 주소 (예: "서울특별시 강남구 삼성동")
        NOTHING                // 일반 키워드 검색 (위 패턴에 맞지 않는 경우)
    }

    private fun loadRegionData(): JSONObject {
        regionData?.let { return it }

        try {
            val inputStream = context.assets.open("regions.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            regionData = JSONObject(json).getJSONObject("행정구역")
            return regionData!!
        } catch (ex: Exception) {
            ex.printStackTrace()
            return JSONObject()
        }
    }

    fun getFullAddress(inputText: String): List<String> {
        val pattern = detectPattern(inputText.split(" "))

        val result = when (pattern) {
            Pattern.SI_ONLY -> getFullAddressUpTo10BySi(inputText)
            Pattern.GU_ONLY -> getFullAddressUpTo10ByGu(inputText)
            Pattern.DONG_ONLY -> getFullAddressByDong(inputText)
            Pattern.SI_GU -> getFullAddressBySiGu(inputText.split(" "))
            Pattern.GU_DONG -> getFullAddressByGuDong(inputText.split(" "))
            Pattern.FULL_ADDRESS -> getFullAddressBySiGuDong(inputText.split(" "))
            Pattern.NOTHING -> emptyList()
        }

        return result
    }

    private fun detectPattern(parts: List<String>): Pattern {
        if (parts.isEmpty()) return Pattern.NOTHING

        return when (parts.size) {
            1 -> {
                when {
                    isSi(parts[0]) -> Pattern.SI_ONLY
                    isGu(parts[0]) -> Pattern.GU_ONLY
                    isDong(parts[0]) -> Pattern.DONG_ONLY
                    else -> Pattern.NOTHING
                }
            }

            2 -> {
                when {
                    isSi(parts[0]) && isGu(parts[1]) -> Pattern.SI_GU
                    isGu(parts[0]) && isDong(parts[1]) -> Pattern.GU_DONG
                    else -> Pattern.NOTHING
                }
            }

            3 -> {
                if (isSi(parts[0]) && isGu(parts[1]) && isDong(parts[2])) {
                    Pattern.FULL_ADDRESS
                } else {
                    Pattern.NOTHING
                }
            }

            else -> return Pattern.NOTHING
        }
    }

    private fun isSi(input: String): Boolean {
        val siData = loadRegionData().getJSONObject("시도")
        return siData.has(input)
    }

    private fun isGu(input: String): Boolean {
        val guData = loadRegionData().getJSONObject("구군")
        return guData.has(input)
    }

    private fun isDong(input: String): Boolean {
        val dongData = loadRegionData().getJSONObject("동읍면")
        return dongData.has(input)
    }

    private fun getFullAddressUpTo10BySi(si: String): List<String> {
        val result = mutableListOf<String>()
        try {
            val regionData = loadRegionData()
            val siData = regionData.getJSONObject("시도").getJSONObject(si)
            val guList = siData.getJSONArray("구군")

            for (i in 0 until guList.length()) {
                if (result.size >= 10) {
                    break
                }

                val guName = guList.getString(i)
                val guData = regionData.getJSONObject("구군").getJSONObject(guName)
                val dongList = guData.getJSONArray("동읍면")

                for (j in 0 until dongList.length()) {
                    if (result.size >= 10) {
                        break
                    }

                    val dongName = dongList.getString(j)
                    val fullAddress = "$si $guName $dongName"
                    result.add(fullAddress)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }

    private fun getFullAddressUpTo10ByGu(gu: String): List<String> {
        val result = mutableListOf<String>()

        try {
            val regionData = loadRegionData()

            val guData = regionData.getJSONObject("구군").getJSONObject(gu)
            val siName = guData.getString("상위")
            val dongList = guData.getJSONArray("동읍면")

            for (i in 0 until dongList.length()) {
                if (result.size >= 10) {
                    break
                }

                val dongName = dongList.getString(i)
                val fullAddress = "$siName $gu $dongName"
                result.add(fullAddress)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    private fun getFullAddressByDong(dong: String): List<String> {
        val result = mutableListOf<String>()

        try {
            val regionData = loadRegionData()

            val dongData = regionData.getJSONObject("동읍면").get(dong)

            if (dongData is JSONObject) {
                result.add("${dongData.getString("최상위")} ${dongData.getString("상위")} $dong")
            } else if (dongData is JSONArray) {
                for (i in 0 until dongData.length()) {
                    val data = dongData.getJSONObject(i)
                    result.add("${data.getString("최상위")} ${data.getString("상위")} $dong")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    private fun getFullAddressBySiGu(siGu: List<String>): List<String> {
        try {
            val si = siGu[0]
            val gu = siGu[1]

            val regionData = loadRegionData()
            val guData = regionData.getJSONObject("구군").getJSONObject(gu)
            val siName = guData.getString("상위")

            if (siName == si) {
                return getFullAddressUpTo10ByGu(gu)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return emptyList()
    }

    private fun getFullAddressByGuDong(guDong: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val gu = guDong[0]
            val dong = guDong[1]

            val regionData = loadRegionData()
            val dongData = regionData.getJSONObject("동읍면").get(dong)

            if (dongData is JSONObject) {
                val guName = dongData.getString("상위")
                if (guName == gu) {
                    result.add("${dongData.getString("최상위")} $gu $dong")
                }
            } else if (dongData is JSONArray) {
                for (i in 0 until dongData.length()) {
                    val dongDataOni = dongData.getJSONObject(i)
                    val guName = dongDataOni.getString("상위")
                    if (guName == gu) {
                        result.add("${dongDataOni.getString("최상위")} $gu $dong")
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    private fun getFullAddressBySiGuDong(siGuDong: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val si = siGuDong[0]
            val gu = siGuDong[1]
            val dong = siGuDong[2]

            val regionData = loadRegionData()
            val dongData = regionData.getJSONObject("동읍면").get(dong)

            if (dongData is JSONObject) {
                val guName = dongData.getString("상위")
                val siName = dongData.getString("최상위")
                if (guName == gu && siName == si) {
                    result.add("$si $gu $dong")
                }
            } else if (dongData is JSONArray) {
                for (i in 0 until dongData.length()) {
                    val dongDataOni = dongData.getJSONObject(i)
                    val guName = dongDataOni.getString("상위")
                    val siName = dongDataOni.getString("최상위")
                    if (guName == gu && siName == si) {
                        result.add("$si $gu $dong")
                    }
                }
            }


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    fun isValidFullAddress(address: String): Boolean {
        val siGuDong = address.split(" ")
        try {
            val pattern = detectPattern(siGuDong)

            if (pattern != Pattern.FULL_ADDRESS) {
                return false
            }

            val si = siGuDong[0]
            val gu = siGuDong[1]
            val dong = siGuDong[2]

            val regionData = loadRegionData()
            val dongData = regionData.getJSONObject("동읍면").get(dong)

            if (dongData is JSONObject) {
                val guName = dongData.getString("상위")
                val siName = dongData.getString("최상위")
                if (guName == gu && siName == si) {
                    return true
                }
            } else if (dongData is JSONArray) {
                for (i in 0 until dongData.length()) {
                    val dongDataOni = dongData.getJSONObject(i)
                    val guName = dongDataOni.getString("상위")
                    val siName = dongDataOni.getString("최상위")
                    if (guName == gu && siName == si) {
                        return true
                    }
                }
            }


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return false
    }
}