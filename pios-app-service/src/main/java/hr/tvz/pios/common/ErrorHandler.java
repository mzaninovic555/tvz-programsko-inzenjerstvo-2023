package hr.tvz.pios.common;

import hr.tvz.pios.common.ErrorResponse.ErrorField;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Error handler. */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    LOGGER.warn("handleHttpMessageNotReadable", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
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
      return ResponseEntity.badRequest().body(new ErrorResponse("Bad request"));
    }

    final List<ErrorResponse.ErrorField> poruke = new ArrayList<>();
    errors.forEach(
        error -> poruke.add(new ErrorField(error.getField(), error.getDefaultMessage())));
    return ResponseEntity.badRequest()
        .body(new ErrorResponse("Bad request", poruke.toArray(new ErrorField[0])));
  }
}
