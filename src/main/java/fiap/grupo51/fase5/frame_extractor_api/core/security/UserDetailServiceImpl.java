package fiap.grupo51.fase5.frame_extractor_api.core.security;

import fiap.grupo51.fase5.frame_extractor_api.domain.exception.UserNotFoundException;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.UserRepository;
import fiap.grupo51.fase5.frame_extractor_api.utils.MessageProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @MessageProperty("user.not-found")
    private String userNotFound;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByLogin(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UserNotFoundException(userNotFound));
    }
}
