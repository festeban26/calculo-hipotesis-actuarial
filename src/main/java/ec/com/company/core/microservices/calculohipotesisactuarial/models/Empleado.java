package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Empleado(Integer identificacion,
                       BigDecimal tsJubilacion,
                       @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
                       LocalDate fechaIngresoJubilacion,
                       @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
                       LocalDate fechaNacimiento,
                       @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
                       LocalDate fechaIngresoDesahucio,
                       Integer tipo,
                       String sexo,
                       String centroDeCosto,
                       BigDecimal costoLaboralJubilacion,
                       BigDecimal interesNetoJubilacion,
                       BigDecimal remuneracionPromedioJubilacion,
                       BigDecimal reservaJubilacion

) {
}
