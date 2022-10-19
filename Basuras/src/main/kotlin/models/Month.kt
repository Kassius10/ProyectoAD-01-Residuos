package models

/**
 * Enum de meses del año
 * @param month nombre del mes del año
 */
enum class Month(month: String) {
    ENERO("enero"),
    FEBRERO("febrero"),
    MARZO("marzo"),
    ABRIL("abril"),
    MAYO("mayo"),
    JUNIO("junio"),
    JULIO("julio"),
    AGOSTO("agosto"),
    SEPTIEMBRE("septiembre"),
    OCTUBRE("octubre"),
    NOVIEMBRE("noviembre"),
    DICIEMBRE("diciembre");

    companion object {
        /**
         * Función para filtrar una string y convertirla en mes
         * @param month Nombre del mes a filtrar
         * @return Devuelve un tipo Month si el mes coincide con el enum, si no salta una exception
         */
        fun parserTo(month: String): Month {
            return when (month.uppercase()) {
                "ENERO" -> ENERO
                "FEBRERO" -> FEBRERO
                "MARZO" -> MARZO
                "ABRIL" -> ABRIL
                "MAYO" -> MAYO
                "JUNIO" -> JUNIO
                "JULIO" -> JULIO
                "AGOSTO" -> AGOSTO
                "SEPTIEMBRE" -> SEPTIEMBRE
                "OCTUBRE" -> OCTUBRE
                "NOVIEMBRE" -> NOVIEMBRE
                "DICIEMBRE" -> DICIEMBRE
                else -> throw IllegalArgumentException("No existe el mes: $month")
            }
        }
    }

}