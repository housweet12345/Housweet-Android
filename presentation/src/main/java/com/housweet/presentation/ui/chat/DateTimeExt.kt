package com.housweet.presentation.ui.chat

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.util.Locale

private val KST: ZoneId = ZoneId.of("Asia/Seoul")

private val ISO_FLEX: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
    .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true).optionalEnd()
    .optionalStart().appendOffset("+HH:MM", "Z").optionalEnd()
    .toFormatter()

private val DOT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

private fun parseToInstant(src: String): Instant {
    return runCatching { Instant.parse(src) }.getOrNull()
        ?: runCatching { OffsetDateTime.parse(src, ISO_FLEX).toInstant() }.getOrNull()
        ?: runCatching { LocalDateTime.parse(src, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toInstant(ZoneOffset.UTC) }.getOrNull()
        ?: runCatching { LocalDate.parse(src, DOT_DATE).atStartOfDay().toInstant(ZoneOffset.UTC) }.getOrNull()
        ?: error("Unsupported datetime format: $src")
}

/** 항상 KST로 변환해서 "yyyy년 MM월 dd일 HH시 mm분" 으로 표기 */
fun String.toKstKoreanFull(): String {
    val zdt = parseToInstant(this).atZone(KST)
    val fmt = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a h시 mm분", Locale.KOREAN)
    return zdt.format(fmt)
}