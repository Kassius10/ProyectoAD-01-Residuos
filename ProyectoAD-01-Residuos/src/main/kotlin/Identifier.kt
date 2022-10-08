import controller.ResiduoController
import dto.ResiduoDTO
import mu.KotlinLogging
import java.io.File

object Identifier {
    private val logger = KotlinLogging.logger {}
    private val headResiduoCSV = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
    private val headContenedorCSV =
        "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION"


    fun isCSV(file: File): List<ResiduoDTO> {
        val residuos: String = file.readLines()[0].substring(1)
        when {
            residuos.equals(headResiduoCSV) -> return ResiduoController.loadDataFromCsv(file)
            residuos.equals(headContenedorCSV) -> return ResiduoController.loadDataFromCsv(file)
            else -> throw IllegalArgumentException("El csv es incorrecto.")
        }
    }

    fun isJSON(file: File): List<ResiduoDTO> {
        val residuos: String = getHeadJson(file)
        when {
            residuos.equals(headResiduoCSV) -> return ResiduoController.readJson(file)
            residuos.equals(headContenedorCSV) -> return ResiduoController.readJson(file)
            else -> throw IllegalArgumentException("El Json es incorrecto.")
        }
    }

    fun isXML(file: File) {
        val residuos: String = file.readLines()[0].substring(1)
        when {
            residuos.equals(headResiduoCSV) -> ResiduoController.readXml(file)
            residuos.equals(headContenedorCSV) -> ResiduoController.readXml(file)
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

        return prueba.joinToString(";").replace("\"", "").replace(":", "")
    }
}