package controller

import Identifier
import dto.ContenedorDTO
import dto.ResiduoDTO
import jetbrains.letsPlot.*
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomBar
import jetbrains.letsPlot.geom.geomTile
import jetbrains.letsPlot.intern.Plot
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.label.labs
import jetbrains.letsPlot.scale.scaleFillGradient
import mu.KotlinLogging
import org.jetbrains.kotlinx.dataframe.api.*
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
            try {
                var tipo = Identifier.isCSV(it)

                when (tipo) {
                    "residuos" -> {
                        var lista = ResiduoController.loadDataFromCsv(it)
                        ResiduoController.writeJson(it, lista)
                        ResiduoController.writeXml(it, lista)
                    }
                    "contenedor" -> {
                        var lista = ContenedorController.loadDataFromCsv(it)
                        ContenedorController.writeJson(it, lista)
                        ContenedorController.writeXml(it, lista)
                    }
                }

            } catch (e: Exception) {
                println(e.message)
            }

        }
    }

    fun resumen(file: File) {
        val residuos: List<ResiduoDTO> = ResiduoController.loadDataFromCsv(file)
        val dataFrame2 = residuos.toDataFrame()


        val contenedores: List<ContenedorDTO> = ContenedorController.loadDataFromCsv(file)
        val dataFrame = contenedores.toDataFrame()


        //Consultar Numero de Contenedores de cada tipo que hay en cada Distrito.
        val numeroContenedores = dataFrame.groupBy("distrito", "tipoContenedor")
            .aggregate { count() into "Numero" }.sortByDesc("distrito").drop(1)
        println(" \n Consultar Numero de Contenedores de cada tipo que hay en cada Distrito.")
        println(numeroContenedores)

        //Consultar la Media de contenedores de cada tipo que hay en cada Distrito.

        val mediaDeContenedoresPorDistrito = numeroContenedores.groupBy("tipoContenedor")
            .aggregate { mean("Numero").toInt() into "Media de Contenedores" }
        println(" \n Consultar Media de Contenedores de cada tipo que hay en cada Distrito.")
        println(mediaDeContenedoresPorDistrito)

        //Gráfico con el total de contenedores de cada tipo que hay en cada Distrito

        var graficoContenedores = ggplot(numeroContenedores.toMap()) +
                geomTile { x = "distrito"; y = "tipoContenedor"; fill = "Numero" } +
                theme(panelBackground = elementBlank(), panelGrid = elementBlank())
        scaleFillGradient("#00BCD4", "#009688") +
                ggsize(900, 400) +
                ggtitle("Cantidad de contenedores por distrito")

        ggsave(graficoContenedores, "grafico1.png")


        //Media de toneladaas annuales de recogidas por cada tipo de basura agrupadas por distrito
        var mediaToneladasAnualesPorDistrito = dataFrame2.groupBy("tipoResiduo", "distrito")
            .aggregate { mean("toneladas").toInt() into "TONELADAS POR DIISTRITO" }.sortByDesc("distrito")
        println(" \n Media de toneladaas annuales de recogidas por cada tipo de basura agrupadas por distrito")
        println(mediaToneladasAnualesPorDistrito)

        //Gráfico de media de Toneladas mensuales recogidas de basura por distrito
        var mediaToneladasMensualesPorDistrito = dataFrame2.groupBy("distrito", "month")
            .aggregate { mean("toneladas").toInt() into "Toneladas por mes" }


        var graficoResiduos: Plot = letsPlot(data = mediaToneladasMensualesPorDistrito.toMap()) +
                geomBar(stat = Stat.identity, alpha = 1) {
                    x = "distrito";y = "Toneladas por mes"
                } + labs(
            x = "distrito",
            y = "Toneladas por mes",
            title = "Grafico Residuos"
        )

        ggsave(graficoResiduos, "grafico2.png")


        //  Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
        var estadisticasToneladasAnualesPorDistrito = dataFrame2.groupBy("tipoResiduo", "distrito")
            .aggregate {
                max("toneladas") into "Maxima"
                min("toneladas") into "Minima"
                mean("toneladas").toInt() into "Media"
            }.sortByDesc("distrito")
        println(" \n Máximo, mínimo, media y desviación de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito")
        println(estadisticasToneladasAnualesPorDistrito)

        // Suma de lo recogido en un año por distrito
        var sumaRecogidosPorDistrito = dataFrame2.groupBy("distrito")
            .aggregate { sum("toneladas").toInt() into "RECOGIDO EN UN AÑO" }
        println(" \n Suma de lo recogido en un año por distrito")
        println(sumaRecogidosPorDistrito)


        // Por cada distrito obtener para cada tipo de residuo la cantidad recogida.
        var porDistritoCantidadRecogida = dataFrame2.groupBy("tipoResiduo", "distrito")
            .aggregate { sum("toneladas").toInt() into "RECOGIDO" }
        println(" \n Por cada distrito obtener para cada tipo de residuo la cantidad recogida")
        println(porDistritoCantidadRecogida)

    }

    fun resumenDistrito(distrito: String) {

    }


}