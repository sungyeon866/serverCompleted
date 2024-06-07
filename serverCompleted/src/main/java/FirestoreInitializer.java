import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirestoreInitializer {
    private static Firestore firestore;

    public static void initializeFirestore() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("C:/javaprogramming-dcd0a-firebase-adminsdk-fj7gs-2f53fbaa2a.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("javaprogramming-dcd0a")
                .build();

        FirebaseApp.initializeApp(options);
        firestore = FirestoreClient.getFirestore();

        System.out.println("Firestore initialized successfully");
    }

    public static Firestore getFirestore() {
        return firestore;
    }
}
