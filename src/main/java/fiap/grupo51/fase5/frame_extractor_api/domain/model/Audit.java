package fiap.grupo51.fase5.frame_extractor_api.domain.model;

import fiap.grupo51.fase5.frame_extractor_api.utils.FrameExtractorUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class Audit {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_insert")
	private User userInsert;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dateInsert;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_update")
	private User userUpdate;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dateUpdate;

	@Column(name = "access_key")
	private String accessKey;

	@PrePersist
	private void prePersist() {
		generateAccessKey();
	}

	public void generateAccessKey() {

		if (this.accessKey == null)
			setAccessKey(FrameExtractorUtils.getNewAccessKey());
	}

}