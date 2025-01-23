package fiap.grupo51.fase5.frame_extractor_api.domain.service;

import fiap.grupo51.fase5.frame_extractor_api.domain.exception.UserNotFoundException;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import fiap.grupo51.fase5.frame_extractor_api.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userAppRepository;

    public UserService(UserRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    public User findOrElseByEmail(String email) {
        return userAppRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public User findOrElseByLogin(String login) {
        return userAppRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public User findByAccessKey(String accessKey) {
        return userAppRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new UserNotFoundException(accessKey));
    }
}
