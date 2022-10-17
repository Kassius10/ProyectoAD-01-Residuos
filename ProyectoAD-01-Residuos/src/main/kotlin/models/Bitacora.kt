package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import utils.formatToISO8601
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger {}

@Serializable
@SerialName("Ejecución")
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
        var file = "$path${File.separator}bitacora.xml"
        var lista = mutableListOf<Bitacora>()
        lista.add(this)

        val xml = XML { indentString = "  " }

        if (Files.notExists(Paths.get(file))) {
            File(file).writeText(xml.encodeToString(lista))
        } else {
            var bitacoras = XML.decodeFromString<List<Bitacora>>(File(file).readText()).toMutableList()
            bitacoras.add(this)
            File(file).writeText(xml.encodeToString(bitacoras))
        }
        logger.debug { "Ruta del fichero bitácora: $file" }

    }

}