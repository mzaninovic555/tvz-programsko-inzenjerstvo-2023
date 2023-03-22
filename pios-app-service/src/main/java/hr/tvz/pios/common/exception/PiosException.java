package hr.tvz.pios.common.exception;

import hr.tvz.pios.common.Message;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 * Iznimka za pogreške u aplikaciji.
 */
public class PiosException extends RuntimeException {
  private final HttpStatus status;
  private final List<Message> message;

  public PiosException(HttpStatus status, List<Message> message) {
    this.status = status;
    this.message = message;
  }

  public PiosException(HttpStatus status, Message... poruke) {
    this(status, Arrays.asList(poruke));
  }

  public static PiosException badRequest(List<Message> poruke) {
    return new PiosException(HttpStatus.BAD_REQUEST, poruke);
  }

  public static PiosException badRequest(Message... poruke) {
    return badRequest(Arrays.asList(poruke));
  }

  public static PiosException notFound(List<Message> poruke) {
    return new PiosException(HttpStatus.NOT_FOUND, poruke);
  }

  public static PiosException notFound(Message... poruke) {
    return notFound(Arrays.asList(poruke));
  }

  public static PiosException conflict(List<Message> poruke) {
    return new PiosException(HttpStatus.CONFLICT, poruke);
  }

  public static PiosException conflict(Message... poruke) {
    return conflict(Arrays.asList(poruke));
  }

  public HttpStatus getStatus() {
    return status;
  }

  public List<Message> getMessages() {
    return message;
  }

  @Override
  public String toString() {
    return "PiosException{" + "status=" + status + ", message=" + message + '}';
  }
}
