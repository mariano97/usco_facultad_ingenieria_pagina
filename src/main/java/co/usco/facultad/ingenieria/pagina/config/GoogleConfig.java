package co.usco.facultad.ingenieria.pagina.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class GoogleConfig {

    @Value("classpath:config/google/firebase-credentials.json")
    private Resource resource;

    @Bean
    public Storage googleCloudStorageConfig() throws IOException {
        /* try {
            Credentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
            System.out.println(">>>>>>>>>> Dentro de bean");
            System.out.println(credentials.getAuthenticationType());
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return StorageOptions.getDefaultInstance().getService(); */
        Credentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        System.out.println(">>>>>>>>>> Dentro de bean");
        System.out.println(credentials.getAuthenticationType());
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

}
