package fiap.grupo51.fase5.frame_extractor_api.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.DomainException;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.EntityNotFoundException;
import fiap.grupo51.fase5.frame_extractor_api.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ApiExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    private ApiExceptionHandler apiExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        apiExceptionHandler = new ApiExceptionHandler();
        apiExceptionHandler.messageSource = messageSource;
    }

    @Test
    void handleAccessDeniedException_shouldReturnForbiddenStatus() {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<?> response = apiExceptionHandler.handleAccessDeniedException(exception, request);

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
        UserNotFoundException exception = new UserNotFoundException("User not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<?> response = apiExceptionHandler.handleUserNotFoundException(exception, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(body.getDetail()).isEqualTo("User not found");
    }

    @Test
    void handleEntityNotFoundException_shouldReturnNotFoundStatus() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<?> response = apiExceptionHandler.handleEntidadeNaoEncontrada(exception, request);

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
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Integrity violation");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<?> response = apiExceptionHandler.handleDataIntegrityViolationException(exception, request);

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
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = apiExceptionHandler.handleHttpMessageNotReadable(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

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
        Exception exception = new Exception("Generic error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = apiExceptionHandler.handleUncaught(exception, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(body.getTitle()).isEqualTo("Erro de sistema");
        assertThat(body.getUserMessage()).isEqualTo(ApiExceptionHandler.MSG_ERRO_GENERICA_USUARIO_FINAL);
    }

    @Test
    void handleMethodArgumentNotValid_shouldReturnBadRequestStatus() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(new FieldError("objectName", "field", "defaultMessage")));
        when(messageSource.getMessage(any(), any())).thenReturn("defaultMessage");

        WebRequest request = mock(WebRequest.class);
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> response = apiExceptionHandler.handleMethodArgumentNotValid(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Dados inválidos");
        assertThat(body.getDetail()).isEqualTo("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
    }

    @Test
    void handleNoHandlerFoundException_shouldReturnNotFoundStatus() {
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/path", new HttpHeaders());
        WebRequest request = mock(WebRequest.class);
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> response = apiExceptionHandler.handleNoHandlerFoundException(exception, headers, HttpStatus.NOT_FOUND, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(body.getTitle()).isEqualTo("Recurso não encontrado");
        assertThat(body.getDetail()).isEqualTo("O recurso /path, que você tentou acessar, é inexistente.");
    }

    @Test
    void handleTypeMismatch_shouldReturnBadRequestStatus() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException("value", String.class, "name", null, new IllegalArgumentException());
        WebRequest request = mock(WebRequest.class);
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> response = apiExceptionHandler.handleTypeMismatch(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Parâmetro inválido");
        assertThat(body.getDetail()).isEqualTo("O parâmetro de URL 'name' recebeu o valor 'value', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo String.");
    }

    @Test
    void handleInvalidFormat_shouldReturnBadRequestStatus() {
        InvalidFormatException exception = mock(InvalidFormatException.class);
        when(exception.getPath()).thenReturn(Collections.emptyList());
        when(exception.getValue()).thenReturn("value");
        when(exception.getTargetType()).thenReturn((Class) String.class);
        WebRequest request = mock(WebRequest.class);
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> response = apiExceptionHandler.handleInvalidFormat(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Mensagem incompreensível");
        assertThat(body.getDetail()).isEqualTo("A propriedade '' recebeu o valor 'value', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo String.");
    }

    @Test
    void handlePropertyBinding_shouldReturnBadRequestStatus() {
        PropertyBindingException exception = mock(PropertyBindingException.class);
        when(exception.getPath()).thenReturn(Collections.emptyList());
        WebRequest request = mock(WebRequest.class);
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> response = apiExceptionHandler.handlePropertyBinding(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Mensagem incompreensível");
        assertThat(body.getDetail()).isEqualTo("A propriedade '' não existe. Corrija ou remova essa propriedade e tente novamente.");
    }

    @Test
    void handleDomainException_shouldReturnBadRequestStatus() {
        DomainException exception = new DomainException("Domain error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<?> response = apiExceptionHandler.handleDomain(exception, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getTitle()).isEqualTo("Violação de regra de negócio");
        assertThat(body.getDetail()).isEqualTo("Domain error");
    }

    @Test
    void handleInternalAuthenticationServiceException_shouldReturnForbiddenStatus() {
        InternalAuthenticationServiceException exception = new InternalAuthenticationServiceException("Authentication error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<?> response = apiExceptionHandler.handleInternalAuthenticationServiceException(exception, request);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Problem body = (Problem) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(body.getTitle()).isEqualTo("Acesso negado");
        assertThat(body.getDetail()).isEqualTo("Authentication error");
    }
}