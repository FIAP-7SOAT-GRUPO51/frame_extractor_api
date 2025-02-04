package fiap.grupo51.fase5.frame_extractor_api.domain.repository;

import fiap.grupo51.fase5.frame_extractor_api.api.assembler.RequestFrameExtractorDisassembler;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import fiap.grupo51.fase5.frame_extractor_api.utils.RequestFrameExtractorHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
    properties = {
            "spring.flyway.locations=classpath:db/migration/h2",
            "jwt.key.path=classpath",
            "jwt.key.name-private=app.key",
            "jwt.key.name-public=app.pub",
            "aws.accessKeyId=accessKeyId",
            "aws.region=us-east-1",
            "aws.s3BucketName=bucket-id",
            "aws.secretAccessKey=secretAccessKey"
    })
@TestPropertySource("/application-development.yml")
class RequestFrameExtractorRepositoryIT {

    @Autowired
    private RequestFrameExtractorRepository requestFrameExtractorRepository;

    @Mock
    private RequestFrameExtractorDisassembler requestFrameExtractorDisassembler;

    @Test
    void permiteCriarTabela() {
        var totalRows = requestFrameExtractorRepository.count();
        assertThat(totalRows).isNotNegative();
    }

    @Test
    void testSave() {

        // Arrange
        var requestFrameExtractor = RequestFrameExtractorHandler.generateRequestFrameExtractor();
        requestFrameExtractor.setStatus(RequestFrameExtractorStatus.EM_ABERTO);

        // Act
        var requestFrameExtractorSaved = requestFrameExtractorRepository.save(requestFrameExtractor);

        // Assert
        assertThat(requestFrameExtractorSaved)
                .isNotNull();

    }

    @Test
    void testDelete() {

        // Arrange
        var requestFrameExtractor = RequestFrameExtractorHandler.generateRequestFrameExtractor();
        requestFrameExtractor.setStatus(RequestFrameExtractorStatus.EM_ABERTO);

        // Act
        var requestFrameExtractorSaved = requestFrameExtractorRepository.save(requestFrameExtractor);
        var accessKey = requestFrameExtractorSaved.getAccessKey();
        requestFrameExtractorRepository.delete(requestFrameExtractorSaved);
        var requestFrameExtractorDeleted = requestFrameExtractorRepository.findByAccessKey(accessKey);

        // Assert
        assertThat(requestFrameExtractorSaved).isNotNull();
        assertThat(requestFrameExtractorDeleted).isNotPresent();

    }

    @Test
    void testFindByAccessKey_WhenRequestFrameExtractorExists() {

        // Arrange
        var accessKey = "E4A8F533-DC7F-4766-B782-D731E14764FC"; // Codigo gerado na autocontidas

        // Act
        var requestFrameExtractorSearch = requestFrameExtractorRepository.findByAccessKey(accessKey);

        // Assert
        assertThat(requestFrameExtractorSearch)
                .isPresent();

        requestFrameExtractorSearch.ifPresent( requestFrameExtractor -> {
            assertThat(requestFrameExtractor.getAccessKey()).isEqualTo(accessKey);
        });

    }

    @Test
    void testFindByAccessKey_WhenRequestFrameExtractorNotExists() {
        // Arrange
        var accessKey = UUID.randomUUID().toString();

        // Act
        Optional<RequestFrameExtractor> result = requestFrameExtractorRepository.findByAccessKey(accessKey);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<RequestFrameExtractor> result = requestFrameExtractorRepository.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertThat(result.getContent().size()).isNotNegative();

    }

}

