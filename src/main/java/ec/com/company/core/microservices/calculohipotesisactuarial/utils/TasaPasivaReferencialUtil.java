package ec.com.company.core.microservices.calculohipotesiscompanyl.utils;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import ec.com.company.core.microservices.calculohipotesiscompanyl.contants.AppConstants;
import ec.com.company.core.microservices.calculohipotesiscompanyl.enums.PoliticasPagoJubilacionEnum;
import ec.com.company.core.microservices.calculohipotesiscompanyl.facades.HipotesiscompanylFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TasaPasivaReferencialUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TasaPasivaReferencialUtil.class);

    public static BigDecimal getTasa(PoliticasPagoJubilacionEnum politicaPago,
                                     LocalDate fechaValoracion, BigDecimal
                                             tasaFinancieraDescuento)
            throws IOException, ExecutionException, InterruptedException {

        switch (politicaPago) {
            case FONDO_GLOBAL -> {
                return getTasa_politicaPagoFondoGlobal(fechaValoracion);
            }
            case PENSION_MENSUAL -> {
                return getTasa_politicaPagoPensionMensual(tasaFinancieraDescuento);
            }
            default -> {
                var msg = "La política de pago de jubilación no es válida.";
                LOGGER.error(msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public static BigDecimal getTasa(String politicaPago,
                                     LocalDate fechaValoracion,
                                     BigDecimal tasaFinancieraDescuento)
            throws IOException, ExecutionException, InterruptedException, IllegalArgumentException {

        if (politicaPago == null) {
            var msg = "The 'politicaPago' argument is null.";
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (fechaValoracion == null) {
            var msg = "The 'fechaValoracion' argument is null.";
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (tasaFinancieraDescuento == null) {
            var msg = "The 'tasaFinancieraDescuento' argument is null.";
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        var politicaPagoJubilacionEnum = TasaPasivaReferencialUtil.getPoliticaPago(politicaPago);
        return getTasa(politicaPagoJubilacionEnum, fechaValoracion, tasaFinancieraDescuento);
    }

    public static PoliticasPagoJubilacionEnum getPoliticaPago(String politicaPago)
            throws IllegalArgumentException {
        return PoliticasPagoJubilacionEnum.getEnum(politicaPago);
    }

    private static BigDecimal getTasa_politicaPagoPensionMensual(BigDecimal tasaFinancieraDescuento) {
        return tasaFinancieraDescuento;
    }

    private static BigDecimal getTasa_politicaPagoFondoGlobal(LocalDate fechaValoracion)
            throws IOException, ExecutionException, InterruptedException {
        var db = FirestoreUtil.getFirestoreInstance();
        String anioValoracion = String.valueOf(fechaValoracion.get(ChronoField.YEAR));
        DocumentSnapshot hipotesisAnual = HipotesiscompanylFacade.getInstance(db).getDocument(anioValoracion);

        String hipotesisAnualLatestVersion = hipotesisAnual.getString("versionFinal");
        DocumentReference refHipotesisAnualUltimaVersion = hipotesisAnual.getReference().collection("versiones").document(hipotesisAnualLatestVersion);
        DocumentSnapshot hipotesisAnualUltimaVersion = HipotesiscompanylFacade.getInstance(db).getDocument(refHipotesisAnualUltimaVersion);

        Map<String, Number> tasasPasivasReferenciales = (Map) hipotesisAnualUltimaVersion.get("tasasPasivasReferenciales");

        double average = tasasPasivasReferenciales.values().stream()
                .mapToDouble(Number::doubleValue) // Convert to double
                .average() // Calculate the average
                .orElse(0);
        return new BigDecimal(average)
                .setScale(AppConstants.RESPONSE_MAX_BIG_DECIMAL_SCALE, AppConstants.BIG_DECIMAL_ROUNDING_MODE)
                .stripTrailingZeros();
    }
}