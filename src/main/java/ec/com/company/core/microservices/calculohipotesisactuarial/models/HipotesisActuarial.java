package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;

// Annotation configure to remove null objects in json response
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@Builder
public class Hipotesiscompanyl {
    BigDecimal tasaRotacionPromedio;
    @With
    BigDecimal tasaFinancieraDescuento;
    @With
    BigDecimal tasaPasivaReferencial;
    BigDecimal tasaPasivaReferencialProyectada;
    BigDecimal tasaIncrementoSalarios;
    BigDecimal tasaPasivaReferencialBce;
    BigDecimal decimoCuartoSueldo;
    BigDecimal factorRemuneracion;
    BigDecimal porcentajeIncrementoSalarialEstimado;
    BigDecimal porcentajeIncrementoSalarialEstimadoProporcional;
    BigDecimal porcentajeVariacionSensibilidad;
    BigDecimal porcentajeVariacionSensibilidadRotacion;
}