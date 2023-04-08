package hr.tvz.pios.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties koji se koriste u ostatku aplikacije.
 */
@ConfigurationProperties(prefix = "pios")
public record PiosProperties(
    String frontendUrl,
    Boolean mailEnabled,
    Mail mail,
    Jwt jwt,
    String[] corsOrigins) {
  public record Mail(String from) {}

  public record Jwt(String secret, Long validitySeconds) {
  }
}
