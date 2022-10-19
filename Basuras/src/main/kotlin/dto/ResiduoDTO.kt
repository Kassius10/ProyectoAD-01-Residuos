package dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

/**
 * Data class de ResiduosDto
 */
@Serializable
@SerialName("Residuos")
@DataSchema
data class ResiduoDTO(
    @SerialName("Año")
    @XmlElement(true)
    val year: Int,
    @SerialName("Mes")
    @XmlElement(true)
    val month: String,
    @SerialName("Lote")
    @XmlElement(true)
    val lote: Int,
    @SerialName("Residuo")
    @XmlElement(true)
    val tipoResiduo: String,
    @SerialName("Distrito")
    @XmlElement(true)
    val numDistrito: Int,
    @SerialName("Nombre_Distrito")
    @XmlElement(true)
    val distrito: String,
    @SerialName("Toneladas")
    @XmlElement(true)
    val toneladas: Double
) {
    /**
     * Método para imprimir el objeto en una sola línea indicándole el separador que queremos que tenga.
     * @param separator Cadena por la que queremos separar
     * @return Devuelve la cadena con el formato indicado.
     */
    fun toString(separator: String): String {
        return "$year$separator$month$separator$lote$separator$tipoResiduo$separator$numDistrito$separator$distrito$separator$toneladas"
    }
}