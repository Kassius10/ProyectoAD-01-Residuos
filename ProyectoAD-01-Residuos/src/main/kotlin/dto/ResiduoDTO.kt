package dto

/**
 * Data class de ResiduosDto
 */
data class ResiduoDTO(
    val year: Int,
    val month: String,
    val lote: Int,
    val tipoResiduo: String,
    val numDistrito: Int,
    val distrito: String,
    val toneladas: Double
)