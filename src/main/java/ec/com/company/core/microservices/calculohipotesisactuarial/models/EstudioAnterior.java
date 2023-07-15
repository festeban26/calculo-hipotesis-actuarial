package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import jakarta.validation.constraints.NotNull;
import lombok.With;

public record EstudioAnterior(
        @NotNull
        @With
        ResultadoJubilacion resultadoJubilacion,
        @NotNull
        @With
        Hipotesiscompanyl hipotesiscompanyl
) {
}
