package dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class de ResiduosDto
 */
@Serializable
data class ResiduoDTO(
    @SerialName("Año")
    val year: Int,
    @SerialName("Mes")
    val month: String,
    @SerialName("Lote")
    val lote: Int,
    @SerialName("Residuo")
    val tipoResiduo: String,
    @SerialName("Distrito")
    val numDistrito: Int,
    @SerialName("Nombre Distrito")
    val distrito: String,
    @SerialName("Toneladas")
    val toneladas: Double
)