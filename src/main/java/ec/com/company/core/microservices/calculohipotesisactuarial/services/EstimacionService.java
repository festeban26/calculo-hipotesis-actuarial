package ec.com.company.core.microservices.calculohipotesiscompanyl.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import ec.com.company.core.microservices.calculohipotesiscompanyl.exceptions.coreException;
import ec.com.company.core.microservices.calculohipotesiscompanyl.models.Estudio;
import ec.com.company.core.microservices.calculohipotesiscompanyl.models.ResultadosEstimacion;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Servicio que realiza la ejecucion de la estimación simplificada de un estudio companyl
 */
@Service
public class EstimacionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EstimacionService.class);

    private final GsonService gsonService;
    private final String estimacionSimplificadaUrl;
    private final Gson gson;
    private final OkHttpClient httpClient;

    public EstimacionService(GsonService gsonService, @Value("${estimacion.simplificada.url}") String estimacionSimplificadaUrl) {
        this.gsonService = gsonService;
        this.estimacionSimplificadaUrl = estimacionSimplificadaUrl;
        this.gson = new Gson();
        this.httpClient = new OkHttpClient();
    }

    public ResultadosEstimacion sendEstimacionRequest(Estudio estudio) throws coreException {
        LOGGER.info("Sending Estimacion request: {}", gsonService.toJson(estudio));

        MediaType jsonMediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(gsonService.toJson(estudio), jsonMediaType);
        Request request = new Request.Builder()
                .url(estimacionSimplificadaUrl)
                .post(requestBody)
                .build();

        try (Response response = executeHttpRequest(request)) {
            LOGGER.info("Received Estimacion response with status code: {}", response.code());

            JsonObject jsonObject = parseResponseJson(response);

            // If the response does not contain the "content" field, then it means that the request failed.
            // Logic based on the response structure of the microservice.
            if (!jsonObject.has("content")) {
                String errorMessage = jsonObject.getAsJsonPrimitive("message").getAsString();
                LOGGER.error("Microservicio Calculo Jubilación error occurred: {}", errorMessage);
                throw new coreException(errorMessage);
            }

            JsonObject content = jsonObject.getAsJsonObject("content");
            JsonObject estimacion = content.getAsJsonObject("estimacion");

            BigDecimal obd1JubConRotacionSuma = estimacion.getAsJsonPrimitive("obd1JubConRotacion_suma").getAsBigDecimal();
            BigDecimal obd2JubConRotacionSuma = estimacion.getAsJsonPrimitive("obd2JubConRotacion_suma").getAsBigDecimal();
            BigDecimal obdSuma = estimacion.getAsJsonPrimitive("obd_suma").getAsBigDecimal();

            LOGGER.info("Estimacion request successful");
            return new ResultadosEstimacion(obd1JubConRotacionSuma, obd2JubConRotacionSuma, obdSuma);

        } catch (IOException e) {
            LOGGER.error("IO exception occurred during Estimacion request.", e);
        } catch (JsonParseException e) {
            LOGGER.error("Error parsing JSON response during Estimacion request.", e);
        }
        return null;
    }

    private Response executeHttpRequest(Request request) throws IOException {
        LOGGER.debug("Executing HTTP request");
        return httpClient.newCall(request).execute();
    }

    private JsonObject parseResponseJson(Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
            if (responseBody != null) {
                String jsonResponse = responseBody.string();
                LOGGER.debug("Parsing JSON response: {}", jsonResponse);
                return gson.fromJson(jsonResponse, JsonObject.class);
            }
        }
        throw new IOException("Empty response body");
    }
}
