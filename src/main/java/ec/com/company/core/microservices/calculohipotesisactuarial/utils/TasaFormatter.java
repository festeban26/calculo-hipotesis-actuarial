package ec.com.company.core.microservices.calculohipotesiscompanyl.utils;

import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;

import java.math.BigDecimal;

public abstract class TasaFormatter {
    public static BigDecimal format(BigDecimal rawTasa) {
        var tasa = rawTasa.multiply(new BigDecimal("100")) // Convertir a porcentaje
                .stripTrailingZeros();
        if (tasa.scale() > AppConstants.RESPONSE_MAX_BIG_DECIMAL_SCALE) {
            tasa = tasa.setScale(AppConstants.RESPONSE_MAX_BIG_DECIMAL_SCALE, AppConstants.JSONS_NUMERIC_ROUNDING_MODE);
        }
        return tasa;
    }
}
