package fiap.grupo51.fase5.frame_extractor_api.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityInUseExceptionTest {

    @Test
    void testEntityInUseExceptionMessage() {
        String message = "Entity is in use";
        EntityInUseException exception = new EntityInUseException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testEntityInUseExceptionIsInstanceOfDomainException() {
        EntityInUseException exception = new EntityInUseException("Entity is in use");

        assertTrue(exception instanceof DomainException);
    }
}