package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Método de extension de LocalDateTime, el cual formatea la fecha a Española
 * @return Devuelve una cadena con el formato aplicado español.
 */
fun LocalDateTime.formatToString(): String {
    return this.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale("es", "ES")))
}

/**
 * Método de extension de LocalDateTime, el cual formatea la fecha con el formato ISO 8601
 * @return Devuelve una cadena con el formato aplicado ISO 8601
 */
fun LocalDateTime.formatToISO8601(): String {
    val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")
    return this.format(formatter)
}