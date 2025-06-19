package com.housweet.presentation.ui.communityPage.searchRegionScreen

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.naver.maps.geometry.LatLng
import org.json.JSONArray
import org.json.JSONObject

class RegionUtils(private val context: Context) {
    private var regionData: JSONObject? = null

    companion object {
        private val nestedSggList = listOf("수원시", "성남시", "안양시", "부천시", "안산시", "고양시", "용인시", "창원시", "포항시", "전주시", "천안시", "청주시")
    }

    enum class Pattern {
        DTG_ONLY, // 도, 특별시, 광역시
        SGG_ONLY, // 시, 군, 구
        DYM_ONLY, // 동, 읍, 면
        DTG_SGG,
        SGG_DYM,
        SI_GU,
        DO_SI_GU,
        SI_GU_DONG,
        FULL_ADDRESS_3, // 특별시, 광역시 -> 시군구 -> 동읍면
        FULL_ADDRESS_4, // 도 -> 시 -> 구-> 동
        NOTHING
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
        if (!isOnlyKoreanWithSpaceAndNumber(inputText)) return emptyList()

        val pattern = detectPattern(inputText.split(" "))

        val result = when (pattern) {
            Pattern.DTG_ONLY -> getFullAddressUpTo10ByDtg(inputText)
            Pattern.SGG_ONLY -> getFullAddressUpTo10BySGG(inputText)
            Pattern.DYM_ONLY -> getFullAddressUpTo10ByDym(inputText)
            Pattern.DTG_SGG -> getFullAddressByDtgSgg(inputText.split(" "))
            Pattern.SGG_DYM -> getFullAddressBySggDym(inputText.split(" "))
            Pattern.SI_GU -> getFullAddressBySiGu(inputText.split(" "))
            Pattern.DO_SI_GU -> getFullAddressByDoSiGu(inputText.split(" "))
            Pattern.SI_GU_DONG -> getFullAddressBySiGuDong(inputText.split(" "))
            Pattern.FULL_ADDRESS_3 -> getFullAddressByDtgSggDym(inputText.split(" "))
            Pattern.FULL_ADDRESS_4 -> getFullAddressByDoSiGuDong(inputText.split(" "))
            Pattern.NOTHING -> getFullAddressByIncompleteWords(inputText)
        }

        return result
    }

    private fun detectPattern(parts: List<String>): Pattern {
        if (parts.isEmpty()) return Pattern.NOTHING

        return when (parts.size) {
            1 -> {
                when {
                    isDTG(parts[0]) -> Pattern.DTG_ONLY
                    isSGG(parts[0]) -> Pattern.SGG_ONLY
                    isDYM(parts[0]) -> Pattern.DYM_ONLY
                    else -> Pattern.NOTHING
                }
            }

            2 -> {
                when {
                    isDTG(parts[0]) && isSGG(parts[1]) -> Pattern.DTG_SGG
                    isSGG(parts[0]) && isDYM(parts[1]) -> Pattern.SGG_DYM
                    isSGG(parts[0]) && isSGG(parts[1]) -> Pattern.SI_GU
                    else -> Pattern.NOTHING
                }
            }

            3 -> {
                if (isDTG(parts[0]) && isSGG(parts[1]) && isDYM(parts[2])) {
                    Pattern.FULL_ADDRESS_3
                } else if (isDTG(parts[0]) && isSGG(parts[1]) && isSGG(parts[2])) {
                    Pattern.DO_SI_GU
                } else if (isSGG(parts[0]) && isSGG(parts[1]) && isDYM(parts[2])) {
                    Pattern.SI_GU_DONG
                } else {
                    Pattern.NOTHING
                }
            }

            4 -> {
                if (isDTG(parts[0]) && isSGG(parts[1]) && isSGG(parts[2]) && isDYM(parts[3])) {
                    Pattern.FULL_ADDRESS_4
                } else {
                    Pattern.NOTHING
                }
            }
            else -> return Pattern.NOTHING
        }
    }

