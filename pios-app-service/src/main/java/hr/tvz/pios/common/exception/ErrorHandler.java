package hr.tvz.pios.common.exception;

import hr.tvz.pios.common.Message;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Error handler.
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<BasicResponse> handleAccessDeniedException(
      AccessDeniedException exception) {
    LOGGER.warn("AccessDeniedException", exception);
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new BasicResponse(null, Message.error("Forbidden")));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<BasicResponse> handleAuthenticationException(
      AuthenticationException exception) {
    LOGGER.warn("AuthenticationException", exception);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new BasicResponse(Message.error("Unauthorized")));
  }

  @ExceptionHandler(PiosException.class)
  public ResponseEntity<BasicResponse> handlePiosException(PiosException exception) {
    LOGGER.warn("{}", exception.toString());
    return new ResponseEntity<>(new BasicResponse(exception.getMessages()), exception.getStatus());
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<BasicResponse> handleGenericException(Throwable exception) {
    LOGGER.error("Neočekivana greška", exception);
    return ResponseEntity.internalServerError()
        .body(new BasicResponse(Message.error("Internal server error")));
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    LOGGER.warn("handleHttpMessageNotReadable", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BasicResponse(ex.getMessage()));
  }

  /** Metoda za handling MethodArgumentNotValidException-a. */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    LOGGER.debug("MethodArgumentNotValidException", ex);
    final List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    if (errors.isEmpty()) {
      return ResponseEntity.badRequest().body(new BasicResponse("Bad request"));
    }

    final List<Message> poruke = new ArrayList<>();
    errors.forEach(error -> poruke.add(Message.error(error.getDefaultMessage(), error.getField())));
    return ResponseEntity.badRequest().body(new BasicResponse(poruke));
  }
}
