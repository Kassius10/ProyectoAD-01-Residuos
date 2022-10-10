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
    VIDRIO("vidrio")
}
