package models

import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import java.io.File

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

/**
 * Función para procesar los datos del csv y limpiar datos.
 * @param file Fichero que contiene el csv a limpiar.
 * @return Devuelve una lista de residuos.
 */
fun loadDataFromCsv(file: File): List<Residuo> {
    val residuos: List<Residuo> = file.readLines()
        .drop(1)
        .map { it.split(";") }
        .map {
            it.map { campo -> campo.trim() }
            Residuo(
                it[0].toInt(),
                Month.valueOf(it[1].uppercase()),
                it[2].toInt(),
                TipoResiduo.valueOf(it[3].replace(" ", "_").replace("-", "_")),
                it[4].toInt(),
                it[5],
                it[6].replace(",", ".").toDouble()
            )
        }
    println("hola")
    return residuos
}