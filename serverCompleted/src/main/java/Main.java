import com.google.cloud.firestore.Firestore;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            FirestoreInitializer.initializeFirestore();
            Firestore firestore = FirestoreInitializer.getFirestore();
            new StartingFrame(firestore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing Firestore: " + e.getMessage());
        }
    }
}
