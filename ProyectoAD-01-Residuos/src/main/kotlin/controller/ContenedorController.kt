package controller

import dto.ContenedorDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File

object ContenedorController {
    private val logger = KotlinLogging.logger {}

    /**
     * Función para procesar los datos del csv y limpiar datos.
     * @param file Fichero que contiene el csv a limpiar.
     * @return Devuelve una lista de residuos.
     */
    fun loadDataFromCsv(file: File): List<ContenedorDTO> {
        val contenedores: List<ContenedorDTO> = file.readLines()
            .drop(1)
            .map { it.split(";") }
            .map {
                it.map { campo -> campo.trim() }
                ContenedorDTO(
                    it[0].toLong(),
                    it[1],
                    it[2],
                    it[3],
                    it[4].toInt(),
                    it[5].toInt(),
                    it[6],
                    if (it[7] != null) it[7] else "No disponible",
                    it[8],
                    it[9],
                    if (it[10] != "") it[10].toInt() else 0,
                    it[11],
                    it[12],
                    it[13],
                    it[14],
                    it[15]
                )
            }
        return contenedores
    }

    fun writeJson(file: File, residuos: List<ContenedorDTO>) {
        var fileJson = file.absolutePath.replace(".csv", ".json")
        println("Ruta del nuevo fichero: $fileJson")
        val json = Json { prettyPrint = true }
        File(fileJson).writeText(json.encodeToString(residuos))
        println("Fichero creado")
    }

    fun writeXml(file: File, residuos: List<ContenedorDTO>) {
        var fileXml = file.absolutePath.replace(".csv", ".xml")
        println("Ruta del nuevo fichero: $fileXml")
        val xml = XML { indent = 4 }
        File(fileXml).writeText(xml.encodeToString(residuos))
    }
}

