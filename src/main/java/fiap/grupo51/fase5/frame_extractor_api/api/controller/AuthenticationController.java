package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.api.openapi.AuthenticationControllerOpenApi;
import fiap.grupo51.fase5.frame_extractor_api.core.security.AuthenticationService;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.TokenIsNotPossibleException;
import fiap.grupo51.fase5.frame_extractor_api.utils.MessageProperty;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
public class AuthenticationController implements AuthenticationControllerOpenApi {

    private final AuthenticationService authenticationService;

    @MessageProperty("token.can-not-possible-generate-token")
    private String canNotPossibleGenerateToken;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("authenticate")
    @Override
    public String authenticate(Authentication authentication) {
        var token = authenticationService.authenticate(authentication);

        if ( token == null ) {
            throw new TokenIsNotPossibleException(canNotPossibleGenerateToken);
        }

        return token;
    }
}
