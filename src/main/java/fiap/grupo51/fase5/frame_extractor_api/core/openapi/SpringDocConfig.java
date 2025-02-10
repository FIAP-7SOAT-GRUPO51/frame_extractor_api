package fiap.grupo51.fase5.frame_extractor_api.core.openapi;

import fiap.grupo51.fase5.frame_extractor_api.api.exception.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SpringDocConfig {

    private static final String badRequestResponse = "BadRequestResponse";
    private static final String notFoundResponse = "NotFoundResponse";
    private static final String notAcceptableResponse = "NotAcceptableResponse";
    private static final String internalServerErrorResponse = "InternalServerErrorResponse";

    //@Autowired
    //JwtUrlTokenProperties jwtUrlTokenProperties;

    @Bean
    public GroupedOpenApi groupedOpenApi() {

        // Substituicao de model na documentacao
        //SpringDocUtils.getConfig().replaceWithClass(LogApp.class, LogAppModelOpenApi.class);

        return GroupedOpenApi.builder()
                .group("V1 - API")
                .pathsToMatch("/v1/**")
                .addOpenApiCustomizer(openApiCustomiser())
                .build();
    }

    @Bean
    public OpenApiCustomizer openApiCustomiser() {

        //final String securitySchemeName = "security_auth";

        Contact contact = new Contact();
        contact.setName("FIAP Grupo 51");
        contact.setUrl("https://fiap.grupo51.com.br/");
        contact.setEmail("contato@fiapgrupo51.com.br");

        return openApi -> {

            openApi
                .components(
                    new Components()
                        .schemas(generateSchemas(openApi.getComponents()))
                        //.addSecuritySchemes(securitySchemeName, oauth(securitySchemeName))
                        .responses(generateResponses())
                )
                .info(
                    new Info()
                        .title("Frame Extractor API 1.0")
                        .version("v1")
                        .description("Documentação das APIs core do Frame Extractor")
                        .contact(contact)
//                  .license(new License()
//                      .name("Apache 2.0")
//                      .url("https://www.apache.org/licenses/LICENSE-2.0")
//                  )
                )
                .getPaths()
                .values()
                .forEach(pathItem -> pathItem.readOperationsMap()
                    .forEach((httpMethod, operation) -> {
                        ApiResponses responses = operation.getResponses();
                        switch (httpMethod) {
                            case GET:
                                responses.addApiResponse(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()), new ApiResponse().$ref(notAcceptableResponse));
                                responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(internalServerErrorResponse));
                                break;
                            case POST:
                            case PUT:
                            case DELETE:
                                responses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse().$ref(badRequestResponse));
                                responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(internalServerErrorResponse));
                                break;
                            default:
                                responses.addApiResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), new ApiResponse().$ref(internalServerErrorResponse));
                                break;
                        }
                    })
                );
        };
    }

    protected Map<String, Schema> generateSchemas(Components components) {
        final Map<String, Schema> schemaMap = new HashMap<>();

        Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
        Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);

//        schemaMap.putAll(problemSchema);
//        schemaMap.putAll(problemObjectSchema);

        components.getSchemas().putAll(problemSchema);
        components.getSchemas().putAll(problemObjectSchema);

        return components.getSchemas();
    }

    protected Map<String, ApiResponse> generateResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<Problem>().$ref("ProblemException")));

        apiResponseMap.put(badRequestResponse, new ApiResponse()
                .$ref(badRequestResponse)
                .content(content));

        apiResponseMap.put(notFoundResponse, new ApiResponse()
                .description("Recurso não encontrado")
                .content(content));

        apiResponseMap.put(notAcceptableResponse, new ApiResponse()
                .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
                .content(content));

        apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
                .$ref(internalServerErrorResponse)
                .content(content));

        return apiResponseMap;
    }

//    public SecurityScheme oauth(String securitySchemeName) {
//        return new SecurityScheme()
//                .name(securitySchemeName)
//                .type(SecurityScheme.Type.OAUTH2)
//                .in(SecurityScheme.In.HEADER)
//                .bearerFormat("jwt")
//                .flows(new OAuthFlows()
//                            .password(new OAuthFlow()
//                                    .tokenUrl(jwtUrlTokenProperties.getTokenUrl())
//                                    .scopes(new Scopes()
//                                            .addString("READ", "for READ operations")
//                                            .addString("WRITE", "for WRITE operations")
//                                    )));
//    }

//    @Bean
//    public GroupedOpenApi groupedOpenApiCliente() {
//        return GroupedOpenApi.builder()
//                .group("AlgaFood API Cliente")
//                .pathsToMatch("/cliente/v1/**")
//                .addOpenApiCustomiser(openApi -> {
//                    openApi.info(new Info()
//                            .title("AlgaFood API Cliente")
//                            .version("v1")
//                            .description("REST API do AlgaFood")
//                            .license(new License()
//                                    .name("Apache 2.0")
//                                    .url("http://springdoc.com")
//                            )
//                    ).externalDocs(new ExternalDocumentation()
//                            .description("AlgaWorks")
//                            .url("https://algaworks.com")
//                    );
//                })
//                .build();
//    }

// QUANDO O DOC NAO É AGRUPADO
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("AlgaFood API")
//                        .version("v1")
//                        .description("REST API do AlgaFood")
//                ).externalDocs(new ExternalDocumentation()
//                        .description("AlgaWorks")
//                        .url("https://algaworks.com")
//                );
//    }

}