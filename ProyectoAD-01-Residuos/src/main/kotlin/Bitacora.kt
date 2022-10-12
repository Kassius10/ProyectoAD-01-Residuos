
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.io.File
import java.time.LocalDateTime
import java.util.UUID

/**
 * @params Elemenetos los cuales compondrán nuestra bitácota
 */
@Serializable
class Bitacora(
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
    val instante: String = LocalDateTime.now().toString()

    fun bitacoraXml(path:String){
        val xml = XML { indentString = "  " }
        val bitacora = File(path+ File.separator+"bitacoraCompleta.xml")
        bitacora.writeText(xml.encodeToString(this))
    }

}

