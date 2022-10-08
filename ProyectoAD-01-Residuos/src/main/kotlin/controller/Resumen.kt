package controller

import Identifier
import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object Resumen {
    private val logger = KotlinLogging.logger {}
//    private lateinit var file: File

    fun procesarData(directorioOrigen: String, directorioDestino: String) {
        logger.info("Procesando los datos...")
        if (Files.isDirectory(Paths.get(directorioOrigen))) {
            logger.info("Accediendo al directorio origen...")
        } else {
            logger.error("No existe el directorio: $directorioOrigen")
        }

//        val residuos: List<ResiduoDTO> = ResiduoController.loadDataFromCsv(File(file))
//        val dataFrame = residuos.toDataFrame()
//        println(dataFrame.schema())
    }

    fun parser(files: List<File>?) {
        files?.forEach {
            println("Repasamos los ficheros")
//            var lista = Identifier.isCSV(it)
//            ResiduoController.writeJson(it, lista)
            var lista = Identifier.isJSON(it)
            lista.forEach { println(it) }

        }
    }

    fun resumen() {

    }

    fun resumenDistrito(distrito: String) {

    }


}