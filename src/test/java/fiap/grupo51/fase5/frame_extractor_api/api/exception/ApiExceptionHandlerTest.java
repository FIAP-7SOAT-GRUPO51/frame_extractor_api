package fiap.grupo51.fase5.frame_extractor_api.api.exception;

import fiap.grupo51.fase5.frame_extractor_api.domain.exception.EntityNotFoundException;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiExceptionHandlerTest {

    private final MessageSource messageSource = mock(MessageSource.class);
    private final ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();

    @Test
    void handleAccessDeniedException_shouldReturnForbiddenStatus() {
        // Arrange
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<?> response = apiExceptionHandler.handleAccessDeniedException(exception, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(body.getTitle()).isEqualTo("Acesso negado");
        assertThat(body.getUserMessage()).isEqualTo("Você não possui permissão para executar essa operação.");
    }

    @Test
    void handleUserNotFoundException_shouldReturnForbiddenStatus() {
        // Arrange
        UserNotFoundException exception = new UserNotFoundException("User not found");
        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<?> response = apiExceptionHandler.handleUserNotFoundException(exception, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(body.getDetail()).isEqualTo("User not found");
    }

    @Test
    void handleEntityNotFoundException_shouldReturnNotFoundStatus() {
        // Arrange
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<?> response = apiExceptionHandler.handleEntidadeNaoEncontrada(exception, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(body.getTitle()).isEqualTo("Recurso não encontrado");
        assertThat(body.getDetail()).isEqualTo("Entity not found");
    }

    @Test
    void handleDataIntegrityViolationException_shouldReturnBadRequestStatus() {
        // Arrange
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Integrity violation");
        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<?> response = apiExceptionHandler.handleDataIntegrityViolationException(exception, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Dados inválidos");
        assertThat(body.getUserMessage()).isEqualTo("Falha de integridade de dados");
    }


    @Test
    void handleHttpMessageNotReadable_shouldReturnIncomprehensibleMessageError() {
        // Arrange
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<Object> response = apiExceptionHandler.handleHttpMessageNotReadable(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Mensagem incompreensível");
        assertThat(body.getDetail()).isEqualTo("O corpo da requisição está inválido. Verifique erro de sintaxe.");
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        // Arrange
        Exception exception = new Exception("Generic error");
        WebRequest request = mock(WebRequest.class);

        // Act
        ResponseEntity<Object> response = apiExceptionHandler.handleUncaught(exception, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(body.getTitle()).isEqualTo("Erro de sistema");
        assertThat(body.getUserMessage()).isEqualTo(ApiExceptionHandler.MSG_ERRO_GENERICA_USUARIO_FINAL);
    }
}
