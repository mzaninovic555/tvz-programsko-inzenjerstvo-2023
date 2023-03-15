package hr.tvz.pios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class PIOSApplication {
	public static void main(String[] args) {
		SpringApplication.run(PIOSApplication.class, args);
	}
}
