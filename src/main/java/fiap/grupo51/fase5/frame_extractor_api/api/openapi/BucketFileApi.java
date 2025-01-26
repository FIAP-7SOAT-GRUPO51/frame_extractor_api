package fiap.grupo51.fase5.frame_extractor_api.api.openapi;

import fiap.grupo51.fase5.frame_extractor_api.api.model.BucketModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Bucket", description = "Integrações com o bucket")
public interface BucketFileApi {

    @Operation(summary = "Listar arquivos por Nome")
    List<String> listFiles(
            @Parameter(description = "Nome do arquivo (opcional)")
            @RequestParam(value = "filename", required = false) String fileName);

    @Operation(summary = "Realizar o download de um arquivo")
    ResponseEntity<byte[]> downloadFile(
            @Parameter(description = "Nome do arquivo", required = true)
            @RequestParam("fileName") String fileName);

    @Operation(summary = "Realizar o upload de um arquivo")
    ResponseEntity<String> uploadFile(
            @Parameter(description = "Arquivo para upload", required = true)
            @RequestParam("file") MultipartFile file);

    @Operation(summary = "Realizar exclusão de um arquivo")
    ResponseEntity<String> deleteFile(
            @Parameter(description = "Nome do arquivo a ser excluído", required = true)
            @RequestParam("fileName") String fileName);
}