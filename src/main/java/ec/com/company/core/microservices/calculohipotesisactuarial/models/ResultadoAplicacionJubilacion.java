package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ResultadoAplicacionJubilacion(
        @NotNull
        BigDecimal variacionObdTasaDescuentoMas,
        @NotNull
        BigDecimal variacionObdTasaDescuentoMenos,
        @NotNull
        BigDecimal obdActual
) {
}
