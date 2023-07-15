package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import java.math.BigDecimal;

public record TasaRotacionAnual(
        int anio,
        BigDecimal tasaRotacion) {
}
