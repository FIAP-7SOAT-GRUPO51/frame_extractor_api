package fiap.grupo51.fase5.frame_extractor_api.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "User not found";
        UserNotFoundException exception = new UserNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithId() {
        Long id = 1L;
        UserNotFoundException exception = new UserNotFoundException(id);
        assertEquals("Não existe um cadastro de usuário com código " + id, exception.getMessage());
    }
}