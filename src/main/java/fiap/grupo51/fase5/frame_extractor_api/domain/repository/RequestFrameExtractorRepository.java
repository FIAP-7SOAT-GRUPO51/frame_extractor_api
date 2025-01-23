package fiap.grupo51.fase5.frame_extractor_api.domain.repository;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestFrameExtractorRepository extends JpaRepository<RequestFrameExtractor, Long> {

	Optional<RequestFrameExtractor> findByAccessKey(String accessKey);
}