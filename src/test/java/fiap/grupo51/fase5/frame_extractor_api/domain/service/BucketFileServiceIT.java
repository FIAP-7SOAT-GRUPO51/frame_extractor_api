package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@SpringBootTest
public class BucketFileServiceIT {

    @Autowired
    private BucketFileService bucketFileService;

    @Mock
    private AmazonS3 s3Client; // Mock do S3 para evitar chamadas reais

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        // Simula a criação do bucket no mock do Amazon S3
        when(s3Client.doesBucketExistV2(bucketName)).thenReturn(true);
    }

    @Test
    void testUploadFile() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", "testFile.txt", "text/plain", "Test Content".getBytes());
        String description = "Arquivo de teste";
        int fps = 30;

        // Act
        String result = bucketFileService.uploadFile(file, description, fps);

        // Assert
        assertThat(result).contains("Arquivo enviado com sucesso: "); // Não pode verificar nome exato pois há UUID

        // Verifica se o arquivo foi enviado corretamente para o S3
        verify(s3Client).putObject(eq(bucketName), anyString(), any(InputStream.class), any());
    }

    @Test
    void testDownloadFile() throws IOException {
        // Arrange
        String fileName = "testFile.txt";
        byte[] expectedContent = "Test Content".getBytes();

        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream inputStream = new S3ObjectInputStream(new ByteArrayInputStream(expectedContent), null);

        when(s3Object.getObjectContent()).thenReturn(inputStream);
        when(s3Client.getObject(bucketName, fileName)).thenReturn(s3Object);

        // Act
        byte[] downloadedContent = bucketFileService.downloadFile(fileName);

        // Assert
        assertArrayEquals(expectedContent, downloadedContent);
        verify(s3Client).getObject(bucketName, fileName);
    }

    @Test
    void testListFiles() {
        // Arrange
        ObjectListing objectListing = mock(ObjectListing.class);
        S3ObjectSummary summary1 = mock(S3ObjectSummary.class);
        S3ObjectSummary summary2 = mock(S3ObjectSummary.class);

        when(summary1.getKey()).thenReturn("testFile1.txt");
        when(summary2.getKey()).thenReturn("testFile2.txt");
        when(objectListing.getObjectSummaries()).thenReturn(Arrays.asList(summary1, summary2));
        when(s3Client.listObjects(bucketName)).thenReturn(objectListing);

        // Act
        List<String> files = bucketFileService.listFiles(null);

        // Assert
        assertThat(files).containsExactlyInAnyOrder("testFile1.txt", "testFile2.txt");
        verify(s3Client).listObjects(bucketName);
    }

    @Test
    void testDeleteFile() {
        // Arrange
        String fileName = "testFile.txt";

        // Act
        bucketFileService.deleteFile(fileName);

        // Assert
        verify(s3Client).deleteObject(bucketName, fileName);
    }
}
