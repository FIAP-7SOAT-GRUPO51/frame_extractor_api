package fiap.grupo51.fase5.frame_extractor_api.domain.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class AWSProperties {
    private String accessKeyId;
    private String region;
    private String s3BucketName;
    private String secretAccessKey;
}
