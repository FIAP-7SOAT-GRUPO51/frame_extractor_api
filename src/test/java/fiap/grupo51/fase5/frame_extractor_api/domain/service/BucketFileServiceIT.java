package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class BucketFileServiceIT {
    @Autowired
    private BucketFileService bucketFileService;

    @Autowired
    private AmazonS3 s3Client;

    private String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        // Crie o bucket para os testes
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
        }
    }

    @Test
    void testUploadFile() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("file", "testFile.txt", "text/plain", "Test Content".getBytes());

        // Act
        String result = bucketFileService.uploadFile(file);

        // Assert
        assertThat(result).isEqualTo("File uploaded successfully: testFile.txt");
    }

    @Test
    void testDownloadFile() throws IOException {
        // Arrange
        String fileName = "testFile.txt";
        byte[] content = "Test Content".getBytes();
        s3Client.putObject(bucketName, fileName, new ByteArrayInputStream(content), null);

        // Act
        byte[] downloadedContent = bucketFileService.downloadFile(fileName);

        // Assert
        assertArrayEquals(content, downloadedContent);
    }

    @Test
    void testListFiles() {
        // Arrange
        String fileName1 = "testFile1.txt";
        String fileName2 = "testFile2.txt";
        s3Client.putObject(bucketName, fileName1, new ByteArrayInputStream("Content1".getBytes()), null);
        s3Client.putObject(bucketName, fileName2, new ByteArrayInputStream("Content2".getBytes()), null);

        // Act
        List<String> files = bucketFileService.listFiles(null);

        // Assert
        assertThat(files).containsExactlyInAnyOrder(fileName1, fileName2);
    }

    @Test
    void testDeleteFile() {
        // Arrange
        String fileName = "testFile.txt";
        s3Client.putObject(bucketName, fileName, new ByteArrayInputStream("Content".getBytes()), null);

        // Act
        bucketFileService.deleteFile(fileName);

        // Assert
        assertFalse(s3Client.doesObjectExist(bucketName, fileName));
    }
}
