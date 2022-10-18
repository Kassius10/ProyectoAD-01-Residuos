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
    val cantidad: Int,
    val lote: Int,
    val distrito: String,
    val barrio: String,
    val tipoVia: String,
    val nombre: String,
    val numero: Int,
    val coordenadaX: String,
    val coordenadaY: String,
    val longitud: String,
    val latitud: String,
    val direction: String

)
