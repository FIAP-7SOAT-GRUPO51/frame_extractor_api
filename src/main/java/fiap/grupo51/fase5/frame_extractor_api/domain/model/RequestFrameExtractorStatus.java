package fiap.grupo51.fase5.frame_extractor_api.domain.model;

public enum RequestFrameExtractorStatus {

	EM_ABERTO("EM ABERTO"),
	EM_PROCESSAMENTO("EM PROCESSAMENTO"),
	FALHA("FALHA"),
	CONCLUIDO("CONCLUIDO");


	private final String status;

	RequestFrameExtractorStatus(String status) {
		this.status = status;
	}
}