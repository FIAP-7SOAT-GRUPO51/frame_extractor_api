package fiap.grupo51.fase5.frame_extractor_api.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(null);
    }

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoderInstance() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder, "PasswordEncoder should not be null");
        assertThat(passwordEncoder.encode("testPassword")).isNotBlank();
    }

}
