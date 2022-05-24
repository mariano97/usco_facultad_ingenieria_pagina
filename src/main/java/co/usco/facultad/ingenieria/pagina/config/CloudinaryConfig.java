package co.usco.facultad.ingenieria.pagina.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("cloudinary.config.cloud-name")
    private String cloudName;

    @Value("cloudinary.config.api-key")
    private String apiKey;

    @Value("cloudinary.config.api-secret")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinaryInitConfig() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dd1wcdjrl",
            "api_key", "366767477716548",
            "api_secret", "g8EMr8miKvE4qRYLRpSXQQUkvD8"
        ));
    }

}
