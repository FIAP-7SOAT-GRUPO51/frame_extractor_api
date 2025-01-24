package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.domain.service.BucketFileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;


@WebMvcTest(BucketFileController.class)
public class BucketFileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BucketFileService bucketService;

    @Test
    void testDownloadFile() throws Exception {
        String fileName = "testFile.txt"; // Nome do arquivo
        byte[] fileContent = "Hello, World!".getBytes(); // Conteúdo do arquivo

        // Mockando o comportamento do serviço
        when(bucketService.downloadFile(fileName)).thenReturn(fileContent);

        // Realizando a requisição GET para "/download"
        mockMvc.perform(get("/download")
                        .param("fileName", fileName)) // Passando o parâmetro de consulta "fileName"
                .andExpect(status().isOk()) // Verifica se o status HTTP é 200
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")) // Verifica o cabeçalho de resposta
                .andExpect(content().bytes(fileContent)); // Verifica o conteúdo da resposta como bytes
    }


    @Test
    void testListFiles() throws Exception {
        List<String> fileList = List.of("file1.txt", "file2.txt");

        when(bucketService.listFiles(null)).thenReturn(fileList);

        mockMvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"file1.txt\", \"file2.txt\"]"));
    }

    @Test
    void testUploadFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello".getBytes());

        when(bucketService.uploadFile(Mockito.any(MultipartFile.class))).thenReturn("File uploaded successfully");

        mockMvc.perform(multipart("/uploadFile")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    void testDeleteFile() throws Exception {
        String fileName = "fileToDelete.txt";

        // Mockando o comportamento do serviço
        Mockito.doNothing().when(bucketService).deleteFile(fileName);

        mockMvc.perform(delete("/")
                        .param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(content().string("File deleted successfully: " + fileName));
    }
}
