package fiap.grupo51.fase5.frame_extractor_api.domain.repository;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.utils.RequestFrameExtractorHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestPropertySource("/application-development.yml")
class RequestFrameExtractorRepositoryTest {

    @Mock
    private RequestFrameExtractorRepository requestFrameExtractorRepository;

    AutoCloseable openMock;

    @BeforeEach
    void setup() {
        openMock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMock.close();
    }

    @Test
    void testSave() {

        // Arrange
        var newRequestFrameExtractor = RequestFrameExtractorHandler.generateRequestFrameExtractor();

        var requestFrameExtractorReturn = RequestFrameExtractorHandler.generateRequestFrameExtractor();
        requestFrameExtractorReturn.setAccessKey(UUID.randomUUID().toString());

        when(requestFrameExtractorRepository.save(newRequestFrameExtractor)).thenReturn(requestFrameExtractorReturn);

        // Act
        var requestFrameExtractorSaved = requestFrameExtractorRepository.save(newRequestFrameExtractor);

        // Assert
        assertThat(requestFrameExtractorSaved)
                .isNotNull()
                .isEqualTo(requestFrameExtractorReturn);

        verify(requestFrameExtractorRepository, times(1)).save(any(RequestFrameExtractor.class));

    }

    @Test
    void testDelete() {

        // Arrange
        var newRequestFrameExtractor = RequestFrameExtractorHandler.generateRequestFrameExtractor();

        var requestFrameExtractorReturn = RequestFrameExtractorHandler.generateRequestFrameExtractor();
        requestFrameExtractorReturn.setAccessKey(UUID.randomUUID().toString());

        when(requestFrameExtractorRepository.save(newRequestFrameExtractor)).thenReturn(requestFrameExtractorReturn);

        // Act
        var requestFrameExtractorSaved = requestFrameExtractorRepository.save(newRequestFrameExtractor);
        requestFrameExtractorRepository.delete(requestFrameExtractorSaved);

        // Assert
        assertThat(requestFrameExtractorSaved)
                .isNotNull()
                .isEqualTo(requestFrameExtractorReturn);

        verify(requestFrameExtractorRepository, times(1)).save(any(RequestFrameExtractor.class));
        verify(requestFrameExtractorRepository, times(1)).delete(any(RequestFrameExtractor.class));

    }

    @Test
    void testFindByAccessKey_WhenRequestFrameExtractorExists() {

        // Arrange
        RequestFrameExtractor requestFrameExtractor = RequestFrameExtractorHandler.generateRequestFrameExtractor();
        requestFrameExtractor.setAccessKey(UUID.randomUUID().toString());
        var requestFrameExtractorReturn = Optional.of(requestFrameExtractor);
        when(requestFrameExtractorRepository.findByAccessKey(any(String.class))).thenReturn(requestFrameExtractorReturn);

        // Act
        var requestFrameExtractorSearch = requestFrameExtractorRepository.findByAccessKey(requestFrameExtractor.getAccessKey());

        // Assert
        assertThat(requestFrameExtractorSearch)
                .isPresent()
                .containsSame(requestFrameExtractor);

        requestFrameExtractorSearch.ifPresent( requestFrameExtractorPresent -> {
            assertThat(requestFrameExtractorPresent.getAccessKey()).isEqualTo(requestFrameExtractor.getAccessKey());
        });

        verify(requestFrameExtractorRepository, times(1)).findByAccessKey(any(String.class));
    }

    @Test
    void testFindByAccessKey_WhenRequestFrameExtractorNotExists() {
        // Arrange
        String accessKey = UUID.randomUUID().toString();
        when(requestFrameExtractorRepository.findByAccessKey(accessKey))
                .thenReturn(Optional.empty());

        // Act
        Optional<RequestFrameExtractor> result = requestFrameExtractorRepository.findByAccessKey(accessKey);

        // Assert
        assertTrue(result.isEmpty());
        verify(requestFrameExtractorRepository).findByAccessKey(accessKey);
    }

    @Test
    void testFindAll() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        RequestFrameExtractor requestFrameExtractor1 = RequestFrameExtractorHandler.generateRequestFrameExtractor();
        requestFrameExtractor1.setAccessKey(UUID.randomUUID().toString());

        RequestFrameExtractor requestFrameExtractor2 = RequestFrameExtractorHandler.generateRequestFrameExtractor();
        requestFrameExtractor2.setAccessKey(UUID.randomUUID().toString());

        List<RequestFrameExtractor> expectedRequestFrameExtractorModels = Arrays.asList(
                requestFrameExtractor1,
                requestFrameExtractor2
        );
        Page<RequestFrameExtractor> requestFrameExtractorModels = new PageImpl<>(expectedRequestFrameExtractorModels, pageable, expectedRequestFrameExtractorModels.size());

        // Mock repository call
        when(requestFrameExtractorRepository.findAll(pageable)).thenReturn(requestFrameExtractorModels);

        // Act
        Page<RequestFrameExtractor> result = requestFrameExtractorRepository.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());

        verify(requestFrameExtractorRepository, times(1)).findAll(pageable);

    }

}

