package models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import utils.formatToISO8601
import java.io.File
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Bitacora(
    @XmlElement(true)
    val opcionRecibida: String,
    @XmlElement(true)
    val isSucces: Boolean,
    @XmlElement(true)
    val tiempoEjecución: Long,
) {
    @XmlElement(true)
    val id: String = UUID.randomUUID().toString()

    @XmlElement(true)
    val instante: String = LocalDateTime.now().formatToISO8601()

    /**
     * Método para crear el fichero xml de bitácora.
     * @param path Ruta donde queremos almacenar el fichero bitácora.
     */
    fun bitacoraXml(path: String) {
        val xml = XML { indentString = "  " }
        val bitacora = File(path + File.separator + "bitacoraCompleta$id.xml")
        bitacora.writeText(xml.encodeToString(this))
    }
}