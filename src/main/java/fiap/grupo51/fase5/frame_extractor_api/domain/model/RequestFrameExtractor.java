package fiap.grupo51.fase5.frame_extractor_api.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "request_frame_extractor")
@NoArgsConstructor
@AllArgsConstructor
public class RequestFrameExtractor extends Audit {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String description;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private int fps;

	@Enumerated(EnumType.STRING)
	private RequestFrameExtractorStatus status;

}