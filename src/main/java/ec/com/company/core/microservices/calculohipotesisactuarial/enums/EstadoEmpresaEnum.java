package ec.com.company.core.microservices.calculohipotesiscompanyl.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum EstadoEmpresaEnum {
    ANTIGUA("A"),
    NUEVA("N"),
    INICIA_OPERACIONES("I");

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoEmpresaEnum.class);
    private static final Map<String, EstadoEmpresaEnum> codigoToEnumMap = new HashMap<>();

    static {
        for (EstadoEmpresaEnum estadoEmpresaEnum : values()) {
            codigoToEnumMap.put(estadoEmpresaEnum.getCodigo(), estadoEmpresaEnum);
        }
    }

    private final String codigo;

    EstadoEmpresaEnum(final String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static EstadoEmpresaEnum getEnum(String codigoEmpresa) throws IllegalArgumentException {
        EstadoEmpresaEnum estadoEmpresaEnum = codigoToEnumMap.get(codigoEmpresa.toUpperCase());
        if (estadoEmpresaEnum == null) {
            String errorMsg = "No se encontró el estado de la empresa: " + codigoEmpresa;
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        return estadoEmpresaEnum;
    }
}