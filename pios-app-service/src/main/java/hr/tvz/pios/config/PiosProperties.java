package hr.tvz.pios.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pios")
public class PiosProperties {

  Mail mail = new Mail();

  Jwt jwt = new Jwt();
  public static class Mail {
    private String from;

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }
  }

  //TODO jwt properties
  private static class Jwt {

  }

  public Mail getMail() {
    return mail;
  }

  public void setMail(Mail mail) {
    this.mail = mail;
  }

  public Jwt getJwt() {
    return jwt;
  }

  public void setJwt(Jwt jwt) {
    this.jwt = jwt;
  }
}
