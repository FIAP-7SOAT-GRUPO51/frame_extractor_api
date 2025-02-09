package fiap.grupo51.fase5.frame_extractor_api.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListFilesS3BucketModel {
    private List<String> files;
}
