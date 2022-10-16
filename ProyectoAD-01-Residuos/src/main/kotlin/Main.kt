import controller.Resumen
import exceptions.FicherosException
import models.Bitacora
import mu.KotlinLogging
import java.io.FileNotFoundException
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger {}

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
    logger.debug { "Comprobando parámetros de entrada..." }
    if (args.isNotEmpty()) {

        try {
            logger.debug { "Comprobando si los directorios existen..." }
            Resumen.getDirectories(args)
        } catch (e: FileNotFoundException) {
            logger.error { "Error: ${e.message}" }
            exitProcess(0)
        }

        if (args[0].equals("parser") && args.size >= 3) {
            logger.debug { "Opción seleccionada: parser" }

            var tiempo = measureTimeMillis {
                try {
                    Resumen.parser()
                } catch (e: FicherosException) {
                    logger.error { "Error: ${e.message}" }
                    isSucces = false
                }
            }
            logger.debug { "Generando bitácora..." }
            var bitacora = Bitacora("parser", isSucces, tiempo)
            bitacora.bitacoraXml(args.last())
            logger.debug { "Bitácora almacenada perfectamente" }

        } else if (args[0].equals("resumen") && args.size >= 3) {

            var tiempo = System.currentTimeMillis()
            logger.debug { "Creando directorios de imágenes y css en el directorio destino..." }
            Resumen.createDirectoryImagesAndCSS()

            if (getDistrito(args) != "") {
                logger.debug { "Opción seleccionada: resumen distrito" }
                try {
                    logger.debug { "Obteniendo el distrito indicado..." }
                    var distrito = getDistrito(args)
                    Resumen.resumenDistrito(distrito)
                } catch (e: Exception) {
                    logger.error { "Error: ${e.message}" }
                    isSucces = false
                }
                logger.debug { "Generando bitácora..." }
                var bitacora = Bitacora("resumen distrito", isSucces, System.currentTimeMillis() - tiempo)
                bitacora.bitacoraXml(args.last())
                logger.debug { "Bitácora almacenada perfectamente" }

            } else {
                logger.debug { "Opción seleccionada: resumen" }
                try {
                    Resumen.resumen()
                } catch (e: FileNotFoundException) {
                    logger.error { "Error: ${e.message}" }
                    isSucces = false
                }
                logger.debug { "Generando bitácora..." }
                var bitacora = Bitacora("resumen", isSucces, System.currentTimeMillis() - tiempo)
                bitacora.bitacoraXml(args.last())
                logger.debug { "Bitácora almacenada perfectamente" }
            }

        } else {
            logger.error { "Error: Opción de parámetro incorrecta" }
            exitProcess(0)
        }

    } else {
        logger.error { "Error: Los argumentos son erróneos." }
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

