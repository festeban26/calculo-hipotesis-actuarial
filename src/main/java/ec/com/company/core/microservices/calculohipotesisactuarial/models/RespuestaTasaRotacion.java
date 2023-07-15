package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import java.util.List;

public record RespuestaTasaRotacion(
        List<TasaRotacionAnual> tasasRotacion,
        Hipotesiscompanyl hipotesiscompanyl) {
}
