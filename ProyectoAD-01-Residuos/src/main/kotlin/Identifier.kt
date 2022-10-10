import controller.ResiduoController
import dto.ResiduoDTO
import mu.KotlinLogging
import java.io.File

object Identifier {
    private val logger = KotlinLogging.logger {}
    private val headResiduo = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
    private val headContenedor =
        "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION"


    fun isCSV(file: File): List<ResiduoDTO> {
        val residuos: String = file.readLines()[0].substring(1)
        when {
            residuos.equals(headResiduo) -> return ResiduoController.loadDataFromCsv(file)
            residuos.equals(headContenedor) -> return ResiduoController.loadDataFromCsv(file)
            else -> throw IllegalArgumentException("El csv es incorrecto.")
        }
    }

    fun isJSON(file: File): List<ResiduoDTO> {
        val residuos: String = getHeadJson(file)
        when {
            residuos.equals(headResiduo) -> return ResiduoController.readJson(file)
            residuos.equals(headContenedor) -> return ResiduoController.readJson(file)
            else -> throw IllegalArgumentException("El Json es incorrecto.")
        }
    }

    fun isXML(file: File): List<ResiduoDTO> {
        val residuos: String = getHeadXml(file)
        when {
            residuos.equals(headResiduo) -> return ResiduoController.readXml(file)
            residuos.equals(headContenedor) -> return ResiduoController.readXml(file)
            else -> throw IllegalArgumentException("El XML es incorrecto.")
        }
    }

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

    private fun getHeadXml(file: File): String {
        var prueba: MutableList<String> = file.readLines().drop(2).toMutableList()

        for (i in prueba.indices) {
            prueba[i] = prueba[i].trim()
            prueba[i] = prueba[i].replaceAfter(">", "")
        }
        prueba = prueba.distinct().toMutableList()
        prueba.removeIf { it.contains("/") }

        return prueba.joinToString(";").replace("<", "").replace(">", "").replace("_", " ")

    }
}