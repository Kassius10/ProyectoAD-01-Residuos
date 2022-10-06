package controller

import dto.ResiduoDTO
import java.io.File

/**
 * Clase encargada del control de ficheros de residuos.
 */
object ResiduoController {
    /**
     * Función para procesar los datos del csv y limpiar datos.
     * @param file Fichero que contiene el csv a limpiar.
     * @return Devuelve una lista de residuos.
     */
    fun loadDataFromCsv(file: File): List<ResiduoDTO> {
        val residuos: List<ResiduoDTO> = file.readLines()
            .drop(1)
            .map { it.split(";") }
            .map {
                it.map { campo -> campo.trim() }
                ResiduoDTO(
                    it[0].toInt(),
                    it[1],
                    it[2].toInt(),
                    it[3],
                    it[4].toInt(),
                    it[5],
                    it[6].replace(",", ".").toDouble()
                )
            }
        return residuos
    }

}
