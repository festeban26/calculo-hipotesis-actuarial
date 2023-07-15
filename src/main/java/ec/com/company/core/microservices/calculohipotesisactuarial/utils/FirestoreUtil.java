package ec.com.company.core.microservices.calculohipotesiscompanyl.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FirestoreUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirestoreUtil.class);
    private static volatile Firestore firestoreInstance;

    private FirestoreUtil() {
        // Private constructor to prevent instantiation
    }

    public static Firestore getFirestoreInstance() throws IOException {
        if (firestoreInstance == null) {
            synchronized (FirestoreUtil.class) {
                if (firestoreInstance == null) {
                    // Use the application default credentials
                    GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(credentials)
                            .setProjectId("web-app-actas")
                            .build();

                    FirebaseApp.initializeApp(options);

                    firestoreInstance = FirestoreClient.getFirestore();
                    LOGGER.info("Firestore instance created successfully.");
                }
            }
        }
        return firestoreInstance;
    }
}