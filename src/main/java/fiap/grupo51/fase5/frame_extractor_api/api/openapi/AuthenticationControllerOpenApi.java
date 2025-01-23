package fiap.grupo51.fase5.frame_extractor_api.api.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;

@Tag(name = "Autenticação", description = "Gerencia autenticação dos clientes")
public interface AuthenticationControllerOpenApi {

    @Operation(summary = "Realiza processo de autenticação e retorna um token com 1h de expiração")
    String authenticate(
            @Parameter(hidden = true)
            Authentication authentication
    );
}