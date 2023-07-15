package ec.com.company.core.microservices.calculohipotesiscompanyl.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum PoliticasPagoJubilacionEnum {
    FONDO_GLOBAL("F"), // "Fondo Global"
    PENSION_MENSUAL("P"); // "Pensión Mensual"

    private static final Logger LOGGER = LoggerFactory.getLogger(PoliticasPagoJubilacionEnum.class);
    private static final Map<String, PoliticasPagoJubilacionEnum> codigoToEnumMap = new HashMap<>();

    static {
        for (PoliticasPagoJubilacionEnum politicaPagoJubilacionEnum : values()) {
            codigoToEnumMap.put(politicaPagoJubilacionEnum.getCodigo(), politicaPagoJubilacionEnum);
        }
    }

    private final String codigo;

    PoliticasPagoJubilacionEnum(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static PoliticasPagoJubilacionEnum getEnum(String politicaPago) throws IllegalArgumentException {
        PoliticasPagoJubilacionEnum politicaPagoJubilacionEnum = codigoToEnumMap.get(politicaPago.toUpperCase());
        if (politicaPagoJubilacionEnum == null) {
            String errorMsg = "No se encontró la política de pago de jubilación: " + politicaPago;
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        return politicaPagoJubilacionEnum;
    }
}
