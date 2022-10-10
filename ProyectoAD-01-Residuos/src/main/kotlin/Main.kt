import controller.Resumen
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

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
    if (args.isNotEmpty()) {

        if (args[0].equals("parser")) {
            try {
                var ficheros = findCSV(args)
                Resumen.parser(ficheros)
            } catch (e: Exception) {
                println("El directorio no contiene archivos csv.")
            }

        } else if (args[0].equals("resumen")) {
            println("Esto es el resume")
        } else {
            println("Parámetros incorrectos")
            exitProcess(0)
        }

    } else {
        println("Opción incorrecta.")
//        exitProcess(0)
//        Resumen.parser()
    }
}

fun findCSV(args: Array<String>): List<File>? {
    val directorio = args.takeLast(2)[0]
    if (Files.isDirectory(Paths.get(directorio))) {
        return File(directorio).listFiles()?.filter { it.absolutePath.contains(".csv") }
    } else return throw IllegalStateException("No existe el directorio: $directorio")
}