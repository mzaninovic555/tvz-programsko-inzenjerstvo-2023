package hr.tvz.pios.common;

/**
 * Rekord za poruke koje se vraćaju u odgovorima.
 *
 * @param type Tip poruke.
 * @param content Sadržaj poruke.
 * @param reference Referenca na određeni podatak.
 */
public record Message(MessageType type, String content, String reference) {
  public static Message info(String content) {
    return Message.info(content, null);
  }

  public static Message info(String content, String reference) {
    return new Message(MessageType.INFO, content, reference);
  }

  public static Message success(String content) {
    return Message.success(content, null);
  }

  public static Message success(String content, String reference) {
    return new Message(MessageType.SUCCESS, content, reference);
  }

  public static Message warn(String content) {
    return Message.warn(content, null);
  }

  public static Message warn(String content, String reference) {
    return new Message(MessageType.WARN, content, reference);
  }

  public static Message error(String content) {
    return Message.error(content, null);
  }

  public static Message error(String content, String reference) {
    return new Message(MessageType.ERROR, content, reference);
  }
}
