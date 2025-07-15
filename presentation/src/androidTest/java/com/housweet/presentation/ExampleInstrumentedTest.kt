package com.housweet.presentation

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.housweet.presentation.ui.communityPage.searchRegionScreen.RegionUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RegionUtilsInstrumentedTest {
    @Test
    fun testRegionUtilsWithRealContext() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val regionUtils = RegionUtils(context)

        val dtgList = regionUtils.getDtgList()

        dtgList.forEach {
            println(it)
            val ssgList = regionUtils.getSggList(it)
            println("$it: $ssgList")
            ssgList.forEach { ssg ->
                val dymList = regionUtils.getDymList(it, ssg)
                println("$it $ssg: $dymList")
            }

            println("\n\n")
        }
    }
}