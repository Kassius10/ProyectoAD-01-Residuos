package controller

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
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.html
import utils.Html
import utils.Identifier
import utils.formatToString
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

object Resumen {
    private var directorioOrigen: String = ""
    private var directorioDestino: String = ""
    private val RESOURCES =
        "${System.getProperty("user.dir")}${File.separator}src${File.separator}main${File.separator}resources"
    private lateinit var IMAGES: String
    private lateinit var CSS: String

    var residuos: List<ResiduoDTO> = mutableListOf()
    var contenedores: List<ContenedorDTO> = mutableListOf()

    /**
     * Método que procesa los datos de los ficheros csv que se obtengan y genera los archivos correspondientes.
     */
    fun parser() {
        var files = findCSV()

        if (files.isNotEmpty()) {

            files.forEach {
                println("Repasamos los ficheros")
                try {
                    when (Identifier.getType(it)) {
                        "residuo" -> {
                            var lista = ResiduoController.loadDataFromCsv(it)
                            ResiduoController.saveDataFromJson(it, lista, File(directorioDestino))
                            ResiduoController.saveDataFromXml(it, lista, File(directorioDestino))
                            ResiduoController.saveDataFromCsv(it, lista, File(directorioDestino))

                        }
                        "contenedor" -> {
                            var lista = ContenedorController.loadDataFromCsv(it)
                            ContenedorController.saveDataFromJson(it, lista, File(directorioDestino))
                            ContenedorController.saveDataFromXml(it, lista, File(directorioDestino))
                            ContenedorController.saveDataFromCsv(it, lista, File(directorioDestino))
                        }
                    }
                } catch (e: Exception) {
                    println("Error: " + e.message)
                }
            }
        } else throw IllegalStateException("El directorio no contiene ficheros csv.")


    }

    /**
     * Método para consultar los datos correspondientes.
     */
    fun resumen() {
        Identifier.findExtension(directorioOrigen)

        if (!residuos.isEmpty() && !contenedores.isEmpty()) {
            val dataFrame2 = residuos.toDataFrame()
            val dataFrame = contenedores.toDataFrame()


            //Consultar Número de Contenedores de cada tipo que hay en cada Distrito.
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
            throw IllegalStateException("Error: Falta un archivo.")
        }

    }

    /**
     * Método de resumen por distrito
     * @param distrito Distrito necesario para realizar las consultas sobre él
     */
    fun resumenDistrito(distrito: String) {
        Identifier.findExtension(directorioOrigen)

        if (!residuos.isEmpty() && !contenedores.isEmpty()) {
            val dataFrameResiduos = residuos.toDataFrame().update { it["distrito"] }.with { it.toString().uppercase() }
            dataFrameResiduos.cast<ResiduoDTO>()
            val dataFrameContenedor = contenedores.toDataFrame()
            dataFrameContenedor.cast<ContenedorDTO>()


            val existeResiduo = dataFrameResiduos.filter { it["distrito"] == distrito }
            val existeContenedor = dataFrameContenedor.filter { it["distrito"] == distrito }

            if (existeResiduo.rowsCount() > 0 && existeContenedor.rowsCount() > 0) {

                val tipoContenedoresDistrito = existeContenedor.groupBy("tipoContenedor", "distrito")
                    .aggregate { count() into "Total" }
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
                ggsave(fig, "totalToneladasPorResiduos$distrito.png", path = IMAGES)


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
                ggsave(fig, "estadisticasResiduosPorMes$distrito.png", path = IMAGES)

                generarHtmlResumenDistrito(
                    distrito,
                    tipoContenedoresDistrito,
                    totalToneladasPorResiduoDistrito,
                    estadisticaPorMesResiduoDistrito
                )


            } else {
                throw IllegalStateException("Error: No existe el distrito")
            }

        } else {
            throw IllegalStateException("Error: Falta un archivo.")
        }
    }

    /**
     * Método para comprobar si los directorios existen y almacenarlos después.
     * @param args Parámetros indicados por consola
     */
    fun getDirectories(args: Array<String>) {
        var directories = args.takeLast(2)

        for (dir in directories) {
            if (!Files.isDirectory(Paths.get(dir))) {
                throw IllegalStateException("No existe el directorio: $dir")
            }
        }
        directorioOrigen = directories[0]
        directorioDestino = directories[1]

    }

    /**
     * Método para obtener los ficheros que contenga el directorio origen.
     * @return Devuelve una lista de ficheros si existen.
     */
    private fun findCSV(): List<File> {
        return File(directorioOrigen).listFiles()!!.filter { it.absolutePath.contains(".csv") }
    }

    /**
     * Método que crea los directorios css e img en la ruta destino.
     */
    fun createDirectoryImagesAndCSS() {
        var imagen = "$RESOURCES${File.separator}img${File.separator}logo.jpg"
        IMAGES = "$directorioDestino${File.separator}img${File.separator}"
        if (Files.notExists(Paths.get(IMAGES))) {
            Files.createDirectory(Paths.get(IMAGES))
        }
        CSS = "$directorioDestino${File.separator}css"
        if (Files.notExists(Paths.get(CSS))) {
            Files.createDirectory(Paths.get(CSS))
        }
        File(imagen).copyTo(File("$IMAGES${File.separator}logo.jpg"), true)
    }

    /**
     * Método que genera el html.
     */
    private fun generarHtmlResumenDistrito(
        distrito: String,
        tipoContenedoresDistrito: DataFrame<ContenedorDTO>,
        totalToneladasPorResiduoDistrito: DataFrame<ResiduoDTO>,
        estadisticaPorMesResiduoDistrito: DataFrame<ResiduoDTO>
    ) {
        var html = Html(
            LocalDateTime.now().formatToString(),
            distrito,
            tipoContenedoresDistrito.html(),
            totalToneladasPorResiduoDistrito.html(),
            estadisticaPorMesResiduoDistrito.html()
        )
        var fileHtml = FileWriter("$directorioDestino${File.separator}resumenDistrito.html")
        fileHtml.write(html.generateResumenDistritoHtml())
        fileHtml.close()

        var fileCss = FileWriter("$CSS${File.separator}css.css")
        fileCss.write(html.generateCss())
        fileCss.close()
    }

}