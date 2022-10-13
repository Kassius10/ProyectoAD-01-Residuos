import java.io.File

class Html(
    private val fechaGeneracion: String,
    private val distrito: String,
    private val tipoContenedoresDistrito: String,
    private val totalToneladasPorResiduoDistrito: String,
    private val estadisticaPorMesResiduoDistrito: String
) {
    fun generateResumenDistritoHtml(): String {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
               <meta charset="UTF-8">
               <meta http-equiv="X-UA-Compatible" content="IE=edge">
               <meta name="viewport" content="width=device-width, initial-scale=1.0">
               <link rel="stylesheet" href=".${File.separator}resources${File.separator}css${File.separator}css.css">
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
                     <img src=".${File.separator}resources${File.separator}img${File.separator}estadisticasResiduosPorMesCENTRO.png">
                  </div>
               
               
                  <h4>Máximo, Mínimo, Media y Desviación De Toneladas Por Mes En $distrito</h4>
                  <div id="minicontainer">
                     <p>$estadisticaPorMesResiduoDistrito</p> 
                  </div>  
                  <br>
               
                  <h4>Gráfica De Máxima, Mínimo, Media y Desviación De Toneladas Por Mes En $distrito </h4>
                  <div id="containerimg">     
                     <img src=".${File.separator}resources${File.separator}img${File.separator}totalToneladasPorResiduosCENTRO.png">
                  </div>
                  <br>
               </div>
                       
               
            </body>
            </html>
        """.trimIndent()
    }
}
