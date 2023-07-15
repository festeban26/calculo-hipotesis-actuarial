package ec.com.company.core.microservices.calculohipotesiscompanyl.facades;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class HipotesiscompanylFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(HipotesiscompanylFacade.class);
    private static HipotesiscompanylFacade instance;
    private final Firestore firestore;

    private static final String BASE_COLLECTION_PATH = "hipotesis_companyles_estandar";

    private HipotesiscompanylFacade(Firestore firestore) {
        this.firestore = firestore;
    }

    public static synchronized HipotesiscompanylFacade getInstance(Firestore firestore) {
        if (instance == null) {
            instance = new HipotesiscompanylFacade(firestore);
        }
        return instance;
    }

    public DocumentSnapshot getDocument(String documentName) throws ExecutionException, InterruptedException {
        DocumentReference documentRef = firestore.collection(BASE_COLLECTION_PATH).document(documentName);
        LOGGER.debug("Retrieving document: {} from collection: {}", documentName, BASE_COLLECTION_PATH);
        return getDocument(documentRef);
    }

    public DocumentSnapshot getDocument(DocumentReference documentRef) throws ExecutionException, InterruptedException {
        LOGGER.debug("Retrieving document from reference: {}", documentRef.getPath());
        ApiFuture<DocumentSnapshot> future = documentRef.get();
        DocumentSnapshot documentSnapshot = future.get();
        LOGGER.debug("Retrieved document: {} from reference: {}", documentSnapshot.getId(), documentRef.getPath());
        return documentSnapshot;
    }
}