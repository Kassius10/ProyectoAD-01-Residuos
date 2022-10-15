package controller

import dto.ContenedorDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.nio.charset.Charset

object ContenedorController {

    /**
     * Función para procesar los datos de un csv.
     * @param file Fichero que contiene el csv a procesar.
     * @return Devuelve una lista de ContenedorDto.
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
                    if (it[7] != "") it[7] else "No disponible",
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

    /**
     * Función para procesar los datos de un json.
     * @param file Fichero que contiene el json a procesar.
     * @return Devuelve una lista de ContenedorDto.
     */
    fun loadDataFromJson(file: File): List<ContenedorDTO> {
        return Json.decodeFromString(file.readText())
    }

    /**
     * Función para procesar los datos de un xml.
     * @param file Fichero que contiene el xml a procesar.
     * @return Devuelve una lista de ContenedorDto.
     */
    fun loadDataFromXml(file: File): List<ContenedorDTO> {
        return XML.decodeFromString(file.readText())
    }

    /**
     * Función para almacenar los datos de contenedores en un fichero csv.
     * @param file Fichero necesario para indicar como almacenar los datos.
     * @param contenedores Lista de contenedores.
     * @param directorioDestino Directorio donde se va a almacenar.
     */
    fun saveDataFromCsv(file: File, contenedores: List<ContenedorDTO>, directorioDestino: File) {
        val fileCSV = directorioDestino.absolutePath + File.separator + file.name
        println("Ruta del nuevo fichero: $fileCSV")
        File(fileCSV).bufferedWriter(Charset.forName("UTF-8")).use { out ->
            out.write("Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION")
            out.newLine()
            contenedores.forEach {
                out.write(it.toString(";"))
                out.newLine()
            }
        }
        println("Fichero creado")
    }

    /**
     * Función para almacenar los datos de contenedores en un fichero json.
     * @param file Fichero necesario para indicar como almacenar los datos.
     * @param contenedores Lista de contenedores.
     * @param directorioDestino Directorio donde se va a almacenar.
     */
    fun saveDataFromJson(file: File, contenedores: List<ContenedorDTO>, directorioDestino: File) {
        val fileJson = directorioDestino.absolutePath + File.separator + file.name.replace(".csv", ".json")
        println("Ruta del nuevo fichero: $fileJson")
        val json = Json { prettyPrint = true }
        File(fileJson).writeText(json.encodeToString(contenedores))
        println("Fichero creado")
    }

    /**
     * Función para almacenar los datos de contenedores en un fichero xml.
     * @param file Fichero necesario para indicar como almacenar los datos.
     * @param contenedores Lista de contenedores.
     * @param directorioDestino Directorio donde se va a almacenar.
     */
    fun saveDataFromXml(file: File, contenedores: List<ContenedorDTO>, directorioDestino: File) {
        val fileXml = directorioDestino.absolutePath + File.separator + file.name.replace(".csv", ".xml")
        println("Ruta del nuevo fichero: $fileXml")
        val xml = XML { indent = 4 }
        File(fileXml).writeText(xml.encodeToString(contenedores))
        println("Fichero creado")
    }
}