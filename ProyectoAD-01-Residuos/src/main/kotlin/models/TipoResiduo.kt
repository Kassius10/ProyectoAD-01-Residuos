package models

/**
 * Enum de tipos de residuos
 * @param tipo Tipo de residuo
 */
enum class TipoResiduo(val tipo: String) {
    ANIMALES_MUERTOS("animales Muertos"),
    CAMA_DE_CABALLO("cama de caballo"),
    CARTON_COMERCIAL("carton comercial"),
    CLINICOS("clinicos"),
    CONTENEDORES_DE_ROPA("contenedores de ropa"),
    ENVASES("envases"),
    ORGANICA("organica"),
    PAPEL_CARTON("papel-carton"),
    PILAS("pilas"),
    PUNTOS_LIMPIOS("puntos limpios"),
    RCD("rcd"),
    RESTO("resto"),
    VIDRIO("vidrio"),
    VIDRIO_COMERCIAL("vidrio comercial");

    companion object {
        /**
         * Función para filtrar una string y convertirla en un tipo de residuo
         * @param tipo Nombre del tipo de residuo a filtrar
         * @return Devuelve un tipo Residuo si el residuo coincide con el enum, si no salta una exception
         */
        fun parserTo(tipo: String): TipoResiduo {

            return when (tipo.uppercase().replace(" ", "_").replace("-", "_")) {
                "ANIMALES_MUERTOS" -> ANIMALES_MUERTOS
                "CAMA_DE_CABALLO" -> CAMA_DE_CABALLO
                "CARTON_COMERCIAL" -> CARTON_COMERCIAL
                "CLINICOS" -> CLINICOS
                "CONTENEDORES_DE_ROPA" -> CONTENEDORES_DE_ROPA
                "ENVASES" -> ENVASES
                "ORGANICA" -> ORGANICA
                "PAPEL_CARTON" -> PAPEL_CARTON
                "PILAS" -> PILAS
                "PUNTOS_LIMPIOS" -> PUNTOS_LIMPIOS
                "RCD" -> RCD
                "RESTO" -> RESTO
                "VIDRIO" -> VIDRIO
                "VIDRIO_COMERCIAL" -> VIDRIO_COMERCIAL
                else -> throw IllegalArgumentException("No existe el tipo de residuo: $tipo")
            }
        }
    }
}
