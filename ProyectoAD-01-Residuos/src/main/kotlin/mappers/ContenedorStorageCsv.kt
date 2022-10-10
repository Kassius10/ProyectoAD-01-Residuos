package mappers

import models.Contenedor
import models.TipoContenedor
import java.io.File

class ContenedorStorageCsv {
    companion object {
        /**
         * Función para procesar los datos del csv y limpiar datos.
         * @param file Fichero que contiene el csv a limpiar.
         * @return Devuelve una lista de residuos.
         */
        fun loadDataFromCsv(file: File): List<Contenedor> {
            val contenedores: List<Contenedor> = file.readLines()
                .drop(1)
                .map { it.split(";") }
                .map {
                    it.map { campo -> campo.trim() }
                    Contenedor(
                        it[0].toLong(),
                        TipoContenedor.valueOf(it[1].replace("-", "_")),
                        it[2],
                        it[3],
                        it[5].toInt(),
                        it[6],
                        it[15]
                    )
                }
            return contenedores
        }
    }
}