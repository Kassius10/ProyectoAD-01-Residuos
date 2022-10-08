package controller

import dto.ResiduoDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
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

    fun writeJson(file: File, residuos: List<ResiduoDTO>) {
        var fileJson = file.absolutePath.replace(".csv", ".json")
        println("Ruta del nuevo fichero: $fileJson")
        val json = Json { prettyPrint = true }
        File(fileJson).writeText(json.encodeToString(residuos))
        println("Fichero creado")
    }

    fun readJson(file: File): List<ResiduoDTO> {
        return Json.decodeFromString(file.readText())
    }

    fun readXml(file: File): List<ResiduoDTO> {
        return XML.decodeFromString(file.readText())
    }

    fun writeXml(file: File) {

    }

}
