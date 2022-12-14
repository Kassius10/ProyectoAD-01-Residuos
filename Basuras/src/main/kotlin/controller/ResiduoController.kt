package controller

import dto.ResiduoDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.nio.charset.Charset

private val looger = KotlinLogging.logger { }

/**
 * Clase encargada del control de ficheros de residuos.
 */
object ResiduoController {
    /**
     * Función para procesar los datos de un csv.
     * @param file Fichero que contiene el csv a procesar.
     * @return Devuelve una lista de residuosDto.
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

    /**
     * Función para procesar los datos de un json.
     * @param file Fichero que contiene el json a procesar.
     * @return Devuelve una lista de ResiduosDto.
     */
    fun loadDataFromJson(file: File): List<ResiduoDTO> {
        return Json.decodeFromString(file.readText())
    }

    /**
     * Función para procesar los datos de un xml.
     * @param file Fichero que contiene el xml a procesar.
     * @return Devuelve una lista de ResiduosDto.
     */
    fun loadDataFromXml(file: File): List<ResiduoDTO> {
        return XML.decodeFromString(file.readText())
    }

    /**
     * Función para almacenar los datos de residuos en un fichero csv.
     * @param file Fichero necesario para indicar como almacenar los datos.
     * @param residuos Lista de residuos.
     * @param directorioDestino Directorio donde se va a almacenar.
     */
    fun saveDataFromCsv(file: File, residuos: List<ResiduoDTO>, directorioDestino: File) {
        val fileCSV = directorioDestino.absolutePath + File.separator + file.name
        looger.debug { "Ruta del nuevo fichero: $fileCSV" }
        File(fileCSV).bufferedWriter(Charset.forName("UTF-8")).use { out ->
            out.write("Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas")
            out.newLine()
            residuos.forEach {
                out.write(it.toString(";"))
                out.newLine()
            }
        }
    }


    /**
     * Función para almacenar los datos de residuos en un fichero json.
     * @param file Fichero necesario para indicar como almacenar los datos.
     * @param residuos Lista de residuos.
     * @param directorioDestino Directorio donde se va a almacenar.
     */
    fun saveDataFromJson(file: File, residuos: List<ResiduoDTO>, directorioDestino: File) {
        val fileJson = directorioDestino.absolutePath + File.separator + file.name.replace(".csv", ".json")
        looger.debug { "Ruta del nuevo fichero: $fileJson" }
        val json = Json { prettyPrint = true }
        File(fileJson).writeText(json.encodeToString(residuos))
    }

    /**
     * Función para almacenar los datos de residuos en un fichero xml.
     * @param file Fichero necesario para indicar como almacenar los datos.
     * @param residuos Lista de contenedores.
     * @param directorioDestino Directorio donde se va a almacenar.
     */
    fun saveDataFromXml(file: File, residuos: List<ResiduoDTO>, directorioDestino: File) {
        val fileXml = directorioDestino.absolutePath + File.separator + file.name.replace(".csv", ".xml")
        looger.debug { "Ruta del nuevo fichero: $fileXml" }
        val xml = XML { indent = 4 }
        File(fileXml).writeText(xml.encodeToString(residuos))
    }

}
