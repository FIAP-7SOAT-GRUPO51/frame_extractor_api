package fiap.grupo51.fase5.frame_extractor_api.api.model;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BucketModel {

    private String fileName;         // Nome do arquivo
    private String contentType;      // Tipo de conteúdo (e.g., "application/pdf", "image/png")
    private long size;               // Tamanho do arquivo em bytes
    private String url;              // URL de acesso ao arquivo no bucket
    private String description;      // Descrição opcional do arquivo
}