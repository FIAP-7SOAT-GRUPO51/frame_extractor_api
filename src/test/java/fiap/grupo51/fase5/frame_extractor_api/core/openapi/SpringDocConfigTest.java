package fiap.grupo51.fase5.frame_extractor_api.core.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpringDocConfigTest {

    @InjectMocks
    private SpringDocConfig springDocConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGroupedOpenApi() {
        GroupedOpenApi groupedOpenApi = springDocConfig.groupedOpenApi();
        assertNotNull(groupedOpenApi);
        assertEquals("V1 - API", groupedOpenApi.getGroup());
    }

    @Test
    void testOpenApiCustomizer() {
        OpenApiCustomizer openApiCustomizer = springDocConfig.openApiCustomiser();
        assertNotNull(openApiCustomizer);
    }

    @Test
    void testGenerateResponses() {
        Map<String, ApiResponse> responses = springDocConfig.generateResponses();
        assertNotNull(responses);
        assertTrue(responses.containsKey("BadRequestResponse"));
        assertTrue(responses.containsKey("NotFoundResponse"));
        assertTrue(responses.containsKey("NotAcceptableResponse"));
        assertTrue(responses.containsKey("InternalServerErrorResponse"));
    }



    @Test
    void testContactInfo() {
        OpenApiCustomizer openApiCustomizer = springDocConfig.openApiCustomiser();
        Info info = new Info();
        Contact contact = new Contact();
        contact.setName("FIAP Grupo 51");
        contact.setUrl("https://fiap.grupo51.com.br/");
        contact.setEmail("contato@fiapgrupo51.com.br");
        info.setContact(contact);
        assertEquals("FIAP Grupo 51", info.getContact().getName());
        assertEquals("https://fiap.grupo51.com.br/", info.getContact().getUrl());
        assertEquals("contato@fiapgrupo51.com.br", info.getContact().getEmail());
    }
}