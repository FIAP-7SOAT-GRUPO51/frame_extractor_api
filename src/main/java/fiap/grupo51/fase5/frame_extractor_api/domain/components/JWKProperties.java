package fiap.grupo51.fase5.frame_extractor_api.domain.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JWKProperties {

    private KeyConfig key;

    @Getter
    @Setter
    public static class KeyConfig {
        private String path;
        private String namePrivate;
        private String namePublic;
    }
}
