package fiap.grupo51.fase5.frame_extractor_api.domain.repository;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestFrameExtractorRepository extends JpaRepository<RequestFrameExtractor, Long> {

	Optional<RequestFrameExtractor> findByAccessKey(String accessKey);
	Optional<RequestFrameExtractor> findByFileName(String fileName);
	List<RequestFrameExtractor> findByStatus(RequestFrameExtractorStatus status);

	@Query("SELECT r.fileName FROM request_frame_extractor r WHERE r.fileName LIKE %:fileName%")
	List<String> findByFileNameContaining(@Param("fileName") String fileName);
}