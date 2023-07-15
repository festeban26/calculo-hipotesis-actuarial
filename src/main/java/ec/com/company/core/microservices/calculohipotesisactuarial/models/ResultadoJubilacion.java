package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import jakarta.validation.constraints.NotNull;
import lombok.With;

public record ResultadoJubilacion(
        @NotNull
        @With
        ResultadoAplicacionJubilacion resultadoAplicacion
) {
}
