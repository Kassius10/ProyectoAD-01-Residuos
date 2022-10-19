package controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import java.nio.charset.Charset

internal class ResiduoControllerTest {

    @Test
    fun loadDataFromCsvTest() {
        val file = System.getProperty("user.dir") + File.separator + "prueba.csv"
        File(file).bufferedWriter(Charset.forName("UTF-8")).use { out ->
            out.write("Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas")
            out.newLine()
            out.write("2021;enero;1;RESTO;1;Centro;3477,92")
        }

        var residuo = ResiduoController.loadDataFromCsv(File(file))[0]

        assertAll(
            { assertEquals(2021, residuo.year) },
            { assertEquals("enero", residuo.month) },
            { assertEquals("RESTO", residuo.tipoResiduo) },
            { assertEquals("Centro", residuo.distrito) }
        )

        File(file).deleteOnExit()
    }

}