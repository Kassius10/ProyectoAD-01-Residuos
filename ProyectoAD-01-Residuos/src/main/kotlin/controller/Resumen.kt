package controller

import dto.ContenedorDTO
import dto.ResiduoDTO
import exceptions.FicherosException
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
import org.jetbrains.kotlinx.dataframe.io.html
import utils.Html
import utils.Identifier
import utils.replaceAcents
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.*

private val logger = KotlinLogging.logger {}

object Resumen {
    private var directorioOrigen: String = ""
    private var directorioDestino: String = ""
    private val RESOURCES = File("").absolutePath.replaceAfter("ProyectoAD-01-Residuos", "") +
            File.separator + "src" + File.separator + "main" + File.separator + "resources"
    private lateinit var IMAGES: String
    private lateinit var CSS: String

    var residuos: List<ResiduoDTO> = mutableListOf()
    var contenedores: List<ContenedorDTO> = mutableListOf()

    /**
     * Método que procesa los datos de los ficheros csv que se obtengan y genera los archivos correspondientes.
     */
    fun parser() {
        logger.debug { "Buscando ficheros csv en el directorio origen..." }
        var files = findCSV()


        if (files.isNotEmpty()) {
            logger.debug { "Ficheros encontrados exitosamente" }
            files.forEach {
                try {
                    logger.debug { "Identificando la clase del fichero" }
                    when (Identifier.getType(it)) {
                        "residuo" -> {

                            var lista = ResiduoController.loadDataFromCsv(it)
                            var pool = Executors.newFixedThreadPool(3)

                            val futureJson = pool.submit {
                                ResiduoController.saveDataFromJson(it, lista, File(directorioDestino))
                                logger.debug { "Guardando datos de residuos en JSON..." }
                            }
                            val futureXml = pool.submit {
                                ResiduoController.saveDataFromXml(it, lista, File(directorioDestino))
                                logger.debug { "Guardando datos de residuos en XML..." }
                            }
                            val futureCsv = pool.submit {
                                ResiduoController.saveDataFromCsv(it, lista, File(directorioDestino))
                                logger.debug { "Guardando datos de residuos en CSV..." }
                            }

                            futureJson.get()
                            futureXml.get()
                            futureCsv.get()
                            pool.shutdown()

                            logger.debug { "Datos guardados correctamente" }
                        }
                        "contenedor" -> {

                            var lista = ContenedorController.loadDataFromCsv(it)
                            var pool = Executors.newFixedThreadPool(3)

                            val futureJson = pool.submit {
                                ContenedorController.saveDataFromJson(it, lista, File(directorioDestino))
                                logger.debug { "Guardando datos de contenedores en JSON..." }
                            }
                            val futureXml = pool.submit {
                                ContenedorController.saveDataFromXml(it, lista, File(directorioDestino))
                                logger.debug { "Guardando datos de contenedores en XML..." }
                            }
                            val futureCsv = pool.submit {
                                ContenedorController.saveDataFromCsv(it, lista, File(directorioDestino))
                                logger.debug { "Guardando datos de contenedores en CSV..." }
                            }

                            futureJson.get()
                            futureXml.get()
                            futureCsv.get()
                            pool.shutdown()
                            logger.debug { "Datos guardados correctamente" }
                        }
                    }
                } catch (e: Exception) {
                    throw FicherosException(e.message)
                }
            }
        } else throw FicherosException("El directorio no contiene ficheros csv.")

    }

