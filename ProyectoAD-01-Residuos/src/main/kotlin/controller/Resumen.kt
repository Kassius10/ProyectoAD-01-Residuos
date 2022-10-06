package controller

import dto.ResiduoDTO
import mu.KotlinLogging
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object Resumen {
    private val logger = KotlinLogging.logger {}

    fun procesarData(directorioOrigen: String, directorioDestino: String) {
        logger.info("Procesando los datos...")
        if (Files.isDirectory(Paths.get(directorioOrigen))) {
            logger.info("Accediendo al directorio origen...")
        } else {
            logger.error("No existe el directorio: $directorioOrigen")
        }

        var file = "C:\\Users\\danii\\Desktop\\ProyectoAd\\ProyectoAD-01-Residuos" + File.separator + ("src") +
                File.separator + ("main") +
                File.separator + ("resources") + File.separator + ("modelo_residuos_2021.csv")


        val residuos: List<ResiduoDTO> = ResiduoController.loadDataFromCsv(File(file))
        val dataFrame = residuos.toDataFrame()
        println(dataFrame.schema())
    }

    fun parser() {

    }

    fun resumen() {

    }

    fun resumenDistrito(distrito: String) {

    }

    private fun isCorrect(file: File): Boolean {
        val residuos: String = file.readLines()[0].substring(1)
        return residuos.equals("Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas")
    }


}