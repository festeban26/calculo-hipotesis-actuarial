package ec.com.company.core.microservices.calculohipotesiscompanyl.facades;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.type.Month;
import ec.com.company.core.microservices.calculohipotesiscompanyl.enums.TipoNormativaEnum;
import ec.com.company.core.microservices.calculohipotesiscompanyl.models.TasaPorDuracion;
import ec.com.company.core.microservices.calculohipotesiscompanyl.models.TasaPorTfPromedio;
import ec.com.company.core.microservices.calculohipotesiscompanyl.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class ActualizacionTasaFinancieraFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActualizacionTasaFinancieraFacade.class);
    private static final String BASE_COLLECTION_PATH = "actualizaciones_tasa_financiera";
    private static final String SUB_COLLECTION_TASAS_POR_DURACION = "tasas_por_duracion";
    private static final String SUB_COLLECTION_TASAS_POR_TF = "tasas_por_tf";

    private static ActualizacionTasaFinancieraFacade instance;
    private final Firestore firestore;

    private ActualizacionTasaFinancieraFacade(Firestore firestore) {
        this.firestore = firestore;
    }

    public static ActualizacionTasaFinancieraFacade getInstance(Firestore firestore) {
        if (instance == null) {
            instance = new ActualizacionTasaFinancieraFacade(firestore);
        }
        return instance;
    }

    public List<TasaPorDuracion> getTasasPorDuracion(LocalDate fechaActualizacionTasaFinanciera,
                                                     TipoNormativaEnum tipoNormativa)
            throws ExecutionException, InterruptedException {

        String date = DateUtil.formatDate(fechaActualizacionTasaFinanciera);
        String codigoNormativa = tipoNormativa.getCodigo();

        CollectionReference tasaCollection =
                getTasaCollectionReference(date, codigoNormativa, SUB_COLLECTION_TASAS_POR_DURACION);

        List<TasaPorDuracion> tasasPorDuracion = retrieveTasas(tasaCollection,
                this::convertSnapshotToTasaPorDuracion);
        LOGGER.info("Retrieved {} tasasPorDuracion for fechaActualizacionTasaFinanciera: {} and tipoNormativa: {}",
                tasasPorDuracion.size(), fechaActualizacionTasaFinanciera, tipoNormativa);
        return tasasPorDuracion;
    }

    public List<TasaPorTfPromedio> getTasasPorTfPromedioMasRecientes(LocalDate fechaEstimacion,
                                                                     TipoNormativaEnum tipoNormativa)
            throws ExecutionException, InterruptedException {
        LocalDate fechaActualizacionTasaFinanciera = fechaEstimacion.withMonth(Month.DECEMBER_VALUE).withDayOfMonth(31);
        String date = DateUtil.formatDate(fechaActualizacionTasaFinanciera);
        String codigoNormativa = tipoNormativa.getCodigo();

        CollectionReference tasaCollection =
                getTasaCollectionReference(date, codigoNormativa, SUB_COLLECTION_TASAS_POR_TF);

        List<TasaPorTfPromedio> tasasPorTf = retrieveTasas(tasaCollection, this::convertSnapshotToTasasPorTf);
        LOGGER.info("Retrieved {} tasasPorTf for fechaActualizacionTasaFinanciera: {} and tipoNormativa: {}",
                tasasPorTf.size(), fechaActualizacionTasaFinanciera, tipoNormativa);
        return tasasPorTf;
    }

    private CollectionReference getTasaCollectionReference(String date, String codigoNormativa,
                                                           String subCollection) {

        CollectionReference tasaCollectionReference = firestore
                .collection(BASE_COLLECTION_PATH)
                .document(date)
                .collection("normativas")
                .document(codigoNormativa)
                .collection(subCollection);
        LOGGER.debug("Retrieving tasas from collection: {}", tasaCollectionReference.getPath());
        return tasaCollectionReference;
    }

    private <T> List<T> retrieveTasas(CollectionReference tasaCollection,
                                      Function<List<QueryDocumentSnapshot>, List<T>> conversionFunction)
            throws ExecutionException, InterruptedException {
        LOGGER.debug("Executing Firestore query to retrieve tasas...");
        ApiFuture<QuerySnapshot> future = tasaCollection.get();
        QuerySnapshot querySnapshot = future.get();
        LOGGER.debug("Firestore query execution completed. Retrieved {} documents", querySnapshot.size());

        List<QueryDocumentSnapshot> tasaDocumentSnapshots = querySnapshot.getDocuments();
        LOGGER.debug("Converting QueryDocumentSnapshots to tasas...");
        List<T> tasas = conversionFunction.apply(tasaDocumentSnapshots);
        LOGGER.debug("Conversion of QueryDocumentSnapshots to tasas completed.");
        return tasas;
    }

    private List<TasaPorDuracion> convertSnapshotToTasaPorDuracion(List<QueryDocumentSnapshot> tasaDocumentSnapshots) {
        List<TasaPorDuracion> tasasPorDuracion = new ArrayList<>();
        LOGGER.debug("Converting QueryDocumentSnapshots to tasasPorDuracion...");

        for (QueryDocumentSnapshot tasaSnapshot : tasaDocumentSnapshots) {
            Double duracion = tasaSnapshot.getDouble("duracion");
            Double tasa = tasaSnapshot.getDouble("tasa");

            if (duracion != null && tasa != null) {
                TasaPorDuracion tasaPorDuracion = new TasaPorDuracion(duracion, tasa);
                tasasPorDuracion.add(tasaPorDuracion);
                LOGGER.debug("Added TasaPorDuracion: duracion={}, tasa={}", duracion, tasa);
            } else {
                LOGGER.warn("Invalid tasa document found: {}", tasaSnapshot.getId());
            }
        }

        LOGGER.debug("Conversion of QueryDocumentSnapshots to tasasPorDuracion completed.");
        return tasasPorDuracion;
    }

    private List<TasaPorTfPromedio> convertSnapshotToTasasPorTf(List<QueryDocumentSnapshot> tasaDocumentSnapshots) {
        List<TasaPorTfPromedio> tasasPorTf = new ArrayList<>();
        LOGGER.debug("Converting QueryDocumentSnapshots to tasasPorTf...");

        for (QueryDocumentSnapshot tasaSnapshot : tasaDocumentSnapshots) {
            var desdeDouble = tasaSnapshot.getDouble("tfDesde");
            var hastaDouble = tasaSnapshot.getDouble("tfHasta");
            Double tasa = tasaSnapshot.getDouble("tasa");

            if (desdeDouble != null && hastaDouble != null && tasa != null) {
                var desde = desdeDouble.intValue();
                var hasta = hastaDouble.intValue();
                TasaPorTfPromedio tasaPorTf = new TasaPorTfPromedio(desde, hasta, tasa);
                tasasPorTf.add(tasaPorTf);
                LOGGER.debug("Added TasaPorTf: desde={}, hasta={}, tasa={}", desde, hasta, tasa);
            } else {
                LOGGER.warn("Invalid tasa document found: {}", tasaSnapshot.getId());
            }
        }

        LOGGER.debug("Conversion of QueryDocumentSnapshots to tasasPorTf completed.");
        return tasasPorTf;
    }
}