package ec.com.company.core.microservices.calculohipotesiscompanyl.services;

import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;

import com.google.gson.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class GsonService {
    private final Gson gson;

    public GsonService() {
        this.gson = createGson();
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext)
                        -> new JsonPrimitive(localDate.format(AppConstants.DATE_FORMATTER)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (je, type, jdc)
                        -> LocalDate.parse(je.getAsString(), AppConstants.DATE_FORMATTER))
                .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
                    BigDecimal value = BigDecimal.valueOf(src);
                    if (value.scale() > AppConstants.JSONS_MAX_NUMERIC_SCALE) {
                        value = value.setScale(AppConstants.JSONS_MAX_NUMERIC_SCALE, AppConstants.JSONS_NUMERIC_ROUNDING_MODE);
                    }
                    return new JsonPrimitive(value);
                })
                .registerTypeAdapter(BigDecimal.class, (JsonSerializer<BigDecimal>) (src, typeOfSrc, context) -> {
                    if (src.scale() > AppConstants.JSONS_MAX_NUMERIC_SCALE) {
                        src = src.setScale(AppConstants.JSONS_MAX_NUMERIC_SCALE, AppConstants.JSONS_NUMERIC_ROUNDING_MODE);
                    }
                    return new JsonPrimitive(src);
                })
                .create();
    }
}