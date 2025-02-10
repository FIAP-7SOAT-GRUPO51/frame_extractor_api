package fiap.grupo51.fase5.frame_extractor_api.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntityNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        EntityNotFoundException exception = new EntityNotFoundException();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Entity not found";
        EntityNotFoundException exception = new EntityNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithCause() {
        Exception cause = new Exception("Cause");
        EntityNotFoundException exception = new EntityNotFoundException(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Entity not found";
        Exception cause = new Exception("Cause");
        EntityNotFoundException exception = new EntityNotFoundException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}