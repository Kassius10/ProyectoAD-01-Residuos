import controller.Resumen
import java.io.File
import java.io.IOException
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

        if (args[0].equals("parser") && args.size >= 3) {
            try {
                var ficheros = findCSV(args)
                var directorioDestino = getDirectory(args)
                Resumen.parser(ficheros, directorioDestino)
            } catch (e: IOException) {
                println(e.message)
            }

        } else if (args[0].equals("resumen") && args.size >= 3) {

            if (getDirectories(args)) {
                Resumen.createDirectoryImages()

                if (getDistrito(args) != "") {
                    var distrito = getDistrito(args)
                    findExtension(args, distrito)
                } else {
                    findExtension(args, "")
                }
            }
        } else {
            println("Parámetros incorrectos")
            exitProcess(0)
        }

    } else {
        println("Opción incorrecta.")
        exitProcess(0)
    }
}

/**
 * Método para comprobar si los directorios parámetros pertenecen a un directorio existente o no.
 * @param args Parámetros indicados por consola
 * @return Devuelve true si son directorios existentes y false si no existe el directorio.
 */
fun getDirectories(args: Array<String>): Boolean {
    var directories = args.takeLast(2)
    var ok = true
    for (dir in directories) {
        if (!Files.isDirectory(Paths.get(dir))) ok = false
    }
    return ok
}

/**
 * Método para obtener el distrito indicado en los parámetros.
 * @param args Parámetros indicados por consola
 * @return Devuelve una cadena con el distrito
 */
fun getDistrito(args: Array<String>): String {
    return args.drop(1).dropLast(2).joinToString(" ")
}

/**
 * Método para obtener el directorio origen.
 * @param args Parámetros indicados por consola
 * @return Devuelve el directorio si existe.
 */
fun getDirectory(args: Array<String>): File {
    val directorio = args.takeLast(1)
    if (Files.isDirectory(Paths.get(directorio[0]))) {
        return File(directorio[0])
    } else return throw IOException("No existe el directorio destino: ${directorio[0]}")
}

/**
 * Método para obtener los ficheros que contenga el directorio origen.
 * @param args Parámetros indicados por consola
 * @return Devuelve una lista de ficheros si existen.
 */
fun findCSV(args: Array<String>): List<File>? {
    val directorio = args.takeLast(2)
    if (Files.isDirectory(Paths.get(directorio[0]))) {
        return File(directorio[0]).listFiles()?.filter { it.absolutePath.contains(".csv") }
    } else return throw IOException("No existe el directorio: ${directorio[0]}")
}

/**
 * Método para obtener los ficheros con diferentes extensiones del directorio origen y procesarlos.
 * @param args Parámetros indicados por consola
 * @param distrito Distrito que se ha indicado por parámetros si existe.
 */
fun findExtension(args: Array<String>, distrito: String) {
    val directorio = args.takeLast(2)[0]
    var files = File(directorio).listFiles()?.toList()
    files?.forEach {
        when {
            it.name.contains(".csv") -> Identifier.isCSV(it)
            it.name.contains(".xml") -> Identifier.isXML(it)
            it.name.contains(".json") -> Identifier.isJSON(it)
        }
    }
    if (distrito != "") Resumen.resumenDistrito(distrito) else Resumen.resumen()
}