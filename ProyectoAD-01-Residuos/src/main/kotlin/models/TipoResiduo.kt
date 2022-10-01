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
    VIDRIO_COMERCIAL("vidrio comercial")
}
