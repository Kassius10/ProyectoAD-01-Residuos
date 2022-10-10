package models

/**
 * Enum de tipo de contenedor
 * @param tipo Tipo de contenedor
 */
enum class TipoContenedor(tipo: String) {
    ENVASES("envases"),
    ORGANICA("organica"),
    PAPEL_CARTON("papel-carton"),
    RESTO("resto"),
    VIDRIO("vidrio");

    companion object {
        /**
         * Función para filtrar una string y convertirla en un tipo de contenedor
         * @param tipo Nombre del tipo de contenedor a filtrar
         * @return Devuelve un tipo Contenedor si el contenedor coincide con el enum, si no salta una exception
         */
        fun parserTo(tipo: String): TipoContenedor {
            return when (tipo.uppercase().replace("-", "_")) {
                "ENVASES" -> ENVASES
                "ORGANICA" -> ORGANICA
                "PAPEL_CARTON" -> PAPEL_CARTON
                "RESTO" -> RESTO
                "VIDRIO" -> VIDRIO
                else -> throw IllegalArgumentException("No existe el tipo de contenedor: $tipo")
            }
        }
    }
}
