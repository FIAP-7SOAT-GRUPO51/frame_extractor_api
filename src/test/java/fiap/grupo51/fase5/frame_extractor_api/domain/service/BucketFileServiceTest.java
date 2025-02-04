package fiap.grupo51.fase5.frame_extractor_api.domain.service;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.BucketFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BucketFileServiceTest {

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private BucketFileRepository bucketFileRepository;

    @InjectMocks
    private BucketFileService bucketFileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFile() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", "testFile.txt", "text/plain", "Test Content".getBytes());
        String description = "Test file";
        int fps = 30;

        when(bucketFileRepository.save(any(RequestFrameExtractor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Simula a persistência

        // Act
        String result = bucketFileService.uploadFile(file, description, fps);

        // Assert
        assertTrue(result.contains("Arquivo enviado com sucesso"));
        verify(s3Client).putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class));
        verify(bucketFileRepository).save(any(RequestFrameExtractor.class));
    }

    @Test
    void testDownloadFile() throws IOException {
        // Arrange
        String fileName = "testFile.txt";
        byte[] expectedContent = "Hello, World!".getBytes();
        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream inputStream = mock(S3ObjectInputStream.class);
        when(s3Object.getObjectContent()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(expectedContent);

        RequestFrameExtractor mockFile = new RequestFrameExtractor(1L, "Test File", fileName, 30, RequestFrameExtractorStatus.EM_PROCESSAMENTO);
        when(bucketFileRepository.findByFileName(fileName)).thenReturn(Optional.of(mockFile));
        when(s3Client.getObject(anyString(), eq(fileName))).thenReturn(s3Object);

        // Act
        byte[] content = bucketFileService.downloadFile(fileName);

        // Assert
        assertArrayEquals(expectedContent, content);
        verify(bucketFileRepository).findByFileName(fileName);
        verify(s3Client).getObject(anyString(), eq(fileName));
    }

    @Test
    void testListFiles() {
        // Arrange
        String file1 = "testFile1.txt";
        String file2 = "testFile2.txt";

        // Simula o comportamento do repositório retornando nomes de arquivos diretamente
        when(bucketFileRepository.findByFileNameContaining(anyString()))
                .thenReturn(Arrays.asList(file1, file2));

        // Act
        List<String> fileList = bucketFileService.listFiles("test");

        // Assert
        assertEquals(2, fileList.size());
        assertTrue(fileList.contains("testFile1.txt"));
        assertTrue(fileList.contains("testFile2.txt"));

        // Verifica se o repositório foi chamado corretamente
        verify(bucketFileRepository).findByFileNameContaining(anyString());
    }


    @Test
    void testDeleteFile() {
        // Arrange
        String fileName = "testFile.txt";
        RequestFrameExtractor fileEntity = new RequestFrameExtractor(1L, "Test File", fileName, 30, RequestFrameExtractorStatus.EM_PROCESSAMENTO);

        when(bucketFileRepository.findByFileName(fileName)).thenReturn(Optional.of(fileEntity));

        // Act
        bucketFileService.deleteFile(fileName);

        // Assert
        verify(s3Client).deleteObject(anyString(), eq(fileName));
        verify(bucketFileRepository).delete(fileEntity);
    }
}