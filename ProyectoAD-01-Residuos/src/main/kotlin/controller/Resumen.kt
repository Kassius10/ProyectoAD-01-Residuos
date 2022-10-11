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
import org.jetbrains.kotlinx.dataframe.io.html
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime

object Resumen {
    private val logger = KotlinLogging.logger {}
    private val RESOURCES = System.getProperty("user.dir")+"${File.separator}src${File.separator}" +
            "main${File.separator}resources"
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
    fun resumen(){
        if (!residuos.isEmpty() && !contenedores.isEmpty()) {
            val dataFrame2 = residuos.toDataFrame()

            val dataFrame = contenedores.toDataFrame()

            val generated = LocalDateTime.now().toString()


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

            ggsave(graficoContenedores, "grafico1.png", path = "$RESOURCES${File.separator}img")


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

            ggsave(graficoResiduos, "grafico2.png", path = "$RESOURCES${File.separator}img")


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


            val resumen = creaciónResumenHtml(generated,numeroContenedores.html(),mediaDeContenedoresPorDistrito.html(),
                mediaToneladasAnualesPorDistrito.html(),mediaToneladasMensualesPorDistrito.html(),estadisticasToneladasAnualesPorDistrito.html(),sumaRecogidosPorDistrito.html()
               )
            //Escribimos el html con el FileWriter
            val html = FileWriter("$RESOURCES${File.separator}resumen.html")
            html.write(resumen)
            html.close()




        } else {
            println("Error: Falta un archivo.")
        }





    }

    /**
     * Función para creacion del Esqueleto del HTML.
     * @return Devuelve el esqueleto del HTML con los datos introducidos anteriormente.
     */
    fun creaciónResumenHtml(fechaGeneracion:String, numeroContenedoresTipo:String,mediaContenedoresTipo:String,
                            mediaToneladasAnuales:String,maxMinMediaToneladasAnuales:String,sumaRecogidosPorDistrito:String,
                            porDistritoCantidadRecogida:String):String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href=".${File.separator}resources${File.separator}css${File.separator}css.css">
            <title>Resumen Distritos Madrid</title>
            </head>
            <body>
             <img id="logo" src=".${File.separator}resources${File.separator}img${File.separator}logo.jpg">
             
             <h1 id ="titulo">Resumen de Recogidas y Reciclaje de contenendores en los distritos de Madrid</h1>
            
             <h2 id="subtituloUno"> Generacion del html: $fechaGeneracion</h2>
             <br>
             <h3 id="subtituloDos">Construido por: Daniel Carmona y Jeremy Ramos</h3>
             
             <div id="contenedor">
             
              <h4 id="subtituloTres"> Numero de contenedores de Cada tipo que hay en cada Distrito </h4>
               
                  <div id="minicontainer">
                  <p> $numeroContenedoresTipo</p>
                  </div>
                        <br>
              
                  <h4> Media de contenedores de cada tipo que hay en cada Distrito </h4>
                  <div id="minicontainer">
                  <p> $mediaContenedoresTipo</p>
                  </div>
                        <br>
               
                  <h4> Media de Toneladas Anuales de recogidas por cada tipo de basura Agrupada por Distrito </h4>
                  <div id="minicontainer">
                  <p> $mediaToneladasAnuales </p>
                  </div>
              
              
                  <h4> Máximo, Mínimo, Media y Desviación de toneladas anuales de recogidas por cada tipo de basura agrupadas por Distrito </h4>
                  <div id="minicontainer">
                  <p> $maxMinMediaToneladasAnuales </p> 
                  </div>  
                       <br>
               
                  <div id="minicontainer">     
                  <h4> Suma de todo lo recogido en un año por Distrito </h4>
                  <p> $sumaRecogidosPorDistrito </p>
                  </div>
                      <br>
              
                  <h4> Por cada distrito  obtener para cada tipo de residuo la cantidad recogida. </h4>
                  <div id="minicontainer">
                  <p> $porDistritoCantidadRecogida </p>
                  </div>
              
                <div id="containerimg">
                      <div id="imagenuno">
                          <h4 id="h4moded"> Gráfico con el total de contenedores por distrito </h4>
                          <img src=".${File.separator}resources${File.separator}img${File.separator}grafico1.png">
                      </div>
                      
                      <div id="imagendos">
                          <h4 id="h4moded"> Gráfico de media de toneladas mensuales de recogida de basura por distrito </h4>
                          <img src=".${File.separator}resources${File.separator}img${File.separator}grafico2.png">
                      </div>
                </div>
                <br id="clear"></br>
            </div>
            </body>
            </html>
            """.trimIndent()
    }



    fun resumenDistrito(distrito: String) {
        println(distrito)
    }


}