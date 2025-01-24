package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class BucketFileServiceTest {
    @Mock
    private AmazonS3 s3Client;

    private BucketFileService bucketFileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicia os mocks
        bucketFileService = new BucketFileService("accessKeyId", "secretKey", "region");
        bucketFileService.setS3Client(s3Client); // Injeta o mock do S3
    }

    @Test
    void testUploadFile() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", "testFile.txt", "text/plain", "Test Content".getBytes());

        // Act
        String result = bucketFileService.uploadFile(file);

        // Assert
        verify(s3Client).putObject(anyString(), eq("testFile.txt"), any(), isNull());
        assertEquals("File uploaded successfully: testFile.txt", result);
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
        when(s3Client.getObject(anyString(), eq(fileName))).thenReturn(s3Object);

        // Act
        byte[] content = bucketFileService.downloadFile(fileName);

        // Assert
        assertArrayEquals(expectedContent, content);
    }

    @Test
    void testListFiles() {
        // Arrange
        ObjectListing objectListing = mock(ObjectListing.class);
        S3ObjectSummary summary1 = mock(S3ObjectSummary.class);
        when(summary1.getKey()).thenReturn("testFile1.txt");
        S3ObjectSummary summary2 = mock(S3ObjectSummary.class);
        when(summary2.getKey()).thenReturn("testFile2.txt");

        when(objectListing.getObjectSummaries()).thenReturn(Arrays.asList(summary1, summary2));
        when(s3Client.listObjects(anyString())).thenReturn(objectListing);

        // Act
        List<String> fileList = bucketFileService.listFiles(null);

        // Assert
        assertEquals(2, fileList.size());
        assertTrue(fileList.contains("testFile1.txt"));
        assertTrue(fileList.contains("testFile2.txt"));
    }

    @Test
    void testDeleteFile() {
        // Arrange
        String fileName = "testFile.txt";

        // Act
        bucketFileService.deleteFile(fileName);

        // Assert
        verify(s3Client).deleteObject(anyString(), eq(fileName));
    }
}
