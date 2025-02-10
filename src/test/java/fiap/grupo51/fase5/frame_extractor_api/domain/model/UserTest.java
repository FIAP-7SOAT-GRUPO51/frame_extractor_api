package fiap.grupo51.fase5.frame_extractor_api.domain.model;

import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    @InjectMocks
    private User user;

    @Mock
    private UsersGroup usersGroup;

    @Mock
    private Permission permission;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .cpf("12345678901")
                .login("testuser")
                .password("password")
                .active(1)
                .usersGroups(List.of(usersGroup))
                .build();
    }

    @Test
    void testGenerateAccessKey() {
        user.generateAccessKey();
        assertNotNull(user.getAccessKey());
    }

    @Test
    void testGenerateAccessKeyWhenAlreadySet() {
        user.setAccessKey("existingKey");
        user.generateAccessKey();
        assertEquals("existingKey", user.getAccessKey());
    }

    @Test
    void testEncryptPassword() {
        String rawPassword = user.getPassword();
        user.encryptPassWord();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(rawPassword, user.getPassword()));
    }

    @Test
    void testPrePersist() {
        user.setAccessKey(null);
        user.prePersist();
        assertNotNull(user.getAccessKey());
    }
}