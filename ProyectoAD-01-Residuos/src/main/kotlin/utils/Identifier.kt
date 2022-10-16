package utils

import controller.ContenedorController
import controller.ResiduoController
import controller.Resumen
import exceptions.FicherosException
import java.io.File

object Identifier {
    private val headResiduo = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
    private val headContenedor =
        "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION"

    /**
     * Método para comprobar a que clase pertenece el archivo csv
     * @param file Fichero csv donde se encuentran los datos a procesar.
     */
    fun isCSV(file: File) {
        val head: String = file.readLines()[0]
        when {
            head.equals(headResiduo) -> {
                var list = ResiduoController.loadDataFromCsv(file)
                Resumen.residuos = list
            }
            head.equals(headContenedor) -> {
                var list = ContenedorController.loadDataFromCsv(file)
                Resumen.contenedores = list
            }
            else -> throw FicherosException("El csv es incorrecto: $head")
        }
    }

    /**
     * Método para comprobar a que clase pertenece el archivo json
     * @param file Fichero json donde se encuentran los datos a procesar.
     */
    fun isJSON(file: File) {
        val head: String = getHeadJson(file)
        when {
            head.equals(headResiduo) -> {
                var list = ResiduoController.loadDataFromJson(file)
                Resumen.residuos = list
            }
            head.equals(headContenedor) -> {
                var list = ContenedorController.loadDataFromJson(file)
                Resumen.contenedores = list
            }
            else -> throw FicherosException("El json es incorrecto.")
        }
    }

    /**
     * Método para comprobar a que clase pertenece el archivo xml
     * @param file Fichero xml donde se encuentran los datos a procesar.
     */
    fun isXML(file: File) {
        val head: String = getHeadXml(file)
        when {
            head.equals(headResiduo) -> {
                var list = ResiduoController.loadDataFromXml(file)
                Resumen.residuos = list
            }
            head.equals(headContenedor) -> {
                var list = ContenedorController.loadDataFromXml(file)
                Resumen.contenedores = list
            }
            else -> throw FicherosException("El xml es incorrecto.")
        }
    }

    /**
     * Método para obtener la cabecera del fichero json
     * @param file Fichero a procesar.
     * @return Devuelve una cadena ya procesada del fichero.
     */
    private fun getHeadJson(file: File): String {
        var prueba: MutableList<String> = file.readLines().toMutableList()

        for (i in prueba.indices) {
            prueba[i] = prueba[i].trim()
        }
        prueba.removeIf { (it.length < 3) }

        for (i in prueba.indices) {
            prueba[i] = prueba[i].replaceAfter(":", "")
        }
        prueba = prueba.distinct().toMutableList()

        return prueba.joinToString(";").replace("\"", "").replace(":", "").replace("_", " ")
    }

    /**
     * Método para obtener la cabecera del fichero xml
     * @param file Fichero a procesar.
     * @return Devuelve una cadena ya procesada del fichero.
     */
    private fun getHeadXml(file: File): String {
        var prueba: MutableList<String> = file.readLines().toMutableList()

        for (i in prueba.indices) {
            prueba[i] = prueba[i].trim()
            prueba[i] = prueba[i].replaceAfter(">", "")
        }
        prueba = prueba.distinct().drop(2).toMutableList()
        prueba.removeIf { it.contains("/") }

        return prueba.joinToString(";").replace("<", "").replace(">", "").replace("_", " ")

    }

    /**
     * Método para indicar el tipo de clase que utiliza el fichero csv.
     * @param file Fichero que contiene los datos a procesar.
     * @return Devuelve una cadena indicando el tipo o una exception si no pertenece a ninguna de las dos.
     */
    fun getType(file: File): String {
        val head: String = file.readLines()[0]
        when {
            head.equals(headResiduo) -> return "residuo"
            head.equals(headContenedor) -> return "contenedor"
            else -> return throw FicherosException("El csv es incorrecto: $head")
        }
    }

    /**
     * Método para obtener los ficheros con diferentes extensiones del directorio origen y procesarlos.
     * @param directorioOrigen Directorio origen que contiene los ficheros.
     */
    fun findExtension(directorioOrigen: String) {
        var files = File(directorioOrigen).listFiles()?.toList()
        files?.forEach {
            when {
                it.name.contains(".csv") -> isCSV(it)
                it.name.contains(".xml") -> isXML(it)
                it.name.contains(".json") -> isJSON(it)
            }
        }
    }
}