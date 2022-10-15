package dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("Contenedor")
data class ContenedorDTO(
    @SerialName("Código_Interno_del_Situad")
    @XmlElement(true)
    val codigoInterno: Long,
    @SerialName("Tipo_Contenedor")
    @XmlElement(true)
    val tipoContenedor: String,
    @SerialName("Modelo")
    @XmlElement(true)
    val modelo: String,
    @SerialName("Descripcion_Modelo")
    @XmlElement(true)
    val description: String,
    @SerialName("Cantidad")
    @XmlElement(true)
    val cantidad: Int,
    @SerialName("Lote")
    @XmlElement(true)
    val lote: Int,
    @SerialName("Distrito")
    @XmlElement(true)
    val distrito: String,
    @SerialName("Barrio")
    @XmlElement(true)
    val barrio: String,
    @SerialName("Tipo_Vía")
    @XmlElement(true)
    val tipoVia: String,
    @SerialName("Nombre")
    @XmlElement(true)
    val nombre: String,
    @SerialName("Número")
    @XmlElement(true)
    val numero: Int,
    @SerialName("COORDENADA_X")
    @XmlElement(true)
    val coordenadaX: String,
    @SerialName("COORDENADA_Y")
    @XmlElement(true)
    val coordenadaY: String,
    @SerialName("LONGITUD")
    @XmlElement(true)
    val longitud: String,
    @SerialName("LATITUD")
    @XmlElement(true)
    val latitud: String,
    @SerialName("DIRECCION")
    @XmlElement(true)
    val direction: String
) {
    /**
     * Método para imprimir el objeto en una sola línea indicándole el separador que queremos que tenga.
     * @param separator Cadena por la que queremos separar
     * @return Devuelve la cadena con el formato indicado.
     */
    fun toString(separator: String): String {
        return "$codigoInterno$separator$tipoContenedor$separator$modelo$separator$description$separator$cantidad$separator$lote$separator$distrito$separator$barrio$separator$tipoVia$separator$nombre$separator$numero$separator$coordenadaX$separator$coordenadaY$separator$longitud$separator$latitud$separator$direction"
    }
}