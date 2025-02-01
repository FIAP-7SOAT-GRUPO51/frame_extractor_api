package fiap.grupo51.fase5.frame_extractor_api.api.model;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestFrameExtractorModel {

    private String description;
    private String fileName;
    private int fps;
    private String accessKey;
    private RequestFrameExtractorStatus status;
    private String email;

}
