package fiap.grupo51.fase5.frame_extractor_api.domain.repository;

import fiap.grupo51.fase5.frame_extractor_api.domain.model.Bucket;
import fiap.grupo51.fase5.frame_extractor_api.domain.model.RequestFrameExtractor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketFileRepository extends JpaRepository<Bucket, Long> {

	Optional<Bucket> findByFileName(String fileName);


}