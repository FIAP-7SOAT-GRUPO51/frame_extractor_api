package fiap.grupo51.fase5.frame_extractor_api.api.controller;

import fiap.grupo51.fase5.frame_extractor_api.core.security.AuthenticationService;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.TokenIsNotPossibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController(authenticationService);
    }

    @Test
    public void testAuthenticate_ShouldReturnToken() {
        String expectedToken = "testToken";
        when(authenticationService.authenticate(authentication)).thenReturn(expectedToken);

        String result = authenticationController.authenticate(authentication);

        assertEquals(expectedToken, result);
    }

    @Test
    public void testAuthenticate_ShouldThrowException_WhenTokenIsNull() {
        when(authenticationService.authenticate(authentication)).thenReturn(null);

        assertThrows(TokenIsNotPossibleException.class, () -> {
            authenticationController.authenticate(authentication);
        });
    }
}