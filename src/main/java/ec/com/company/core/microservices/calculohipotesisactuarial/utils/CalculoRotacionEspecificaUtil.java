package ec.com.company.core.microservices.calculohipotesiscompanyl.utils;

import ec.com.company.core.microservices.calculohipotesiscompanyl.calculators.CalculadoraTasaRotacion;
import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;
import ec.com.company.core.microservices.calculohipotesiscompanyl.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class CalculoRotacionEspecificaUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculoRotacionEspecificaUtil.class);

    public static RespuestaTasaRotacion calcular(InformacionRotacion informacionRotacion) throws IllegalArgumentException {
        LOGGER.debug("Iniciando cálculo de rotación específica...");
        // Para cada año
        List<TasaRotacionAnual> tasasRotacionAnuales = new ArrayList<>();
        // Promedio de tasas de rotación de cada año
        BigDecimal sumTasaRotacionPromedio = BigDecimal.ZERO;

        for (ItemInformacionRotacion periodoRotacion : informacionRotacion.dataPeriodosRotacion()) {
            // Calcular la tasa de rotación del año de cada periodoRotacion
            BigDecimal tasaRotacionItem = CalculadoraTasaRotacion.calcularTasaRotacion(
                    periodoRotacion.empleadosAlInicioDelAnio(),
                    periodoRotacion.salidas());
            // Considerar la tasa de rotación del año en el promedio
            sumTasaRotacionPromedio = sumTasaRotacionPromedio.add(tasaRotacionItem);

            // Crear objeto de respuesta
            TasaRotacionAnual itemTasaRotacionAnual = new TasaRotacionAnual(periodoRotacion.anio(), tasaRotacionItem);
            tasasRotacionAnuales.add(itemTasaRotacionAnual);
        }

        if (informacionRotacion.dataPeriodosRotacion().isEmpty()) {
            LOGGER.warn("No se encontraron elementos para el cálculo de rotación.");
            return null;
        }

        int numItems = informacionRotacion.dataPeriodosRotacion().size();
        BigDecimal divisor = BigDecimal.valueOf(numItems);
        var context = AppConstants.DEFAULT_BIG_DECIMAL_MATH_CONTEXT;

        var rawTasa = sumTasaRotacionPromedio.divide(divisor, context);
        BigDecimal tasaRotacionPromedio = TasaFormatter.format(rawTasa);

        LOGGER.debug("Cálculo de rotación específica finalizado correctamente.");

        Hipotesiscompanyl hipotesiscompanyl = Hipotesiscompanyl.builder()
                .tasaRotacionPromedio(tasaRotacionPromedio)
                .build();
        return new RespuestaTasaRotacion(tasasRotacionAnuales, hipotesiscompanyl);
    }
}
