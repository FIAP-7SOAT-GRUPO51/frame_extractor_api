package fiap.grupo51.fase5.frame_extractor_api.core.security;

import fiap.grupo51.fase5.frame_extractor_api.domain.exception.UserNotFoundException;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        User user = new User();
        user.setLogin("testuser");
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

}