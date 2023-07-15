package ec.com.company.core.microservices.calculohipotesiscompanyl.contants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public class AppConstants {
    // 10 was chosen because it is the default scale for BigDecimal
    public static final int DEFAULT_BIG_DECIMAL_SCALE = 10;
    public static final int RESPONSE_MAX_BIG_DECIMAL_SCALE = 4;

    public static final RoundingMode JSONS_NUMERIC_ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final RoundingMode BIG_DECIMAL_ROUNDING_MODE= RoundingMode.HALF_UP;
    public static final RoundingMode VARIABLE_REFERENCIA_ROUNDING_MODE = RoundingMode.HALF_UP;

    public static final MathContext DEFAULT_BIG_DECIMAL_MATH_CONTEXT = new MathContext(DEFAULT_BIG_DECIMAL_SCALE, BIG_DECIMAL_ROUNDING_MODE);
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static final int JSONS_MAX_NUMERIC_SCALE = 4;


    public static final BigDecimal HORIZONTE_PAGO_BENEFICIO_JUBILACION = BigDecimal.valueOf(25.0);
    public static final BigDecimal VARIABLE_REFERENCIA_INCREMENTO_DURACION_DEFAULT_VALUE = BigDecimal.valueOf(0.5);

}
