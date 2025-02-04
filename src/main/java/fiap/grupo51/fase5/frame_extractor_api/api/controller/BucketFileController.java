package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.api.openapi.BucketFileApi;
import fiap.grupo51.fase5.frame_extractor_api.domain.service.BucketFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/bucket")
public class BucketFileController implements BucketFileApi {

    private final BucketFileService bucketService;

    public BucketFileController(BucketFileService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") @PathVariable String filename) {
        try {
            byte[] content = bucketService.downloadFile(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/list/{filename}")
    public List<String> listFiles(@PathVariable String filename) {
        List<String> fileNames = bucketService.listFiles(filename);
        return ResponseEntity.ok(fileNames).getBody();
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("description") String description,
                                             @RequestParam("fps") int fps) {
        try {
            // Chama o serviço passando os parâmetros necessários
            String responseMessage = bucketService.uploadFile(file, description, fps);

            // Retorna uma resposta de sucesso
            return ResponseEntity.ok(responseMessage);
        } catch (IOException e) {
            // Em caso de erro, retorna uma resposta com erro interno
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@RequestParam("filename") @PathVariable String filename) {
        try {
            bucketService.deleteFile(filename);
            return ResponseEntity.ok("File deleted successfully: " + filename);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete file: " + e.getMessage());
        }
    }
}
