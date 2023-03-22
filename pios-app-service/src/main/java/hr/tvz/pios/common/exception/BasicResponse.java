package hr.tvz.pios.common.exception;

import hr.tvz.pios.common.Message;
import java.util.Arrays;
import java.util.List;

/**
 * Odgovor za slučajeve kada se desi iznimka.
 *
 * @param messages Poruke.
 */
public record BasicResponse(List<Message> messages) {
  public BasicResponse(Message... messages) {
    this(Arrays.asList(messages));
  }

  public BasicResponse(String message) {
    this(List.of(Message.info(message)));
  }
}
