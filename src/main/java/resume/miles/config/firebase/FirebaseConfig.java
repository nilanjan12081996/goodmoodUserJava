package resume.miles.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${FIREBASE_ADMIN_CREDENTIALS:}")
    private String firebaseAdminCredentialsBase64;

    @Bean
    public FirebaseApp firebaseApp() {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        if (firebaseAdminCredentialsBase64 == null || firebaseAdminCredentialsBase64.trim().isEmpty()) {
            logger.warn("FIREBASE_ADMIN_CREDENTIALS is not configured or empty. Skipping Firebase initialization.");
            return null;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(firebaseAdminCredentialsBase64.replaceAll("\\s", ""));
            InputStream serviceAccount = new ByteArrayInputStream(decodedBytes);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            logger.info("FirebaseApp successfully initialized.");
            return FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            logger.error("Failed to initialize FirebaseApp: {}. Push notifications will be disabled.", e.getMessage());
            return null;
        }
    }
}
