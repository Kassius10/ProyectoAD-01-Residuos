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
import org.jetbrains.kotlinx.dataframe.io.DisplayConfiguration
import java.io.File

object Resumen {
    private val logger = KotlinLogging.logger {}
    var residuos: List<ResiduoDTO> = mutableListOf()
    var contenedores: List<ContenedorDTO> = mutableListOf()

    /**
     * Método que procesa los datos de los ficheros csv que se obtengan y genera los archivos correspondientes.
     * @param files Lista de ficheros que hay dentro de la carpeta origen indicada.
     * @param directorioDestino Directorio destino donde se almacenaran los nuevos ficheros.
     */
    fun parser(files: List<File>?, directorioDestino: File) {
        files?.forEach {
            println("Repasamos los ficheros")
            try {
                var tipo = Identifier.getType(it)
                when (tipo) {
                    "residuo" -> {
                        var lista = ResiduoController.loadDataFromCsv(it)
                        ResiduoController.saveDataFromJson(it, lista, directorioDestino)
                        ResiduoController.saveDataFromXml(it, lista, directorioDestino)
                    }
                    "contenedor" -> {
                        var lista = ContenedorController.loadDataFromCsv(it)
                        ContenedorController.saveDataFromJson(it, lista, directorioDestino)
                        ContenedorController.saveDataFromXml(it, lista, directorioDestino)
                    }
                }

            } catch (e: Exception) {
                println(e.message)
            }

        }
    }

    /**
     * Método para consultar los datos correspondientes.
     */
    fun resumen() {
        if (!residuos.isEmpty() && !contenedores.isEmpty()) {
            DisplayConfiguration.DEFAULT.rowsLimit = 5
            val dataFrame2 = residuos.toDataFrame()

            val dataFrame = contenedores.toDataFrame()


            //Consultar Numero de Contenedores de cada tipo que hay en cada Distrito.
            val numeroContenedores = dataFrame.groupBy("distrito", "tipoContenedor")
                .aggregate { count() into "Numero" }.sortByDesc("distrito").drop(1)
            println(" \n Consultar Numero de Contenedores de cada tipo que hay en cada Distrito.")
            numeroContenedores.print()

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
        } else {
            println("Error: Falta un archivo.")
        }

    }

    fun resumenDistrito(distrito: String) {
        if (!residuos.isEmpty() && !contenedores.isEmpty()) {
            val dataFrameResiduos = residuos.toDataFrame().update { it["distrito"] }.with { it.toString().uppercase() }
            dataFrameResiduos.cast<ResiduoDTO>()
            val dataFrameContenedor = contenedores.toDataFrame()
            dataFrameContenedor.cast<ContenedorDTO>()


            val existeResiduo = dataFrameResiduos.filter { it["distrito"] == distrito }
            val existeContenedor = dataFrameContenedor.filter { it["distrito"] == distrito }

            if (existeResiduo.rowsCount() > 0 && existeContenedor.rowsCount() > 0) {

                val tipoContenedoresDistrito = existeContenedor.groupBy("tipoContenedor", "distrito")
                    .aggregate {
                        count() into "Total"
                    }
                println("Número de contenedores de cada tipo que hay en: $distrito")
                tipoContenedoresDistrito.print()

                val totalToneladasPorResiduoDistrito = existeResiduo.groupBy("tipoResiduo", "distrito")
                    .aggregate { sum("toneladas").toFloat() into "Total" }
                println("Total de toneladas recogidas por residuo en: $distrito")
                totalToneladasPorResiduoDistrito.print()

                var fig: Plot = ggplot(totalToneladasPorResiduoDistrito.toMap()) +
                        geomTile { x = "distrito"; y = "tipoResiduo"; fill = "Total" } +
                        theme(panelBackground = elementBlank(), panelGrid = elementBlank()) +
                        scaleFillGradient("#d2b4de", "#5b2c6f") +
                        ggsize(700, 400) +
                        labs(
                            x = "Distrito",
                            y = "Tipo de Residuo",
                            title = "Total de toneladas por Residuos en $distrito"
                        )
                ggsave(fig, "totalToneladasPorResiduos$distrito.png")


                val estadisticaPorMesResiduoDistrito = existeResiduo.groupBy("month", "distrito")
                    .aggregate {
                        max("toneladas") into "Max"
                        min("toneladas") into "Min"
                        mean("toneladas").toFloat() into "Media"
                        std("toneladas") into "Desviación"
                    }
                println("Estadística de residuos por mes en: $distrito")
                estadisticaPorMesResiduoDistrito.print()


                fig = letsPlot(data = estadisticaPorMesResiduoDistrito.toMap()) + geomBar(
                    stat = Stat.identity,
                    alpha = 1,
                    fill = "#117a65"
                ) {
                    x = "month"; y = "Max"
                } + geomBar(
                    stat = Stat.identity,
                    alpha = 0.8,
                    fill = "#138d75"
                ) {
                    x = "month"; y = "Media"
                } + geomBar(
                    stat = Stat.identity,
                    alpha = 0.6,
                    fill = "#45b39d"
                ) {
                    x = "month"; y = "Desviación"
                } + geomBar(
                    stat = Stat.identity,
                    alpha = 0.3,
                    fill = "#0b5345"
                ) {
                    x = "month"; y = "Min"
                } + labs(
                    x = "Meses",
                    y = "Estadística",
                    title = "Estadística de residuos por mes en $distrito"
                )
                ggsave(fig, "estadisticasResiduosPorMes$distrito.png")

            } else {
                println("Error: No existe el distrito")
            }

        } else {
            println("Error: Falta un archivo.")
        }
    }


}