    private fun getFullAddressByIncompleteWords(words: String): List<String> {
        val result = mutableListOf<String>()

        try {
            val wordList = words.split(" ")

            return when (wordList.size) {
                1 -> {
                    processFullAddressByIncompleteSingleWord(wordList, result)
                }

                2 -> {
                    processFullAddressByIncompleteTwoWords(wordList, result)
                }

                3 -> {
                    processFullAddressByIncompleteThreeWords(wordList, result)
                }

                4 -> {
                    processFullAddressByIncompleteFourWords(wordList, result)
                }

                else -> emptyList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return emptyList()
        }
    }

    private fun processFullAddressByIncompleteSingleWord(
        wordList: List<String>,
        result: MutableList<String>
    ): List<String> {
        val regionData = loadRegionData()
        val dtgList = regionData.getJSONObject("도/특별시/광역시").keys()
        val ssgList = regionData.getJSONObject("시/구/군").keys()
        val dymList = regionData.getJSONObject("동/읍/면").keys()

        dtgList.forEach {
            if (it.startsWith(wordList[0])) {
                result.addAll(getFullAddress(it))
                if (result.size > 10) return result.take(10)  // 즉시 리턴
            }
        }

        ssgList.forEach {
            if (it.startsWith(wordList[0])) {
                result.addAll(getFullAddress(it))
                if (result.size > 10) return result.take(10)  // 즉시 리턴
            }
        }

        dymList.forEach {
            if (it.startsWith(wordList[0])) {
                result.addAll(getFullAddress(it))
                if (result.size > 10) return result.take(10)  // 즉시 리턴
            }
        }

        return result
    }

    private fun processFullAddressByIncompleteTwoWords(
        wordList: List<String>,
        result: MutableList<String>
    ): List<String> {
        val regionData = loadRegionData()
        if (isDTG(wordList[0])) {
            val dtgData = regionData.getJSONObject("도/특별시/광역시").get(wordList[0])
            when (dtgData) {
                is JSONObject -> {
                    val ssgList = dtgData.getJSONArray("시/구/군")
                    processFullAddressByRestWordsAndSingleWord(ssgList, wordList[0] + " ", wordList[1], result)
                    if (result.size > 10) return result.take(10)
                }

                is JSONArray -> {
                    for (i in 0 until dtgData.length()) {
                        val ssgList = dtgData.getJSONObject(i).getJSONArray("시/구/군")
                        processFullAddressByRestWordsAndSingleWord(ssgList, wordList[0] + " ", wordList[1], result)
                        if (result.size > 10) return result.take(10)
                    }
                }
            }
        }

        if (isSGG(wordList[0])) {
            val ssgData = regionData.getJSONObject("시/구/군").get(wordList[0])
            when (ssgData) {
                is JSONObject -> {
                    val gdymList = ssgData.getJSONArray("구/동/읍/면")
                    processFullAddressByRestWordsAndSingleWord(gdymList, wordList[0] + " ", wordList[1], result)
                    if (result.size > 10) return result.take(10)
                }


                is JSONArray -> {
                    for (i in 0 until ssgData.length()) {
                        val gdymList = ssgData.getJSONObject(i).getJSONArray("구/동/읍/면")
                        processFullAddressByRestWordsAndSingleWord(gdymList, wordList[0] + " ", wordList[1], result)
                        if (result.size > 10) return result.take(10)
                    }
                }
            }
        }

        return result
    }

    private fun processFullAddressByIncompleteThreeWords(
        wordList: List<String>,
        result: MutableList<String>
    ): List<String> {
        val regionData = loadRegionData()
        if (isDTG(wordList[0]) && isSGG(wordList[1])) {
            val ssgData = regionData.getJSONObject("시/구/군").get(wordList[1])
            when (ssgData) {
                is JSONObject -> {
                    val gdymList = ssgData.getJSONArray("구/동/읍/면")
                    processFullAddressByRestWordsAndSingleWord(gdymList, wordList[0] + " " + wordList[1] + " ", wordList[2], result)
                    if (result.size > 10) return result.take(10)
                }

                is JSONArray -> {
                    for (i in 0 until ssgData.length()) {
                        val gdymList = ssgData.getJSONObject(i).getJSONArray("구/동/읍/면")
                        processFullAddressByRestWordsAndSingleWord(gdymList, wordList[0] + " " + wordList[1] + " ", wordList[2], result)
                        if (result.size > 10) return result.take(10)

                    }
                }
            }
        }


        if (isSGG(wordList[0]) && isSGG(wordList[1])) {
            val ssgData = regionData.getJSONObject("시/구/군").get(wordList[1])
            when (ssgData) {
                is JSONObject -> {
                    val dymList = ssgData.getJSONArray("구/동/읍/면")
                    processFullAddressByRestWordsAndSingleWord(dymList, wordList[0] + " " + wordList[1] + " ", wordList[2], result)
                    if (result.size > 10) return result.take(10)
                }

                is JSONArray -> {
                    for (i in 0 until ssgData.length()) {
                        val dymList = ssgData.getJSONObject(i).getJSONArray("구/동/읍/면")
                        processFullAddressByRestWordsAndSingleWord(dymList, wordList[0] + " " + wordList[1] + " ", wordList[2], result)
                        if (result.size > 10) return result.take(10)
                    }
                }
            }
        }
        return result
    }

    private fun processFullAddressByIncompleteFourWords(
        wordList: List<String>,
        result: MutableList<String>
    ): List<String> {
        val regionData = loadRegionData()
        if (isDTG(wordList[0]) && isSGG(wordList[1]) && isSGG(wordList[2])) {
            val ssgData = regionData.getJSONObject("시/구/군").get(wordList[2])
            when (ssgData) {
                is JSONObject -> {
                    val dymList = ssgData.getJSONArray("구/동/읍/면")
                    processFullAddressByRestWordsAndSingleWord(dymList, wordList[0] + " " + wordList[1] + " " + wordList[2] + " ", wordList[3], result)
                    if (result.size > 10) return result.take(10)
                }

                is JSONArray -> {
                    for (i in 0 until ssgData.length()) {
                        val dymList = ssgData.getJSONObject(i).getJSONArray("구/동/읍/면")
                        processFullAddressByRestWordsAndSingleWord(dymList, wordList[0] + " " + wordList[1] + " " + wordList[2] + " ", wordList[3], result)
                        if (result.size > 10) return result.take(10)
                    }
                }
            }
        }
        return result
    }

    private fun processFullAddressByRestWordsAndSingleWord(
        regionList: JSONArray,
        restWords: String,
        singleWord: String,
        result: MutableList<String>
    ) {
        for (i in 0 until regionList.length()) {
            val underRegionName = regionList.getString(i)
            if (underRegionName.startsWith(singleWord)) {
                result.addAll(getFullAddress(restWords + underRegionName))
            }
        }
    }

    private fun getFullAddressUpTo10ByDtg(dtg: String): List<String> {
        val result = mutableListOf<String>()
        try {
            val regionData = loadRegionData()
            val dtgData = regionData.getJSONObject("도/특별시/광역시").getJSONObject(dtg)
            val sggList = dtgData.getJSONArray("시/구/군")

            for (i in 0 until sggList.length()) {
                if (result.size >= 10) break

                val sggName = sggList.getString(i)
                val sggData = regionData.getJSONObject("시/구/군").get(sggName)

                when (sggData) {
                    is JSONObject -> processSggAndDymByDtgFromObject(sggData, dtg, sggName, result)

                    is JSONArray -> processSggAndDymByDtgFromArray(sggData, dtg, sggName, result)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }

    private fun processSggAndDymByDtgFromObject(
        sggData: JSONObject,
        dtgName: String,
        sggName: String,
        result: MutableList<String>
    ) {
        val gdymList = sggData.getJSONArray("구/동/읍/면")
        for (i in 0 until gdymList.length()) {
            if (result.size >= 10) break

            val gdymName = gdymList.getString(i)

            if (isSGG(gdymName)) {
                processNestedSgg(gdymName, dtgName, sggName, result)
            } else {
                result.add("$dtgName $sggName $gdymName")
            }
        }
    }

    private fun processSggAndDymByDtgFromArray(
        sggData: JSONArray,
        dtgName: String,
        sggName: String,
        result: MutableList<String>
    ) {
        for (i in 0 until sggData.length()) {
            if (result.size >= 10) break

            val sggDataOni = sggData.getJSONObject(i)
            if (dtgName != sggDataOni.getString("상위")) continue

            processSggAndDymByDtgFromObject(sggDataOni, dtgName, sggName, result)
        }
    }

    private fun getFullAddressUpTo10BySGG(sgg: String): List<String> {
        val result = mutableListOf<String>()

        try {
            val regionData = loadRegionData()

            val sggData = regionData.getJSONObject("시/구/군").get(sgg)

            when (sggData) {
                is JSONObject -> {
                    processDtgAndDymOrGuDymBySgg(sggData, sgg, result)
                }

                is JSONArray -> {
                    for (i in 0 until sggData.length()) {
                        val sggDataOni = sggData.getJSONObject(i)
                        processDtgAndDymOrGuDymBySgg(sggDataOni, sgg, result)
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    private fun processDtgAndDymOrGuDymBySgg(
        sggData: JSONObject,
        sggName: String,
        result: MutableList<String>
    ) {
        val dtgOrSiName = sggData.getString("상위")
        val gdymList = sggData.getJSONArray("구/동/읍/면")

        for (i in 0 until gdymList.length()) {
            if (result.size >= 10) {
                break
            }
            val gdymName = gdymList.getString(i)

            if (isSGG(gdymName)) {
                processNestedSgg(gdymName, dtgOrSiName, sggName, result)
            } else if (isSGG(dtgOrSiName)) {
                val siData = regionData?.getJSONObject("시/구/군")?.getJSONObject(dtgOrSiName) ?: return
                val dtgName = siData.getString("상위")
                result.add("$dtgName $dtgOrSiName $sggName $gdymName")
            } else {
                result.add("$dtgOrSiName $sggName $gdymName")
            }
        }
    }

    private fun getFullAddressUpTo10ByDym(dym: String): List<String> {
        val result = mutableListOf<String>()

        try {
            val regionData = loadRegionData()

            val dymData = regionData.getJSONObject("동/읍/면").get(dym)

            when (dymData) {
                is JSONObject -> processDtgAndSggByDymFromObject(dymData, dym, result)

                is JSONArray -> processDtgAndSggByDymFromArray(dymData, dym, result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }

    private fun processDtgAndSggByDymFromObject(
        dymData: JSONObject,
        dymName: String,
        result: MutableList<String>
    ) {
        val sggName = dymData.getString("상위")
        val sggData = regionData?.getJSONObject("시/구/군")?.get(sggName) ?: return
        when (sggData) {
            is JSONObject -> processDtgOrDtgAndSiBySggFromObject(sggData, sggName, dymName, result)
            is JSONArray -> processDtgOrDtgAndSiBySggFromArray(sggData, dymData, sggName, dymName, result)
        }
    }

    private fun processDtgAndSggByDymFromArray(
        dymData: JSONArray,
        dymName: String,
        result: MutableList<String>
    ) {
        for (i in 0 until dymData.length()) {
            if (result.size >= 10) break
            val dymDataOni = dymData.getJSONObject(i)
            processDtgAndSggByDymFromObject(dymDataOni, dymName, result)
        }
    }

    private fun processDtgOrDtgAndSiBySggFromObject(
        sggData: JSONObject,
        sggName: String,
        dymName: String,
        result: MutableList<String>
    ) {
        val dtgOrSiName = sggData.getString("상위")
        if (isSGG(dtgOrSiName)) {
            val doName = sggData.getString("최상위")
            result.add("$doName $dtgOrSiName $sggName $dymName")
        } else {
            result.add("$dtgOrSiName $sggName $dymName")
        }
    }

    private fun processDtgOrDtgAndSiBySggFromArray(
        sggData: JSONArray,
        dymData: JSONObject,
        sggName: String,
        dymName: String,
        result: MutableList<String>
    ) {
        for (i in 0 until sggData.length()) {
            if (result.size >= 10) break

            val sggDataOni = sggData.getJSONObject(i)
            val doNameOnSgg = sggDataOni.getString("최상위")
            val doNameOnDym = dymData.getString("최상위")

            if (doNameOnSgg != doNameOnDym) continue

            processDtgOrDtgAndSiBySggFromObject(sggDataOni, sggName, dymName, result)
        }
    }

    private fun getFullAddressByDtgSgg(dtgSgg: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val dtg = dtgSgg[0]
            val sgg = dtgSgg[1]

            val regionData = loadRegionData()
            val sggData = regionData.getJSONObject("시/구/군").get(sgg)

            when (sggData) {
                is JSONObject -> processGuDymByDtgAndSggFromObject(sggData, dtg, sgg, result)

                is JSONArray -> processGuDymByDtgAndSggFromArray(sggData, dtg, sgg, result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    private fun processGuDymByDtgAndSggFromObject(
        sggData: JSONObject,
        dtgName: String,
        sggName: String,
        result: MutableList<String>
    ) {
        val gdymList = sggData.getJSONArray("구/동/읍/면")
        if (dtgName != sggData.getString("상위")) return
        for (i in 0 until gdymList.length()) {
            if (result.size >= 10) break

            val gdymName = gdymList.getString(i)

            if (isSGG(gdymName)) {
                processNestedSgg(gdymName, dtgName, sggName, result)
            } else {
                result.add("$dtgName $sggName $gdymName")
            }
        }
    }

    private fun processGuDymByDtgAndSggFromArray(
        sggData: JSONArray,
        dtgName: String,
        sggName: String,
        result: MutableList<String>
    ) {
        for (i in 0 until sggData.length()) {
            if (result.size >= 10) break
            val sggDataOni = sggData.getJSONObject(i)
            if (dtgName != sggDataOni.getString("상위")) continue
            processGuDymByDtgAndSggFromObject(sggDataOni, dtgName, sggName, result)
        }
    }

    private fun getFullAddressBySggDym(guDym: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val sgg = guDym[0]
            val dym = guDym[1]

            val regionData = loadRegionData()
            val sggData = regionData.getJSONObject("시/구/군").get(sgg)

            when (sggData) {
                is JSONObject -> processDtgOrDtgAndSiBySggDymFromObject(sggData, sgg, dym, result)

                is JSONArray -> {
                    for (i in 0 until sggData.length()) {
                        if (result.size >= 10) break
                        val sggDataOni = sggData.getJSONObject(i)
                        processDtgOrDtgAndSiBySggDymFromObject(sggDataOni, sgg, dym, result)
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    private fun processDtgOrDtgAndSiBySggDymFromObject(
        sggData: JSONObject,
        sggName: String,
        dymName: String,
        result: MutableList<String>
    ) {
        val dtgOrSiName = sggData.getString("상위")
        val dtgNameBySgg = sggData.getString("최상위")
        val dymList = sggData.getJSONArray("구/동/읍/면")

        if (!dymList.contains(dymName)) return

        if (dtgOrSiName == dtgNameBySgg) {
            result.add("$dtgNameBySgg $sggName $dymName")
        } else {
            result.add("$dtgNameBySgg $dtgOrSiName $sggName $dymName")
        }
    }

    private fun getFullAddressBySiGu(siGu: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val si = siGu[0]
            val gu = siGu[1]

            val regionData = loadRegionData()
            val siData = regionData.getJSONObject("시/구/군").getJSONObject(si)
            val dtgName = siData.getString("상위")
            val guData = regionData.getJSONObject("시/구/군").get(gu)

            when (guData) {
                is JSONObject -> {
                    val siName = guData.getString("상위")

                    if (siName != si) return emptyList()

                    processDymByGuFromObject(guData, dtgName, si, gu, result)
                }

                is JSONArray -> {
                    for (i in 0 until guData.length()) {
                        if (result.size >= 10) break

                        val guDataOni = guData.getJSONObject(i)
                        val siName = guDataOni.getString("상위")

                        if (siName != si) continue

                        processDymByGuFromObject(guDataOni, dtgName, si, gu, result)
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }

    private fun getFullAddressByDoSiGu(doSiGu: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val doName = doSiGu[0]
            val siName = doSiGu[1]
            val guName = doSiGu[2]

            val regionData = loadRegionData()
            val siData = regionData.getJSONObject("시/구/군").getJSONObject(siName)
            val doNameBySi = siData.getString("상위")
            val guData = regionData.getJSONObject("시/구/군").get(guName)

            if (doNameBySi != doName) return emptyList()

            when (guData) {
                is JSONObject -> {
                    val siNameByGu = guData.getString("상위")
                    if (siNameByGu != siName) return emptyList()
                    processDymByGuFromObject(guData, doName, siName, guName, result)
                }

                is JSONArray -> {
                    for (i in 0 until guData.length()) {
                        val guDataOni = guData.getJSONObject(i)
                        val siNameByGu = guDataOni.getString("상위")
                        if (siNameByGu != siName) continue
                        processDymByGuFromObject(guDataOni, doName, siName, guName, result)
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

            val siData = regionData.getJSONObject("시/구/군").getJSONObject(si)
            val guData = regionData.getJSONObject("시/구/군").get(gu)

            val doName = siData.getString("상위")

            when (guData) {
                is JSONObject -> {
                    if (!isVaildFullAddressBySiGuDong(guData, si, dong)) return emptyList()
                    result.add("$doName $si $gu $dong")
                }

                is JSONArray -> {
                    for (i in 0 until guData.length()) {
                        val guDataOni = guData.getJSONObject(i)
                        if (!isVaildFullAddressBySiGuDong(guDataOni, si, dong)) continue
                        result.add("$doName $si $gu $dong")
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }

    private fun isVaildFullAddressBySiGuDong (
        guData: JSONObject,
        si: String,
        dong: String,
    ): Boolean {
        val dongList = guData.getJSONArray("구/동/읍/면")
        val siName = guData.getString("상위")
        if (siName != si) return false
        if (!dongList.contains(dong)) return false
        return true
    }

    private fun getFullAddressByDtgSggDym(dtgSggDym: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val dtg = dtgSggDym[0]
            val sgg = dtgSggDym[1]
            val dym = dtgSggDym[2]

            val regionData = loadRegionData()
            val dymData = regionData.getJSONObject("동/읍/면").get(dym)

            when (dymData) {
                is JSONObject -> processFullAddressByDtgSggDym(dymData, dtg, sgg, dym, result)

                is JSONArray -> {
                    for (i in 0 until dymData.length()) {
                        val dymDataOni = dymData.getJSONObject(i)
                        processFullAddressByDtgSggDym(dymDataOni, dtg, sgg, dym, result)
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return result
    }

    private fun processFullAddressByDtgSggDym(
        dymData: JSONObject,
        dtgName: String,
        sggName: String,
        dymName: String,
        result: MutableList<String>
    ) {
        val sggNameByDym = dymData.getString("상위")
        val dtgNameByDym = dymData.getString("최상위")
        if (sggNameByDym == sggName && dtgNameByDym == dtgName) {
            result.add("$dtgName $sggName $dymName")
        }
    }

    private fun getFullAddressByDoSiGuDong(doSiGuDong: List<String>): List<String> {
        val result = mutableListOf<String>()
        try {
            val doName = doSiGuDong[0]
            val si = doSiGuDong[1]
            val gu = doSiGuDong[2]
            val dong = doSiGuDong[3]

            val regionData = loadRegionData()
            val siData = regionData.getJSONObject("시/구/군").getJSONObject(si)
            val doNameBySi = siData.getString("상위")

            if (doNameBySi != doName) return emptyList()

            result.addAll(getFullAddressBySiGuDong(listOf(si, gu, dong)))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    fun isValidFullAddress(address: String): Boolean {
        return address == getFullAddress(address).joinToString(separator = " ")
    }

    fun getCoordinateByAddress(address: String): LatLng {
        val regionData = loadRegionData()
        val addresses = address.split(" ")
        val dtgName = addresses[addresses.size - 2]
        val dymName = addresses.last()
        val dymData = regionData.getJSONObject("동/읍/면").get(dymName)
        when (dymData) {
            is JSONObject -> {
                val coordinate = dymData.getString("좌표").split(",").map { it.toDouble() }
                return LatLng(coordinate[0], coordinate[1])
            }

            is JSONArray -> {
                for (i in 0 until dymData.length()) {
                    val dymDataOni = dymData.getJSONObject(i)
                    if (dymDataOni.getString("상위") != dtgName) continue
                    val coordinate = dymDataOni.getString("좌표").split(",").map { it.toDouble() }
                    return LatLng(coordinate[0], coordinate[1])
                }
            }
        }

        return LatLng(0.0, 0.0)
    }

    private fun isDTG(input: String): Boolean {
        val dtgData = loadRegionData().getJSONObject("도/특별시/광역시")
        return dtgData.has(input)
    }

    private fun isSGG(input: String): Boolean {
        val sggData = loadRegionData().getJSONObject("시/구/군")
        return sggData.has(input)
    }

    private fun isDYM(input: String): Boolean {
        val dymData = loadRegionData().getJSONObject("동/읍/면")
        return dymData.has(input)
    }

    private fun processNestedSgg(
        guName: String,
        doName: String,
        siName: String,
        result: MutableList<String>
    ) {
        try {
            val guDataValue = regionData?.getJSONObject("시/구/군")?.get(guName) ?: return

            when (guDataValue) {
                is JSONObject -> processDymByGuFromObject(guDataValue, doName, siName, guName, result)
                is JSONArray -> processDymByGuFromArray(guDataValue, doName, siName, guName, result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun processDymByGuFromObject(
        guData: JSONObject,
        doName: String,
        siName: String,
        guName: String,
        result: MutableList<String>
    ) {
        val dymList = guData.getJSONArray("구/동/읍/면")

        for (i in 0 until dymList.length()) {
            if (result.size >= 10) break

            val dymName = dymList.getString(i)
            result.add("$doName $siName $guName $dymName")
        }
    }

    private fun processDymByGuFromArray(
        guArray: JSONArray,
        doName: String,
        siName: String,
        guName: String,
        result: MutableList<String>
    ) {
        for (i in 0 until guArray.length()) {
            if (result.size >= 10) break

            val guDataOni = guArray.getJSONObject(i)
            if (siName != guDataOni.getString("상위")) continue
            processDymByGuFromObject(guDataOni, doName, siName, guName, result)
        }
    }

    private fun JSONArray.contains(value: Any): Boolean {
        for (i in 0 until this.length()) {
            if (this.get(i) == value) {
                return true
            }
        }
        return false
    }

    private fun isOnlyKoreanWithSpaceAndNumber(text: String): Boolean {
        val koreanRegex = Regex("^[가-힣]+([가-힣0-9]|\\s[가-힣])*$")
        return koreanRegex.matches(text)
    }

    fun getDtgList(): List<String> {
        try {
            val regionData = loadRegionData()
            return regionData.getJSONObject("도/특별시/광역시").keys().asSequence().toList()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return emptyList()
        }
    }

    fun getSggList(dtg: String): List<String> {
        val result = mutableListOf<String>()
        try {
            val regionData = loadRegionData()
            val dtgData = regionData.getJSONObject("도/특별시/광역시").getJSONObject(dtg)
            val sggJsonArray = dtgData.getJSONArray("시/구/군")
            for (i in 0 until sggJsonArray.length()) {
                val sggName = sggJsonArray.getString(i)
                if (nestedSggList.contains(sggName)) {
                    val nestedSggJsonArray =
                        regionData.getJSONObject("시/구/군").getJSONObject(sggName)
                            .getJSONArray("구/동/읍/면")
                    (0 until nestedSggJsonArray.length()).map { guNum ->
                        result.add(sggName + nestedSggJsonArray.getString(guNum))
                    }
                } else {
                    result.add(sggName)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    fun getDymList(dtg: String, sgg: String): List<String> {
        val result = mutableListOf<String>()
        try {
            val regionData = loadRegionData()
            val nestedSgg = splitIfNestedSgg(sgg)
            if (nestedSgg.isEmpty()) {
                val sggData = regionData.getJSONObject("시/구/군").get(sgg)
                when (sggData) {
                    is JSONObject -> {
                        getDymListFromSgg(sggData, result)
                    }

                    is JSONArray -> {
                        for (i in 0 until sggData.length()) {
                            val sggDataOni = sggData.getJSONObject(i)
                            if (dtg != sggDataOni.getString("상위")) continue
                            getDymListFromSgg(sggDataOni, result)
                        }
                    }
                }
            } else {
                val guData = regionData.getJSONObject("시/구/군").get(nestedSgg[1])
                when (guData) {
                    is JSONObject -> {
                        getDymListFromSgg(guData, result)
                    }

                    is JSONArray -> {
                        for (i in 0 until guData.length()) {
                            val guDataOni = guData.getJSONObject(i)
                            if (nestedSgg[0] != guDataOni.getString("상위") || dtg != guDataOni.getString(
                                    "최상위"
                                )
                            ) continue
                            getDymListFromSgg(guDataOni, result)
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }

    private fun getDymListFromSgg(
        sggData: JSONObject,
        result: MutableList<String>
    ) {
        val dymList = sggData.getJSONArray("구/동/읍/면")
        for (i in 0 until dymList.length()) {
            result.add(dymList.getString(i))
        }
    }

    private fun splitIfNestedSgg(sgg: String): List<String> {
        return nestedSggList.find { sgg.contains(it) }
            ?.let { listOf(it, sgg.removePrefix(it)) }
            ?: emptyList()
    }
}