package controller

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ResumenTest {
    @Test
    fun getDirectoriesTest() {
        var args: Array<String> = arrayOf(System.getProperty("user.dir"), System.getProperty("user.home"))
        assertTrue(Resumen.getDirectories(args))

    }

    @Test
    fun getDirectoriesTestExceptions() {
        var thrown: Exception
        var args: Array<String> = arrayOf("Hola", System.getProperty("user.home"))
        var args1: Array<String> = arrayOf(System.getProperty("user.home"), "Hola")
        var args2: Array<String> = arrayOf("hola", "hola")
        var args3: Array<String> = arrayOf("hola")


        thrown = assertThrows { Resumen.getDirectories(args) }
        assertTrue(thrown.message!!.contains("No existe el directorio"))

        thrown = assertThrows { Resumen.getDirectories(args1) }
        assertTrue(thrown.message!!.contains("No existe el directorio"))

        thrown = assertThrows { Resumen.getDirectories(args2) }
        assertTrue(thrown.message!!.contains("No existe el directorio"))

        thrown = assertThrows { Resumen.getDirectories(args3) }
        assertTrue(thrown.message!!.contains("No existe el directorio"))
    }


}