package utils

import java.io.File

class Html(
    private val fechaGeneracion: String,
    private val distrito: String,
    private val tipoContenedoresDistrito: String,
    private val totalToneladasPorResiduoDistrito: String,
    private val estadisticaPorMesResiduoDistrito: String,
) {
    fun generateResumenDistritoHtml(): String {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
               <meta charset="UTF-8">
               <meta http-equiv="X-UA-Compatible" content="IE=edge">
               <meta name="viewport" content="width=device-width, initial-scale=1.0">
               <link rel="stylesheet" href="./css/css.css">
               <title>Resumen $distrito</title>
            </head>
            <body>
               <img id="logo" src=".${File.separator}resources${File.separator}img${File.separator}logo.jpg">
               
               <h1 id ="titulo">Resumen de Recogidas y Reciclaje de contenendores del distrito: $distrito</h1>

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
                     <img src="./img/totalToneladasPorResiduosCENTRO.png">
                  </div>
               
               
                  <h4>Máximo, Mínimo, Media y Desviación De Toneladas Por Mes En $distrito</h4>
                  <div id="minicontainer">
                     <p>$estadisticaPorMesResiduoDistrito</p> 
                  </div>  
                  <br>
               
                  <h4>Gráfica De Máxima, Mínimo, Media y Desviación De Toneladas Por Mes En $distrito </h4>
                  <div id="containerimg">     
                     <img src="./img/estadisticasResiduosPorMesCENTRO.png">
                  </div>
                  <br>
               </div>
                       
               
            </body>
            </html>
        """.trimIndent()
    }

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
