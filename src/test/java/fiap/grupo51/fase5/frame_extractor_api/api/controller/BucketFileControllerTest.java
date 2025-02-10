//package fiap.grupo51.fase5.frame_extractor_api.api.controller;
//
//import fiap.grupo51.fase5.frame_extractor_api.domain.service.BucketFileService;
//import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUploadInput;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class BucketFileControllerTest {
//
//    private BucketFileController bucketFileController;
//
//    @Mock
//    private BucketFileService bucketFileService;
//
//    @Mock
//    private MultipartFile multipartFile;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        bucketFileController = new BucketFileController(bucketFileService);
//    }
//
//    @Test
//    public void testDownloadFile_ShouldReturnFileContent() throws IOException {
//        String filename = "test.txt";
//        byte[] content = "file content".getBytes();
//
//        when(bucketFileService.downloadFile(filename)).thenReturn(content);
//
//        ResponseEntity<byte[]> response = bucketFileController.downloadFile(filename);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(content, response.getBody());
//    }
//
//    @Test
//    public void testDownloadFile_ShouldReturnInternalServerError() throws IOException {
//        String filename = "test.txt";
//
//        when(bucketFileService.downloadFile(filename)).thenThrow(new IOException("Error"));
//
//        ResponseEntity<byte[]> response = bucketFileController.downloadFile(filename);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    public void testListFiles_ShouldReturnFileNames() {
//        String filename = "test";
//        List<String> fileNames = Arrays.asList("file1.txt", "file2.txt");
//
//        when(bucketFileService.listFiles(filename)).thenReturn(fileNames);
//
//        List<String> response = bucketFileController.listFiles(filename);
//
//        assertEquals(fileNames, response);
//    }
//
//    @Test
//    public void testUploadFile_ShouldReturnSuccessMessage() throws IOException {
//        String description = "test description";
//        int fps = 30;
//        String responseMessage = "File uploaded successfully";
//        RequestFrameExtractorUploadInput input = new RequestFrameExtractorUploadInput(description, fps);
//
//        when(bucketFileService.uploadFile(multipartFile, input)).thenReturn(responseMessage);
//
//        ResponseEntity<String> response = bucketFileController.uploadFile(multipartFile, description, fps);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(responseMessage, response.getBody());
//    }
//
//    @Test
//    public void testUploadFile_ShouldReturnInternalServerError() throws IOException {
//        String description = "test description";
//        int fps = 30;
//        RequestFrameExtractorUploadInput input = new RequestFrameExtractorUploadInput(description, fps);
//
//        when(bucketFileService.uploadFile(multipartFile, input)).thenThrow(new IOException("Error"));
//
//        ResponseEntity<String> response = bucketFileController.uploadFile(multipartFile, description, fps);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//
//    @Test
//    public void testDeleteFile_ShouldReturnSuccessMessage() {
//        String filename = "test.txt";
//
//        doNothing().when(bucketFileService).deleteFile(filename);
//
//        ResponseEntity<String> response = bucketFileController.deleteFile(filename);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("File deleted successfully: " + filename, response.getBody());
//    }
//
//    @Test
//    public void testDeleteFile_ShouldReturnInternalServerError() {
//        String filename = "test.txt";
//
//        doThrow(new RuntimeException("Error")).when(bucketFileService).deleteFile(filename);
//
//        ResponseEntity<String> response = bucketFileController.deleteFile(filename);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
//}