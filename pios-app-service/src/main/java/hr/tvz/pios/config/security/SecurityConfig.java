package hr.tvz.pios.config.security;

import hr.tvz.pios.config.security.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Konfiguracijska klasa za Spring Security.
 */
@Configuration
public class SecurityConfig {

  @Autowired
  JwtFilter jwtFilter;

  // ovdje dodati URL koje ne treba autentificirati
  public static final String[] UNAUTHENTICATED_URLS = {

  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers(UNAUTHENTICATED_URLS).permitAll()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        .anyRequest().authenticated()
    );

    http.csrf().disable();
    http.headers().frameOptions().disable();
    http.cors();

    http.exceptionHandling().authenticationEntryPoint(
        ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
    ).and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
