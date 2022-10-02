package models

import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

/**
 * Data class de residuos, clase que almacena información del informe de residuos en los distritos.
 */
@DataSchema
data class Residuo(
    val year: Int,
    val month: Month,
    val lote: Int,
    val tipoResiduo: TipoResiduo,
    val numDistrito: Int,
    val distrito: String,
    val toneladas: Double
)