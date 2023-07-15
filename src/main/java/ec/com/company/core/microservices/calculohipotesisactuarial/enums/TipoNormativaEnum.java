package ec.com.company.core.microservices.calculohipotesiscompanyl.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum TipoNormativaEnum {
    BONOS_EEUU("CE"),
    BONOS_LOCALES("CL"),
    PYMES("PY"),
    NEC("NC");

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoNormativaEnum.class);
    private static final Map<String, TipoNormativaEnum> codigoToEnumMap = new HashMap<>();

    static {
        for (TipoNormativaEnum tipoNormativaEnum : values()) {
            codigoToEnumMap.put(tipoNormativaEnum.getCodigo(), tipoNormativaEnum);
        }
    }

    private final String codigo;

    TipoNormativaEnum(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TipoNormativaEnum getEnum(String tipoNormativa) throws IllegalArgumentException {
        TipoNormativaEnum tipoNormativaEnum = codigoToEnumMap.get(tipoNormativa.toUpperCase());
        if (tipoNormativaEnum == null) {
            String errorMsg = "No se encontró el tipo de tipoNormativa: " + tipoNormativa;
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        return tipoNormativaEnum;
    }
}
