package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import fiap.grupo51.fase5.frame_extractor_api.api.assembler.RequestFrameExtractorAssembler;
import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.RequestFrameExtractorNotFindException;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.RequestFrameExtractorRepository;
import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUserUtils;
import fiap.grupo51.fase5.frame_extractor_api.utils.ResourceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static fiap.grupo51.fase5.frame_extractor_api.utils.RequestFrameExtractorHandler.generateRequestFrameExtractorInput;
import static fiap.grupo51.fase5.frame_extractor_api.utils.RequestFrameExtractorHandler.generateRequestFrameExtractorUpdate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
    properties = {
            "spring.flyway.locations=classpath:db/migration/h2",
            "jwt.key.path=classpath",
            "jwt.key.name-private=app.key",
            "jwt.key.name-public=app.pub"
    })
@TestPropertySource("/application-development.yml")
class RequestFrameExtractorServiceIT {

    @Autowired
    private RequestFrameExtractorService requestFrameExtractorService;

    @Autowired
    private RequestFrameExtractorAssembler requestFrameExtractorAssembler;

    @Autowired
    private RequestFrameExtractorRepository requestFrameExtractorRepository;

    @Autowired
    private FrameExtractorUserUtils frameExtractorUserUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private Authentication authentication;

    private Map<String, String> env;
    private User userAudit;

    @BeforeEach
    void setUp() {
        userAudit = userService.findOrElseByLogin("piloto");
        env = ResourceUtils.getEnv();
        this.authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(env.get("JWT_TOKEN_USERNAME"), env.get("JWT_TOKEN_PASSWORD"))
        );
    }

    @Test
    void testSaveRequestFrameExtractor_Success() {
        // Arrange
        RequestFrameExtractorInput requestFrameExtractorInput = generateRequestFrameExtractorInput();

        // Act
        RequestFrameExtractorModel savedCustom = requestFrameExtractorService.save(requestFrameExtractorInput, authentication);

        // Assert
        assertNotNull(savedCustom);
    }

    @Test
    void testUpdateRequestFrameExtractor_Success() {
        // Arrange
        RequestFrameExtractor requestFrameExtractor = requestFrameExtractorService.findByAccessKey("E4A8F533-DC7F-4766-B782-D731E14764FC");

        var requestFrameExtractorUpdate = generateRequestFrameExtractorUpdate();
        requestFrameExtractorUpdate.setDescription("ALTERADO");

        // Act
        RequestFrameExtractorModel savedCustom = requestFrameExtractorService.update(requestFrameExtractor.getAccessKey(), requestFrameExtractorUpdate, authentication);

        // Assert
        assertNotNull(savedCustom);
        assertThat(savedCustom.getDescription()).isEqualTo("ALTERADO");
        assertThat(savedCustom.getAccessKey()).isEqualTo(requestFrameExtractor.getAccessKey());
    }

    @Test
    void testDeleteRequestFrameExtractor_Success() {
        // Arrange
        RequestFrameExtractorInput requestFrameExtractorInput = generateRequestFrameExtractorInput();

        // Act
        RequestFrameExtractorModel savedCustom = requestFrameExtractorService.save(requestFrameExtractorInput, authentication);
        var accessKey = savedCustom.getAccessKey();

        requestFrameExtractorService.delete(accessKey);

        // Assert
        assertNotNull(accessKey);
        assertThrows(RequestFrameExtractorNotFindException.class, () -> {
            requestFrameExtractorService.findByAccessKey(accessKey);
        });
    }

    @Test
    void testListAllRequestFrameExtractors_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<RequestFrameExtractorModel> resultPage = requestFrameExtractorService.findAll(pageable);

        // Assert
        assertNotNull(resultPage);
        assertThat(resultPage.getContent().size()).isNotNegative();
    }

    @Test
    void testFindByAccessKey_Success() {
        // Arrange
        var accessKey = "E4A8F533-DC7F-4766-B782-D731E14764FC";

        // Act
        RequestFrameExtractor requestFrameExtractor = requestFrameExtractorService.findByAccessKey(accessKey);

        // Assert
        assertNotNull(requestFrameExtractor);
    }

    @Test
    void testFindByAccessKey_NotFound() {
        // Arrange
        var accessKey = "E4A8F533-DC7F-4766-B782-ZZZZZ";

        // Act & Assert
        assertThrows(RequestFrameExtractorNotFindException.class, () -> {
            requestFrameExtractorService.findByAccessKey(accessKey);
        });
    }

}
