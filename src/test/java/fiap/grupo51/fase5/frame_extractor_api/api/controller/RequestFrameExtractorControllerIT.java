package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.api.model.RequestFrameExtractorModel;
import fiap.grupo51.fase5.frame_extractor_api.api.model.input.RequestFrameExtractorInput;
import fiap.grupo51.fase5.frame_extractor_api.core.security.JwtService;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.RequestFrameExtractorNotFindException;
import fiap.grupo51.fase5.frame_extractor_api.domain.service.RequestFrameExtractorService;
import fiap.grupo51.fase5.frame_extractor_api.utils.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
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
class RequestFrameExtractorControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RequestFrameExtractorService requestFrameExtractorService;

    private Map<String, String> env;
    private String token = "";
    private Authentication authentication;

    @BeforeEach
    void setUp() {

        env = ResourceUtils.getEnv();
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(env.get("JWT_TOKEN_USERNAME"), env.get("JWT_TOKEN_PASSWORD"))
        );
        token = jwtService.generateToken(authentication);
        RestAssured.basePath = "/v1/request-frame-extractor";

    }

    @Test
    void testListAllRequestFrameExtractor() {
        given()
            .contentType(ContentType.JSON)
            .param("page", 0)
            .param("size", 10)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(this.token))
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("items", notNullValue())
            .body("items.size()", greaterThanOrEqualTo(0));
    }

    @Test
    void testFindByAccessKey_Success() {
        given()
            .pathParam("accessKey", "E4A8F533-DC7F-4766-B782-D731E14764FC")
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(this.token))
        .when()
            .get("/{accessKey}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("accessKey", notNullValue());
    }

    @Test
    void testAdd_Success() {

        String requestFrameExtractorInput = """
        {            
            "description": "Post Teste",
            "fileName": "test.mp4",
            "email": "test@gmail.com",
            "fps": 1
        }
        """;

        String accessKey =
                given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(requestFrameExtractorInput)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(this.token))
                .when()
                    .post()
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("accessKey", notNullValue())
                    .extract().body().jsonPath().getObject("accessKey", String.class);

        // Additional validation can be added here if needed
        assertNotNull(accessKey);
    }

    @Test
    void testUpdate_Success() {

        String requestFrameExtractorUpdate = """
        {
            "description": "ALTERADO 2",
            "fileName": "test.mp4",
            "fps": 1
        }
        """;

        String accessKey = "E4A8F533-DC7F-4766-B782-D731E14764FC";
        String description =
                given()
                    .pathParam("accessKey", accessKey)
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(requestFrameExtractorUpdate)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(this.token))
                .when()
                    .put("/{accessKey}")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("description", notNullValue())
                    .extract().body().jsonPath().getObject("description", String.class);

        assertThat(description).isEqualTo("ALTERADO 2");
    }

    @Test
    void testDelete_Success() {

        RequestFrameExtractorInput requestFrameExtractorInput = new RequestFrameExtractorInput();
        requestFrameExtractorInput.setDescription("Delete Teste");
        requestFrameExtractorInput.setFileName("test.mp4");
        requestFrameExtractorInput.setFps(1);

        RequestFrameExtractorModel requestFrameExtractorModel = requestFrameExtractorService.save(requestFrameExtractorInput, authentication);
        String accessKey = requestFrameExtractorModel.getAccessKey();

        given()
            .pathParam("accessKey", accessKey)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(requestFrameExtractorInput)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(this.token))
        .when()
            .delete("/{accessKey}")
        .then()
            .statusCode(HttpStatus.OK.value());

        // Additional validation can be added here if needed
        assertNotNull(accessKey);
        assertThrows(RequestFrameExtractorNotFindException.class, () -> {
            requestFrameExtractorService.findByAccessKey(accessKey);
        });
    }

}