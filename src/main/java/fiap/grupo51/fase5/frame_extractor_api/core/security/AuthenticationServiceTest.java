package fiap.grupo51.fase5.frame_extractor_api.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private JwtService jwtService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        // Mockamos a dependência JwtService
        jwtService = mock(JwtService.class);
        authenticationService = new AuthenticationService(jwtService);
    }

    @Test
    void authenticate_ShouldReturnGeneratedToken() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        String expectedToken = "mocked-jwt-token";

        // Configuramos o comportamento de jwtService.generateToken
        when(jwtService.generateToken(authentication)).thenReturn(expectedToken);

        // Act
        String result = authenticationService.authenticate(authentication);

        // Assert
        assertEquals(expectedToken, result, "O token gerado deve ser o mesmo retornado pelo JwtService");
        verify(jwtService, times(1)).generateToken(authentication); // Verifica se o método foi chamado uma vez
    }

    @Test
    void authenticate_ShouldCallJwtServiceGenerateToken() {
        // Arrange
        Authentication authentication = mock(Authentication.class);

        // Act
        authenticationService.authenticate(authentication);

        // Assert
        verify(jwtService, times(1)).generateToken(authentication); // Verifica se o método foi chamado exatamente uma vez
    }
}