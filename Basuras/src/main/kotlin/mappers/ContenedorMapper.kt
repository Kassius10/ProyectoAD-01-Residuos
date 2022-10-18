package mappers

import dto.ContenedorDTO
import models.Contenedor
import models.TipoContenedor

/**
 * Método de extension de la clase ContenedorDTO para intercambiar un modelo ContenedorDTO a Contenedor
 * @return Devuelve el objeto Contenedor con los datos intercambiados.
 */
fun ContenedorDTO.toResiduo(): Contenedor {
    return Contenedor(
        codigoInterno = this.codigoInterno,
        tipoContenedor = TipoContenedor.parserTo(this.tipoContenedor),
        modelo = this.modelo,
        description = this.description,
        cantidad = this.cantidad,
        lote = this.lote,
        distrito = this.distrito,
        barrio = this.barrio,
        tipoVia = this.tipoVia,
        nombre = this.nombre,
        numero = this.numero,
        coordenadaX = this.coordenadaX,
        coordenadaY = this.coordenadaY,
        longitud = this.longitud,
        latitud = this.latitud,
        direction = this.direction
    )
}

/**
 * Método de extension de la clase Contenedor para intercambiar un modelo Contenedor a ContenedorDTO
 * @return Devuelve el objeto ContenedorDTO con los datos intercambiados.
 */
fun Contenedor.toResiduoDTO(): ContenedorDTO {
    return ContenedorDTO(
        codigoInterno = this.codigoInterno,
        tipoContenedor = this.tipoContenedor.name.uppercase(),
        modelo = this.modelo,
        description = this.description,
        cantidad = this.cantidad,
        lote = this.lote,
        distrito = this.distrito,
        barrio = this.barrio,
        tipoVia = this.tipoVia,
        nombre = this.nombre,
        numero = this.numero,
        coordenadaX = this.coordenadaX,
        coordenadaY = this.coordenadaY,
        longitud = this.longitud,
        latitud = this.latitud,
        direction = this.direction
    )
}