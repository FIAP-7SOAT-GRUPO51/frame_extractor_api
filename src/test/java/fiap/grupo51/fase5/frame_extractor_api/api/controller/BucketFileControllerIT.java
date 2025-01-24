package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.domain.service.BucketFileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BucketFileControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BucketFileService bucketService;

    @Test
    void integrationTestDownloadFile() throws Exception {
        String fileName = "testFile.txt";
        byte[] fileContent = "Integration Test".getBytes();

        Mockito.when(bucketService.downloadFile(fileName)).thenReturn(fileContent);

        mockMvc.perform(get("/download")
                        .param("fileName", fileName))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"testFile.txt\""))
                .andExpect(content().bytes(fileContent));
    }

    @Test
    void integrationTestUploadFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Integration Content".getBytes());

        Mockito.when(bucketService.uploadFile(Mockito.any(MultipartFile.class))).thenReturn("File uploaded successfully");

        mockMvc.perform(multipart("/uploadFile")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }
}
