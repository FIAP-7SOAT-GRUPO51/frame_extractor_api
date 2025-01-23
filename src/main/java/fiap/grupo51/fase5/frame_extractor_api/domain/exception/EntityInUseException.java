package fiap.grupo51.fase5.frame_extractor_api.domain.exception;

public class EntityInUseException extends DomainException {

    private static final long serialVersionUID = 1L;

    public EntityInUseException(String message) {
        super(message);
    }

}