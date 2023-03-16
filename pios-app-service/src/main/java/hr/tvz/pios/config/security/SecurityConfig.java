package hr.tvz.pios.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.pios.common.ErrorResponse;
import hr.tvz.pios.config.security.jwt.PiosAuthConverter;
import hr.tvz.pios.config.security.jwt.PiosJwtDecoder;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguracijska klasa za Spring Security.
 */
@Configuration
public class SecurityConfig {
  private final PiosJwtDecoder piosJwtDecoder;
  private final PiosAuthConverter piosAuthConverter;
  private final ObjectMapper mapper = new ObjectMapper();

  public SecurityConfig(PiosJwtDecoder piosJwtDecoder, PiosAuthConverter piosAuthConverter) {
    this.piosJwtDecoder = piosJwtDecoder;
    this.piosAuthConverter = piosAuthConverter;
  }

  // ovdje dodati URL koje ne treba autentificirati
  public static final String[] UNAUTHENTICATED_URLS = {
      "/api/v1/login",
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        authorize ->
            authorize
                .requestMatchers(UNAUTHENTICATED_URLS)
                .permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .anyRequest()
                .authenticated());

    http.oauth2ResourceServer()
        .jwt()
        .decoder(piosJwtDecoder)
        .jwtAuthenticationConverter(piosAuthConverter);

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.csrf().disable();
    http.headers().frameOptions().disable();
    http.cors();

    http.exceptionHandling()
        .accessDeniedHandler(
            (req, response, e) ->
                sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
        .authenticationEntryPoint(
            ((req, response, a) ->
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")));

    return http.build();
  }

  private void sendErrorResponse(HttpServletResponse response, int status, String message)
      throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(status);
    mapper.writeValue(response.getOutputStream(), new ErrorResponse(message));
  }
}
