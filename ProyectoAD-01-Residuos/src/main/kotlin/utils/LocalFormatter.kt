package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun LocalDateTime.formatToString(): String {
    return this.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale("es", "ES")))
}

fun LocalDateTime.formatToISO8601(): String {
    val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")
    return this.format(formatter)
}