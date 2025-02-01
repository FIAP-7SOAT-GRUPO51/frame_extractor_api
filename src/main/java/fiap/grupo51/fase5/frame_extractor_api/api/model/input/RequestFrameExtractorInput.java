package fiap.grupo51.fase5.frame_extractor_api.api.model.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestFrameExtractorInput {

    @NotNull
    private String description;

    @NotNull
    private String fileName;

    @Positive
    private int fps;

    @NotNull
    private String email;

}
