package storage

import models.Month
import models.Residuo
import models.TipoResiduo
import java.io.File

class ResiduosStorageCsv {
    companion object {
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
            return residuos
        }
    }
}