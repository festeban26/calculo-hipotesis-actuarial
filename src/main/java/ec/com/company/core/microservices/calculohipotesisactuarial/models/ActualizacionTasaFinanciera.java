package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;


import java.time.LocalDate;

public record ActualizacionTasaFinanciera(
        @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
        LocalDate fecha
) {
}
