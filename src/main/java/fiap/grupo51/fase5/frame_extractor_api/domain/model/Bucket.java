package fiap.grupo51.fase5.frame_extractor_api.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "bucket_file")
@NoArgsConstructor
@AllArgsConstructor
public class Bucket extends Audit {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String contentType;

	@Column(nullable = false)
	private long size;

	@Column(nullable = false)
	private String url;

	@Column
	private String description;
}