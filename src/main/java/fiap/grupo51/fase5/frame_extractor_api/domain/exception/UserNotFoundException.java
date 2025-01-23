package fiap.grupo51.fase5.frame_extractor_api.domain.exception;

public class UserNotFoundException extends DomainException {

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        this(String.format("Não existe um cadastro de usuário com código %d", id));
    }
}