package dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Data class de ResiduosDto
 */
@Serializable
@SerialName("Residuo")
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
)