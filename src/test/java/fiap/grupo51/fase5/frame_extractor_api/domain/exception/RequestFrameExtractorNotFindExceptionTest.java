package fiap.grupo51.fase5.frame_extractor_api.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestFrameExtractorNotFindExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Request frame extractor not found";
        RequestFrameExtractorNotFindException exception = new RequestFrameExtractorNotFindException(message);
        assertEquals(message, exception.getMessage());
    }
}