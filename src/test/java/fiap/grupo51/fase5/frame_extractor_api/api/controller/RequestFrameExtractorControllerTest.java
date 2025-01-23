package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorUpdate;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.RequestFrameExtractorRepository;
import fiap.grupo51.fase5.frame_extractor_api.domain.service.RequestFrameExtractorService;
import fiap.grupo51.fase5.frame_extractor_api.utils.RequestFrameExtractorHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestFrameExtractorControllerTest {

    @Mock
    private RequestFrameExtractorService requestFrameExtractorService;

    @Mock
    RequestFrameExtractorRepository requestFrameExtractorRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RequestFrameExtractorController requestFrameExtractorController;

    AutoCloseable openMocks;

    private RequestFrameExtractorModel mockRequestFrameExtractorModel;
    private Optional<RequestFrameExtractor> mockOptionalRequestFrameExtractor;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        mockRequestFrameExtractorModel = RequestFrameExtractorHandler.generateRequestFrameExtractorModel();
        mockOptionalRequestFrameExtractor = Optional.of(RequestFrameExtractorHandler.generateRequestFrameExtractor());
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testListAllRequestFrameExtractor() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<RequestFrameExtractorModel> requestFrameExtractorModelPage = new PageImpl<>(Collections.singletonList(mockRequestFrameExtractorModel));
        when(requestFrameExtractorService.findAll(pageable)).thenReturn(requestFrameExtractorModelPage);;

        // Act
        Page<RequestFrameExtractorModel> result = requestFrameExtractorController.list(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(requestFrameExtractorService).findAll(pageable);
    }

    @Test
    void testFindByAccessKey_Success() {
        // Arrange
        String accessKey = "E4A8F533-DC7F-4766-B782-D731E14764FC";
        mockRequestFrameExtractorModel.setAccessKey(accessKey);
        when(requestFrameExtractorService.findByAccessKeyToModel(accessKey)).thenReturn(mockRequestFrameExtractorModel);
        when(requestFrameExtractorRepository.findByAccessKey(accessKey)).thenReturn(mockOptionalRequestFrameExtractor);

        // Act
        RequestFrameExtractorModel result = requestFrameExtractorController.findByAccessKey(accessKey);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testAdd_Success() {
        // Arrange
        RequestFrameExtractorInput requestFrameExtractorInput = RequestFrameExtractorHandler.generateRequestFrameExtractorInput();
        when(requestFrameExtractorService.save(requestFrameExtractorInput, authentication)).thenReturn(mockRequestFrameExtractorModel);

        // Act
        RequestFrameExtractorModel result = requestFrameExtractorController.add(requestFrameExtractorInput, authentication);

        // Assert
        assertNotNull(result);
        verify(requestFrameExtractorService, times(1)).save(any(RequestFrameExtractorInput.class),any(Authentication.class));
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        String accesskey = "9999999999999";
        RequestFrameExtractorUpdate requestFrameExtractorUpdate = RequestFrameExtractorHandler.generateRequestFrameExtractorUpdate();
        when(requestFrameExtractorService.update(accesskey,requestFrameExtractorUpdate, authentication)).thenReturn(mockRequestFrameExtractorModel);

        // Act
        RequestFrameExtractorModel result = requestFrameExtractorController.update(accesskey, requestFrameExtractorUpdate, authentication);

        // Assert
        assertNotNull(result);
        verify(requestFrameExtractorService, times(1)).update(any(String.class),any(RequestFrameExtractorUpdate.class),any(Authentication.class));
    }

    @Test
    void testDelete_Success() {
        // Arrange
        String accessKey = "E4A8F533-DC7F-4766-B782-ZZZZZZ";
        mockRequestFrameExtractorModel.setAccessKey(accessKey);
        RequestFrameExtractorInput requestFrameExtractorInput = RequestFrameExtractorHandler.generateRequestFrameExtractorInput();
        when(requestFrameExtractorService.save(requestFrameExtractorInput, authentication)).thenReturn(mockRequestFrameExtractorModel);
        RequestFrameExtractorModel result = requestFrameExtractorController.add(requestFrameExtractorInput, authentication);
        doNothing().when(requestFrameExtractorRepository).deleteById(any(Long.class));
        doNothing().when(requestFrameExtractorService).delete(any(String.class));

        // Act
        requestFrameExtractorService.delete(accessKey);

        // Assert
        assertNotNull(result);
        verify(requestFrameExtractorService, times(1)).save(any(RequestFrameExtractorInput.class),any(Authentication.class));
        verify(requestFrameExtractorService, times(1)).delete(any(String.class));
    }

}