package models

import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

/**
 * Data class de contenedores, clase que almacena información del informe de contenedores de los distritos.
 */
@DataSchema
data class Contenedor(
    val codigoInterno: Long,
    val tipoContenedor: TipoContenedor,
    val modelo: String,
    val description: String,
    val lote: Int,
    val distrito: String,
    val direction: String

)
