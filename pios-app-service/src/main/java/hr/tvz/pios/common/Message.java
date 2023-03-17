package hr.tvz.pios.common;

/**
 * Rekord za poruke koje se vraćaju u odgovorima.
 *
 * @param type Tip poruke.
 * @param content Sadržaj poruke.
 */
public record Message(MessageType type, String content) {
  public static Message info(String content) {
    return new Message(MessageType.INFO, content);
  }

  public static Message success(String content) {
    return new Message(MessageType.SUCCESS, content);
  }

  public static Message warn(String content) {
    return new Message(MessageType.WARN, content);
  }

  public static Message error(String content) {
    return new Message(MessageType.ERROR, content);
  }
}
