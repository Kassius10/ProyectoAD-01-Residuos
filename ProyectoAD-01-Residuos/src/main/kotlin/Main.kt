import controller.Resumen
import models.Bitacora
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

/**
 * Función main principal
 * @author Daniel Carmona y Jeremy Ramos
 * @version V0.1
 * @param args Argumentos necesarios que se pasaran por consola.
 */
fun main(args: Array<String>) {
    init(args)
}

/**
 * Función para comprobar los parámetros indicados y permitir la
 * funcionalidad del programa.
 * @param args Argumentos necesarios que se reciben del main.
 */
fun init(args: Array<String>) {
    var isSucces = true

    if (args.isNotEmpty()) {

        try {
            Resumen.getDirectories(args)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            exitProcess(0)
        }

        if (args[0].equals("parser") && args.size >= 3) {

            var tiempo = measureTimeMillis {

                try {
                    Resumen.parser()

                } catch (e: Exception) {
                    println("Error parser: ${e.message}")
                    isSucces = false
                }
            }
            var bitacora = Bitacora("parser", isSucces, tiempo)
            bitacora.bitacoraXml(args.last())

        } else if (args[0].equals("resumen") && args.size >= 3) {

            var tiempo = System.currentTimeMillis()
            Resumen.createDirectoryImagesAndCSS()

            if (getDistrito(args) != "") {
                try {
                    var distrito = getDistrito(args)
                    Resumen.resumenDistrito(distrito)
                } catch (e: Exception) {
                    println(e.message)
                    isSucces = false
                }
                var bitacora = Bitacora("resumen distrito", isSucces, System.currentTimeMillis() - tiempo)
                bitacora.bitacoraXml(args.last())

            } else {
                try {
                    Resumen.resumen()
                } catch (e: Exception) {
                    println(e.message)
                    isSucces = false
                }
                var bitacora = Bitacora("resumen", isSucces, System.currentTimeMillis() - tiempo)
                bitacora.bitacoraXml(args.last())
            }

        } else {
            println("Error: Opción de parámetro incorrecta")
            exitProcess(0)
        }

    } else {
        println("Error: Los argumentos son erróneos.")
        exitProcess(0)
    }
}

/**
 * Método para obtener el distrito indicado en los parámetros.
 * @param args Parámetros indicados por consola
 * @return Devuelve una cadena con el distrito
 */
fun getDistrito(args: Array<String>): String {
    return args.drop(1).dropLast(2).joinToString(" ")
}

