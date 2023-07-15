package ec.com.company.core.microservices.calculohipotesiscompanyl;

import ec.com.company.core.microservices.calculohipotesiscompanyl.enums.EstadoEmpresaEnum;
import ec.com.company.core.microservices.calculohipotesiscompanyl.enums.TipoNormativaEnum;
import ec.com.company.core.microservices.calculohipotesiscompanyl.exceptions.coreException;
import ec.com.company.core.microservices.calculohipotesiscompanyl.models.*;
import ec.com.company.core.microservices.calculohipotesiscompanyl.services.EstimacionService;
import ec.com.company.core.microservices.calculohipotesiscompanyl.services.GsonService;
import ec.com.company.core.microservices.calculohipotesiscompanyl.utils.CalculoRotacionEspecificaUtil;
import ec.com.company.core.microservices.calculohipotesiscompanyl.calculators.CalculadoraTasaFinancieraDescuento;
import ec.com.company.core.microservices.calculohipotesiscompanyl.utils.TasaPasivaReferencialUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/")
public class CalculoHipotesiscompanylController {

    private final GsonService gsonService;
    private final EstimacionService estimacionService;

    @Autowired
    public CalculoHipotesiscompanylController(GsonService gsonService, EstimacionService estimacionService) {
        this.gsonService = gsonService;
        this.estimacionService = estimacionService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculoHipotesiscompanylController.class);

    private static final String OK_RESPONSE_MESSAGE = "OK";

    @CrossOrigin()
    @PostMapping(value = "/v1/calculo-tasa-rotacion-especifica",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MicroserviceResponse<?>> calcularTasaRotacion(@Valid @RequestBody InformacionRotacion informacionRotacion) {
        LOGGER.info("Received POST request with body: {}. Starting to calculate tasa de rotación", gsonService.toJson(informacionRotacion));
        final long startTime = System.nanoTime();

        try {
            RespuestaTasaRotacion respuestaTasaRotacion = CalculoRotacionEspecificaUtil.calcular(informacionRotacion);
            MicroserviceResponse<RespuestaTasaRotacion> microserviceResponse = createRespuestaCalculoSuccesfulResponse(respuestaTasaRotacion);
            logOperationTime(startTime);
            return ResponseEntity.ok(microserviceResponse);
        } catch (IllegalArgumentException e) {
            MicroserviceResponse<String> errorResponse = createIllegalArgumentExceptionResponse(e);
            logOperationTime(startTime);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @CrossOrigin()
    @PostMapping(value = "/v1/calculo-tasa-pasiva-referencial",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MicroserviceResponse<?>> calcularTasaPasivaReferencial(@Valid @RequestBody Estudio estudio) throws IOException, ExecutionException, InterruptedException {
        LOGGER.info("Received POST request with body: {}. Starting to calculate tasa pasiva referencial", gsonService.toJson(estudio));
        final long startTime = System.nanoTime();

        String politicaPago = estudio.parametros().politicaPagoJubilacion();
        BigDecimal tasaFinancieraDescuento = estudio.hipotesiscompanyl().getTasaFinancieraDescuento();
        LocalDate fechaValoracion = estudio.parametros().fechaValoracion();

        BigDecimal tasaPasivaReferencial = TasaPasivaReferencialUtil.getTasa(politicaPago, fechaValoracion, tasaFinancieraDescuento);
        // Build the response
        Hipotesiscompanyl hipotesiscompanyl = Hipotesiscompanyl.builder()
                .tasaPasivaReferencial(tasaPasivaReferencial)
                .tasaPasivaReferencialProyectada(tasaPasivaReferencial)
                .build();
        RespuestaCalculo respuestaCalculo = new RespuestaCalculo(hipotesiscompanyl);

        MicroserviceResponse<RespuestaCalculo> microserviceResponse = createRespuestaCalculoSuccesfulResponse(respuestaCalculo);
        logOperationTime(startTime);
        return ResponseEntity.ok(microserviceResponse);
    }

    @CrossOrigin()
    @PostMapping(value = "/v1/calculo-tasa-financiera-descuento",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MicroserviceResponse<?>> calcularTasaFinancieraDescuento(@Valid @RequestBody Estudio estudio)
            throws IOException, ExecutionException, InterruptedException {

        LOGGER.info("Received POST request with body: {}. Starting to calculate tasa pasiva descuento",
                gsonService.toJson(estudio));
        final long startTime = System.nanoTime();

        try {
            var estadoEmpresa = EstadoEmpresaEnum.getEnum(estudio.empresa().estado());
            var tipoNormativa = TipoNormativaEnum.getEnum(estudio.tipoNormativa().codigo());

            var fechaAtf = estudio.actualizacionTasaFinanciera().fecha();
            var empleados = estudio.empleados();

            if (empleados.isEmpty()) {
                String errorMsg = "No se puede calcular la tasa de descuento financiera, no hay empleados";
                LOGGER.warn(errorMsg);
                MicroserviceResponse<String> errorResponse = createBadRequestResponse(errorMsg);
                return ResponseEntity.badRequest().body(errorResponse);
            }

            var tdes = switch (estadoEmpresa) {
                case ANTIGUA ->
                        CalculadoraTasaFinancieraDescuento.getTasaEmpresaAntigua(fechaAtf, tipoNormativa, estudio.estudioAnterior());
                case NUEVA -> CalculadoraTasaFinancieraDescuento.getTasaEmpresaNueva(estudio, estimacionService);
                case INICIA_OPERACIONES ->
                        CalculadoraTasaFinancieraDescuento.getTasaEmpresaIniciaOperaciones(fechaAtf, tipoNormativa, empleados);
            };

            Hipotesiscompanyl hipotesiscompanyl = Hipotesiscompanyl.builder()
                    .tasaFinancieraDescuento(tdes)
                    .build();
            RespuestaCalculo respuestaCalculo = new RespuestaCalculo(hipotesiscompanyl);
            MicroserviceResponse<RespuestaCalculo> microserviceResponse = createRespuestaCalculoSuccesfulResponse(respuestaCalculo);
            logOperationTime(startTime);
            return ResponseEntity.ok(microserviceResponse);

        } catch (IllegalArgumentException | coreException e) {
            MicroserviceResponse<String> errorResponse = createBadRequestResponse(e.toString());
            logOperationTime(startTime);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private <T> MicroserviceResponse<T> createRespuestaCalculoSuccesfulResponse(T respuestaTasa) {
        int httpOkStatusCode = 0; // Blame coworker for returning 0 instead of HttpStatus.OK.value()
        return new MicroserviceResponse<>(httpOkStatusCode, OK_RESPONSE_MESSAGE, respuestaTasa);
    }

    private MicroserviceResponse<String> createIllegalArgumentExceptionResponse(IllegalArgumentException e) {
        return new MicroserviceResponse<>(HttpStatus.BAD_REQUEST.value(), "BAD REQUEST", e.toString());
    }

    private MicroserviceResponse<String> createBadRequestResponse(String errorMsg) {
        return new MicroserviceResponse<>(HttpStatus.BAD_REQUEST.value(), "BAD REQUEST", errorMsg);
    }


    private static void logOperationTime(long startTime) {
        final long estimatedTime = System.nanoTime() - startTime;
        long estimatedTimeInSeconds = Duration.ofNanos(estimatedTime).getSeconds();
        LOGGER.info("Operation took " + estimatedTimeInSeconds + " seconds");
    }
}
