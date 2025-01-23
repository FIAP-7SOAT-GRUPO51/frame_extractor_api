package fiap.grupo51.fase5.frame_extractor_api.domain.repository;


import fiap.grupo51.fase5.frame_extractor_api.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	Optional<User> findByAccessKey(String accessKey);
	Optional<User> findByLogin(String login);
}