package utils

import java.io.File
import java.time.LocalDateTime

class Html(
    private val fechaGeneracion: String = LocalDateTime.now().formatToString(),
) {

    fun generateResumenHtml(
        tiempoGeneracion: Long,
        numeroContenedoresTipo: String,
        mediaContenedoresTipo: String,
        mediaToneladasAnuales: String,
        maxMinMediaToneladasAnuales: String,
        sumaRecogidosPorDistrito: String,
        porDistritoCantidadRecogida: String,
    ): String {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href=".${File.separator}css${File.separator}css.css">
                <title>Resumen Distritos Madrid</title>
            </head>
            <body>
             <img id="logo" src=".${File.separator}img${File.separator}logo.jpg">
             
             <h1 id ="titulo">Resumen de Recogidas y Reciclaje de contenedores en los distritos de Madrid</h1>
            
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
                          <img src=".${File.separator}img${File.separator}grafico1.png">
                      </div>
                      
                      <div id="imagendos">
                          <h4 id="h4moded"> Gráfico de media de toneladas mensuales de recogida de basura por distrito </h4>
                          <img src=".${File.separator}img${File.separator}grafico2.png">
                      </div>
                </div>
                <br id="clear"></br>
            </div>
            <p>Tiempo de generación del archivo: ${System.currentTimeMillis() - tiempoGeneracion}ms</p>
            </body>
            </html>
            """.trimIndent()
    }

    /**
     * Método que genera el html de resumen de distrito
     * @return Devuelve una cadena con el contenido del html.
     */
    fun generateResumenDistritoHtml(
        tiempoGeneracion: Long,
        distrito: String,
        tipoContenedoresDistrito: String,
        totalToneladasPorResiduoDistrito: String,
        estadisticaPorMesResiduoDistrito: String,
    ): String {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
               <meta charset="UTF-8">
               <meta http-equiv="X-UA-Compatible" content="IE=edge">
               <meta name="viewport" content="width=device-width, initial-scale=1.0">
               <link rel="stylesheet" href=".${File.separator}css${File.separator}css.css">
               <title>Resumen $distrito</title>
            </head>
            <body>
               <img id="logo" src=".${File.separator}img${File.separator}logo.jpg">
               
               <h1 id ="titulo">Resumen de Recogidas y Reciclaje de contenedores del distrito: $distrito</h1>

               <h2> Generacion del html: $fechaGeneracion</h2>
               <br>
               <h3>Construido por: Daniel Carmona y Jeremy Ramos</h3>
               
               <div id="contenedor">
               
                  <h4>Numero de Contenedores De Cada Tipo Que Hay En $distrito</h4>
                  
                  <div id="minicontainer">
                     <p>$tipoContenedoresDistrito</p>
                  </div>
                  <br>
               
                  <h4>Total De Toneladas Recogidas Por Residuos En $distrito</h4>
                  <div id="minicontainer">
                     <p>$totalToneladasPorResiduoDistrito</p>
                  </div>
                  <br>
               
                  <h4>Gráfica De Total De Toneladas Por Residuo En $distrito</h4>
                  <div id="minicontainer">
                     <img src="./img/totalToneladasPorResiduos$distrito.png">
                  </div>
               
               
                  <h4>Máximo, Mínimo, Media y Desviación De Toneladas Por Mes En $distrito</h4>
                  <div id="minicontainer">
                     <p>$estadisticaPorMesResiduoDistrito</p> 
                  </div>  
                  <br>
               
                  <h4>Gráfica De Máxima, Mínimo, Media y Desviación De Toneladas Por Mes En $distrito </h4>
                  <div id="containerimg">     
                     <img src="./img/estadisticasResiduosPorMes$distrito.png">
                  </div>
                  <br>
               </div>
               <p>Tiempo de generación del archivo: ${System.currentTimeMillis() - tiempoGeneracion}ms</p>
            </body>
            </html>
        """.trimIndent()
    }

    /**
     * Método que genera el css necesario de los html.
     * @return Devuelve una cadena con el contenido del css.
     */
    fun generateCss(): String {
        return """
            #logo{
               height: 700px;
               width: 80%;
               box-shadow: #303030;
               margin:auto;
               margin-left: 10%;

            }
            #titulo {
               font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
               font-style: oblique;
               text-decoration: underline;
            }
            #contenedor{
               margin: auto;
               width: 100%;
               background: #0CABA8;
               -moz-box-shadow: 3px 3px 5px 6px #008F8C;
               -webkit-box-shadow: 3px 3px 5px 6px #008F8C;
               box-shadow: 3px 3px 5px 6px #008F8C;
            }

            #minicontainer{
               margin-left: 1.5em;
               color: #0CABA8;
            }
            table{
               margin: 0 auto;
               width:40%;
            }
            h1{
               font-size: 3.08em;
               color: #023535;
            }

            h4{
               color: #ffffff;
               background: #015958;
               height: 2.2em;
               padding-top: 0.9em;

               -moz-box-shadow: 3px 3px 5px 6px #015958;
               -webkit-box-shadow: 3px 3px 5px 6px #015958;
               box-shadow: 3px 3px 5px 6px #015958;
               font-style: italic;
               padding-left: 1.3em;
               font-size:1.7em;
            }


            img {
               width: 70%;
               height: 30%;
               backgroundColor:red;
               -moz-box-shadow: 3px 3px 5px 6px #008F8C;
               -webkit-box-shadow: 3px 3px 5px 6px #008F8C;
               box-shadow: 3px 3px 5px 6px #008F8C;
               margin-left: 15%;

            }

            #containerimg {
               border: 5px solid #0CABA8;

            }

            #imagenuno {
               width: 49.95%;
               float: right;
            }

            #imagendos {
               width: 49.95%;

               border-right:5px solid #023535;
               border-bottom-right-radius: 5px;
               border-bottom-left-radius:  5px ;
            }


            #h4moded{
               color: #ffffff;
               background: #015958;
               height: 2.2em;
               padding-top: 0.9em;
               -moz-box-shadow: 3px 3px 5px 6px #015958;
               -webkit-box-shadow: 3px 3px 5px 6px #015958;
               box-shadow: 3px 3px 3px 3px #015958;
               font-style: italic;
               text-align: left;
               padding-left: 1.3em;
               font-size:1.7em;
            }

            #clear{
               float: none;
            }
        """.trimIndent()
    }
}
