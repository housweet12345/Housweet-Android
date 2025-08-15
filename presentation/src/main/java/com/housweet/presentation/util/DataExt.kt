package com.housweet.presentation.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun String.isoToDotDate(): String = try {
    val odt = OffsetDateTime.parse(this)
    odt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
} catch (_: Exception) { this }