package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

internal class IdentifierTest {

    @Test
    fun getTypeTest() {
        val file = System.getProperty("user.dir") + File.separator + "prueba.csv"
        File(file).writeText("Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas")

        assertEquals("residuo", Identifier.getType(File(file)))

        File(file).writeText("Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION")

        assertEquals("contenedor", Identifier.getType(File(file)))

        File(file).deleteOnExit()
    }

    @Test
    fun getTypeTestExceptions() {
        val file = System.getProperty("user.dir") + File.separator + "prueba.csv"
        File(file).writeText("Año;Mes;Nombre Distrito;Toneladas")

        val thrown: Exception = assertThrows { Identifier.getType(File(file)) }
        assertTrue(thrown.message!!.contains("El csv es incorrecto"))
        File(file).deleteOnExit()
    }
}