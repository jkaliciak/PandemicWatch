package dev.jakal.pandemicwatch.common.utils

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

/**
 * LocalDate to epoch days Long
 */
fun LocalDate.toLong(): Long = toEpochDay()

/**
 * Epoch days Long to LocalDate
 */
fun Long.toLocalDate(): LocalDate = LocalDate.ofEpochDay(this)

/**
 * LocalDateTime to epoch millis Long
 */
fun LocalDateTime.toLong(): Long = this.toInstant(ZoneOffset.UTC).toEpochMilli()

/**
 * Epoch millis Long to LocalDateTime
 */
fun Long.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(this),
    ZoneOffset.UTC
)

/**
 * M/d/yy format String to LocalDate
 */
fun String.toLocalDateFromNovelCovidAPIDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern("M/d/yy"))

/**
 * LocalDateTime to UI ready formatted String
 */
fun LocalDateTime.toDateTimeFormat(): String = DateTimeFormatter.ofLocalizedDateTime(
    FormatStyle.SHORT, FormatStyle.SHORT
).format(this)

/**
 * LocalDate to UI ready formatted String
 */
fun LocalDate.toDateFormat(): String = format(DateTimeFormatter.ofPattern("d MMM"))