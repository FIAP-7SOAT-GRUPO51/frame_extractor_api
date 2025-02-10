package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import fiap.grupo51.fase5.frame_extractor_api.domain.exception.UserNotFoundException;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindOrElseByEmail_UserFound() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.findOrElseByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testFindOrElseByEmail_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findOrElseByEmail("unknown@example.com"));
    }

    @Test
    void testFindOrElseByLogin_UserFound() {
        User user = new User();
        user.setLogin("testuser");
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        User result = userService.findOrElseByLogin("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getLogin());
    }

    @Test
    void testFindOrElseByLogin_UserNotFound() {
        when(userRepository.findByLogin("unknownuser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findOrElseByLogin("unknownuser"));
    }

    @Test
    void testFindByAccessKey_UserFound() {
        User user = new User();
        user.setAccessKey("accessKey");
        when(userRepository.findByAccessKey("accessKey")).thenReturn(Optional.of(user));

        User result = userService.findByAccessKey("accessKey");

        assertNotNull(result);
        assertEquals("accessKey", result.getAccessKey());
    }

    @Test
    void testFindByAccessKey_UserNotFound() {
        when(userRepository.findByAccessKey("unknownKey")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByAccessKey("unknownKey"));
    }
}