package mappers

import dto.ResiduoDTO
import models.Month
import models.Residuo
import models.TipoResiduo

/**
 * Método de extension de la clase ResiduoDTO para intercambiar un modelo ResiduoDTO a Residuo
 * @return Devuelve el objeto Residuo con los datos intercambiados.
 */
fun ResiduoDTO.toResiduo(): Residuo {
    return Residuo(
        year = this.year,
        month = Month.parserTo(this.month),
        lote = this.lote,
        tipoResiduo = TipoResiduo.parserTo(this.tipoResiduo),
        numDistrito = this.numDistrito,
        distrito = this.distrito,
        toneladas = this.toneladas
    )
}

/**
 * Método de extension de la clase Residuo para intercambiar un modelo Residuo a ResiduoDTO
 * @return Devuelve el objeto ResiduoDTO con los datos intercambiados.
 */
fun Residuo.toResiduoDTO(): ResiduoDTO {
    return ResiduoDTO(
        year = this.year,
        month = this.month.name.uppercase(),
        lote = this.lote,
        tipoResiduo = this.tipoResiduo.tipo.uppercase(),
        numDistrito = this.numDistrito,
        distrito = this.distrito,
        toneladas = this.toneladas
    )
}

