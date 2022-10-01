package controller

import models.Residuo
import models.loadDataFromCsv
import mu.KotlinLogging
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import java.io.File

object ContenedorController {
    private val logger = KotlinLogging.logger {}

    fun procesarData() {
        logger.info("Procesando los datos...")
        var file = System.getProperty("user.dir") + File.separator + ("src") +
                File.separator + ("main") +
                File.separator + ("resources") + File.separator + ("modelo_residuos_2021.csv")

        val residuos: List<Residuo> = loadDataFromCsv(File(file))
        val dataFrame = residuos.toDataFrame()
        println(dataFrame.schema())
    }
}