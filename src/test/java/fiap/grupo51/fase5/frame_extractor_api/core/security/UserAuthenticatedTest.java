package fiap.grupo51.fase5.frame_extractor_api.core.security;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAuthenticatedTest {

    private User user;
    private UserAuthenticated userAuthenticated;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        userAuthenticated = new UserAuthenticated(user);
    }

    @Test
    void testGetPassword() {
        when(user.getPassword()).thenReturn("password");

        assertEquals("password", userAuthenticated.getPassword());
    }

    @Test
    void testGetUsername() {
        when(user.getLogin()).thenReturn("username");

        assertEquals("username", userAuthenticated.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userAuthenticated.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userAuthenticated.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userAuthenticated.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userAuthenticated.isEnabled());
    }
}