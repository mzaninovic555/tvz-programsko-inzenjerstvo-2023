package hr.tvz.pios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableMethodSecurity
public class PIOSApplication {
  public static void main(String[] args) {
    SpringApplication.run(PIOSApplication.class, args);
  }
}
