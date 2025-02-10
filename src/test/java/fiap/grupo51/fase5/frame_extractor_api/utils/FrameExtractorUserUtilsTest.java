package fiap.grupo51.fase5.frame_extractor_api.utils;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FrameExtractorUserUtilsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FrameExtractorUserUtils frameExtractorUserUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser_UserFound() {
        User user = new User();
        user.setLogin("testuser");
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        User result = frameExtractorUserUtils.getUser(authentication);

        assertNotNull(result);
        assertEquals("testuser", result.getLogin());
    }

    @Test
    void testGetUser_UserNotFound() {
        when(authentication.getName()).thenReturn("unknownuser");
        when(userRepository.findByLogin("unknownuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> frameExtractorUserUtils.getUser(authentication));
    }
}