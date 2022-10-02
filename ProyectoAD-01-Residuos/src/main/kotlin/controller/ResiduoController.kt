package controller

import models.Residuo
import mu.KotlinLogging
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import storage.ResiduosStorageCsv
import java.io.File

object ResiduoController {
    private val logger = KotlinLogging.logger {}

    fun procesarData() {
        logger.info("Procesando los datos...")
        var file = System.getProperty("user.dir") + File.separator + ("src") +
                File.separator + ("main") +
                File.separator + ("resources") + File.separator + ("modelo_residuos_2021.csv")

        val residuos: List<Residuo> = ResiduosStorageCsv.loadDataFromCsv(File(file))
        val dataFrame = residuos.toDataFrame()
        println(dataFrame.schema())
    }
}