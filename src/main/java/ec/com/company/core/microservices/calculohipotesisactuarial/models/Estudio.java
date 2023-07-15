package ec.com.company.core.microservices.calculohipotesiscompanyl.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.With;


import java.math.BigDecimal;
import java.util.List;

public record Estudio(
        ActualizacionTasaFinanciera actualizacionTasaFinanciera,
        Empresa empresa,
        EstudioAnterior estudioAnterior,
        @With
        Hipotesiscompanyl hipotesiscompanyl,
        List<Empleado> empleados,
        Normativa tipoNormativa,
        Parametros parametros,
        SaldosIniciales saldosIniciales,
        String numeroProceso
) {
    // Para el proceso de calculo de estimación
    public Estudio withTasas(BigDecimal tasaFinancieraDescuento,
                             BigDecimal tasaPasivaReferencial) {

        Hipotesiscompanyl hipotesiscompanyl = this.hipotesiscompanyl()
                .withTasaFinancieraDescuento(tasaFinancieraDescuento)
                .withTasaPasivaReferencial(tasaPasivaReferencial);
        return this.withHipotesiscompanyl(hipotesiscompanyl);
    }
}
