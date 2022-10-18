package controller

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.Charset

internal class ContenedorControllerTest {
    @Test
    fun loadDataFromCsvTest() {
        val file = System.getProperty("user.dir") + File.separator + "prueba.csv"
        File(file).bufferedWriter(Charset.forName("UTF-8")).use { out ->
            out.write("Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION")
            out.newLine()
            out.write("181193;RESTO;CL_18;Res 3200 CL;1;3;VILLA DE VALLECAS;;CALLE;DE LA SIERRA ESPAÑA;4;447109.71;4469732.48;-3.62305;40.3764981;CALLE DE LA SIERRA ESPAÑA, 4")
        }

        var contenedor = ContenedorController.loadDataFromCsv(File(file))[0]

        assertAll(
            { assertEquals("RESTO", contenedor.tipoContenedor) },
            { assertEquals("VILLA DE VALLECAS", contenedor.distrito) },
            { assertEquals(181193, contenedor.codigoInterno) },
            { assertEquals("CL_18", contenedor.modelo) }
        )

        File(file).deleteOnExit()
    }
}