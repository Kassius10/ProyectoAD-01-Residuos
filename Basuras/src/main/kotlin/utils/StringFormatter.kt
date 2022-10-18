package utils

import java.text.Normalizer

/**
 * Método para remplazar los acentos o caracteres especiales de una cadena.
 * @param cadena Cadena que queremos modificar
 * @return Cadena ya formateada
 */
fun replaceAcents(cadena: String): String {
    var s = Normalizer.normalize(cadena, Normalizer.Form.NFD)
    s = Regex("\\p{InCombiningDiacriticalMarks}+").replace(s, "")
    return s
}