    /**
     * Método para consultar los datos correspondientes.
     */
    fun resumen() {
        val tiempoGeneracion = System.currentTimeMillis()
        logger.debug { "Buscando ficheros necesarios para la toma de datos..." }
        Identifier.findExtension(directorioOrigen)

        if (!residuos.isEmpty() && !contenedores.isEmpty()) {
            logger.debug { "Datos obtenidos con éxito." }
            logger.debug { "Cargando datos de residuos..." }
            val dataFrame2 = residuos.toDataFrame()
            logger.debug { "Datos cargados: ${dataFrame2.rowsCount()}" }

            logger.debug { "Cargando datos de contenedores..." }
            val dataFrame = contenedores.toDataFrame()
            logger.debug { "Datos cargados: ${dataFrame.rowsCount()}" }

            logger.debug { "Generando consultas..." }
            //Consultar Número de Contenedores de cada tipo que hay en cada Distrito.
            val numeroContenedores = dataFrame.groupBy("distrito", "tipoContenedor")
                .aggregate { count() into "Numero" }.sortByDesc("distrito").drop(1)
            println(" \n Consultar Numero de Contenedores de cada tipo que hay en cada Distrito.")
            numeroContenedores.print()

            //Consultar la Media de contenedores de cada tipo que hay en cada Distrito.
            val mediaDeContenedoresPorDistrito = numeroContenedores.groupBy("tipoContenedor", "distrito")
                .aggregate { mean("Numero").toInt() into "Media de Contenedores" }
            println(" \n Consultar Media de Contenedores de cada tipo que hay en cada Distrito.")
            println(mediaDeContenedoresPorDistrito)

            //Gráfico con el total de contenedores de cada tipo que hay en cada Distrito

            var graficoContenedores = ggplot(numeroContenedores.toMap()) +
                    geomTile { x = "distrito"; y = "tipoContenedor"; fill = "Numero" } +
                    theme(panelBackground = elementBlank(), panelGrid = elementBlank()) +
                    ggtitle("Cantidad de contenedores por distrito")

            ggsave(graficoContenedores, "grafico1.png", path = IMAGES)
            logger.debug { "Generando gráfica en $IMAGES" }


            //Media de toneladaas annuales de recogidas por cada tipo de basura agrupadas por distrito
            var mediaToneladasAnualesPorDistrito = dataFrame2.groupBy("tipoResiduo", "distrito")
                .aggregate { mean("toneladas").toInt() into "TONELADAS POR DISTRITO" }.sortByDesc("distrito")
            println(" \n Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito")
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

            ggsave(graficoResiduos, "grafico2.png", path = IMAGES)
            logger.debug { "Generando gráfica en $IMAGES" }


            //  Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
            var estadisticasToneladasAnualesPorDistrito = dataFrame2.groupBy("tipoResiduo", "distrito")
                .aggregate {
                    max("toneladas") into "Maxima"
                    min("toneladas") into "Minima"
                    mean("toneladas").toInt() into "Media"
                    std("toneladas").toInt() into "Desviación"
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
                .aggregate { sum("toneladas").toInt() into "RECOGIDO" }.sortByDesc("distrito")
            println(" \n Por cada distrito obtener para cada tipo de residuo la cantidad recogida")
            println(porDistritoCantidadRecogida)

            logger.debug { "Datos consultados exitosamente." }

            logger.debug { "Generando html de resumen de distritos..." }
            generarHtmlResumen(
                tiempoGeneracion,
                numeroContenedores.html(),
                mediaDeContenedoresPorDistrito.html(),
                mediaToneladasAnualesPorDistrito.html(),
                estadisticasToneladasAnualesPorDistrito.html(),
                sumaRecogidosPorDistrito.html(),
                porDistritoCantidadRecogida.html()
            )
        } else {
            throw FileNotFoundException("Error: Falta un archivo.")
        }

    }

    /**
     * Método de resumen por distrito
     * @param distrito Distrito necesario para realizar las consultas sobre él
     */
    fun resumenDistrito(dis: String) {
        val tiempoGeneracion = System.currentTimeMillis()
        logger.debug { "Buscando ficheros necesarios para la toma de datos..." }
        Identifier.findExtension(directorioOrigen)

        if (!residuos.isEmpty() && !contenedores.isEmpty()) {
            logger.debug { "Datos obtenidos con éxito." }
            var distrito = replaceAcents(dis).uppercase()

            logger.debug { "Cargando datos de residuos..." }
            val dataFrameResiduos =
                residuos.toDataFrame().update { it["distrito"] }.with { replaceAcents(it.toString().uppercase()) }
            dataFrameResiduos.cast<ResiduoDTO>()
            logger.debug { "Datos cargados: ${dataFrameResiduos.rowsCount()}" }

            logger.debug { "Cargando datos de contenedores..." }
            val dataFrameContenedor =
                contenedores.toDataFrame().update { it["distrito"] }.with { replaceAcents(it.toString().uppercase()) }
            dataFrameContenedor.cast<ContenedorDTO>()
            logger.debug { "Datos cargados: ${dataFrameContenedor.rowsCount()}" }


            logger.debug { "Comprobando que el distrito exista: $distrito" }
            val existeResiduo = dataFrameResiduos.filter { it["distrito"] == distrito }
            val existeContenedor = dataFrameContenedor.filter { it["distrito"] == distrito }

            if (existeResiduo.rowsCount() > 0 && existeContenedor.rowsCount() > 0) {

                logger.debug { "Generando consultas..." }
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
                logger.debug { "Generando gráfica en $IMAGES" }


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
                logger.debug { "Generando gráfica en $IMAGES" }
                logger.debug { "Datos consultados exitosamente." }

                logger.debug { "Generando html de resumen distrito..." }
                generarHtmlResumenDistrito(
                    tiempoGeneracion,
                    distrito,
                    tipoContenedoresDistrito.html(),
                    totalToneladasPorResiduoDistrito.html(),
                    estadisticaPorMesResiduoDistrito.html()
                )


            } else {
                throw IllegalStateException("Error: No existe el distrito")
            }

        } else {
            throw FileNotFoundException("Error: Falta un archivo.")
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
                throw FileNotFoundException("No existe el directorio: $dir")
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
     * Método que genera el html de resumen distrito.
     * @param distrito Distrito del resumen
     * @param tipoContenedoresDistrito Primera consulta
     * @param totalToneladasPorResiduoDistrito Segunda consulta
     * @param estadisticaPorMesResiduoDistrito Tercera consulta
     */
    private fun generarHtmlResumenDistrito(
        tiempoGeneracion: Long,
        distrito: String,
        tipoContenedoresDistrito: String,
        totalToneladasPorResiduoDistrito: String,
        estadisticaPorMesResiduoDistrito: String,
    ) {
        var html = Html()
        var fileHtml = File("$directorioDestino${File.separator}resumenDistrito.html")
        fileHtml.bufferedWriter(Charset.forName("UTF-8")).use {
            it.write(
                html.generateResumenDistritoHtml(
                    tiempoGeneracion,
                    distrito,
                    tipoContenedoresDistrito,
                    totalToneladasPorResiduoDistrito,
                    estadisticaPorMesResiduoDistrito
                )
            )
        }
        logger.debug { "Fichero html generado correctamente en: $fileHtml" }

        var fileCss = FileWriter("$CSS${File.separator}css.css")
        fileCss.write(html.generateCss())
        fileCss.close()
    }

    /**
     * Método que genera el html de resumen.
     * @param numeroContenedoresTipo Primera consulta
     * @param mediaContenedoresTipo Segunda consulta
     * @param mediaToneladasAnuales Tercera consulta
     * @param maxMinMediaToneladasAnuales Cuarta consulta
     * @param sumaRecogidosPorDistrito Quinta consulta
     * @param porDistritoCantidadRecogida Sexta consulta
     */
    private fun generarHtmlResumen(
        tiempoGeneracion: Long,
        numeroContenedoresTipo: String,
        mediaContenedoresTipo: String,
        mediaToneladasAnuales: String,
        maxMinMediaToneladasAnuales: String,
        sumaRecogidosPorDistrito: String,
        porDistritoCantidadRecogida: String,
    ) {
        var html = Html()
        var fileHtml = File("$directorioDestino${File.separator}resumenDistritos.html")
        fileHtml.bufferedWriter(Charset.forName("UTF-8")).use {
            it.write(
                html.generateResumenHtml(
                    tiempoGeneracion,
                    numeroContenedoresTipo,
                    mediaContenedoresTipo,
                    mediaToneladasAnuales,
                    maxMinMediaToneladasAnuales,
                    sumaRecogidosPorDistrito,
                    porDistritoCantidadRecogida
                )
            )
        }
        logger.debug { "Fichero html generado correctamente en: $fileHtml" }

        var fileCss = FileWriter("$CSS${File.separator}css.css")
        fileCss.write(html.generateCss())
        fileCss.close()

    }

}