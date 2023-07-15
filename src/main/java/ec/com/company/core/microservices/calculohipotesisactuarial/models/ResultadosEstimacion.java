package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import java.math.BigDecimal;

public record ResultadosEstimacion(
        BigDecimal obd1JubConRotacion_suma,
        BigDecimal obd2JubConRotacion_suma,
        BigDecimal obd_suma
) {
}