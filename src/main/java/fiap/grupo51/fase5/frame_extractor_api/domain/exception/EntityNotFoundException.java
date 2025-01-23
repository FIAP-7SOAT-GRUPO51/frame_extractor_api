package fiap.grupo51.fase5.frame_extractor_api.domain.exception;

import jakarta.persistence.PersistenceException;

public class EntityNotFoundException extends PersistenceException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(Exception cause) {
        super(cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}