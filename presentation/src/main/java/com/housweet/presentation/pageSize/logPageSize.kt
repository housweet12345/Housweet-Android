package com.housweet.presentation.pageSize

import android.system.Os
import android.system.OsConstants
import android.util.Log

fun logPageSize() {
    try {
        val ps = Os.sysconf(OsConstants._SC_PAGESIZE)
        Log.i("PageSizeCheck", "PAGE_SIZE=$ps")
    } catch (t: Throwable) {
        Log.w("PageSizeCheck", "sysconf failed: ${t.message}")
    }
}