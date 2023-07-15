package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;


import java.time.LocalDate;

public record Parametros(
        @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
        LocalDate fechaValoracion,
        @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
        LocalDate fechaEstimacion,
        @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
        LocalDate fechaProyeccion,
        String politicaPagoJubilacion,
        String reconocimientoSalidas,
        String tipoCalculo,
        String tipoTraspaso,
        String versionTablaMortalidadJubilacion,
        String versionCalculoFactorProbabilidadJubilacion,
        Boolean usaTasaRotacionPorCentroDeCosto,
        Double ajusteQxRotacionJubilacion
) {
}
