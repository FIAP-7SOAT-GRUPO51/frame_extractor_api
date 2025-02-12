package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.grupo51.fase5.frame_extractor_api.api.model.ListFilesS3BucketModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUploadInput;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.DomainException;
import fiap.grupo51.fase5.frame_extractor_api.domain.service.BucketFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/bucket")
public class BucketFileController /*implements BucketFileApi*/ {

    private final BucketFileService bucketService;

    public BucketFileController(BucketFileService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping("/downloadFile/{accessKey}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String accessKey) {
        try {
            byte[] content = bucketService.downloadFile(accessKey);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + accessKey + "\"")
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/list")
    public ListFilesS3BucketModel listFilesFromBucket() {
        return bucketService.listFilesFromBucket();
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("data") String jsonParameter,
                                             Authentication authentication) {

        ObjectMapper objectMapper = new ObjectMapper();
        RequestFrameExtractorUploadInput requestFrameExtractorUploadInput = null;
        try {
            requestFrameExtractorUploadInput = objectMapper.readValue(jsonParameter, RequestFrameExtractorUploadInput.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            // Chama o serviço passando os parâmetros necessários
            String responseMessage = bucketService.uploadFile(file, requestFrameExtractorUploadInput);

            // Retorna uma resposta de sucesso

            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
        } catch (IOException e) {
            // Em caso de erro, retorna uma resposta com erro interno
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    @DeleteMapping
    public void deleteFile(@RequestParam("filename") String filename) {
        try {
            bucketService.deleteFile(filename);
        } catch (Exception e) {
            throw new DomainException("Failed to delete file: " + e.getMessage());
        }
    }
}
