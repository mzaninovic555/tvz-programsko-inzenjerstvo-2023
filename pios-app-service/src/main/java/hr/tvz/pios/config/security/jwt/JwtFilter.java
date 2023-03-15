package hr.tvz.pios.config.security.jwt;

import hr.tvz.pios.config.security.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter za provjeru i validaciju JWT tokena. Izvršava se jednom po svakom requestu.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

  private final JwtService jwtService;

  public JwtFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    request.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    if (!isEndpointUnauthenticated(request)) {
      String jwtToken = extractJwt(request);
      LOGGER.trace("Filtering for endpoint: {}, resolved JWT: {}", request.getRequestURI(), jwtToken);
      if (jwtToken != null && !jwtToken.isEmpty()) {
        Boolean authenticate = jwtService.authenticate(jwtToken);
        if (!authenticate) {
          unauthorized(response);
        }
      } else {
        unauthorized(response);
      }
    }
    filterChain.doFilter(request, response);
  }

  private void unauthorized(HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  private String extractJwt(HttpServletRequest request) {
    String token = request.getHeader(AUTHORIZATION_HEADER);
    if (token != null && token.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
      return token.substring(AUTHORIZATION_TOKEN_PREFIX.length());
    }
    return null;
  }

  private Boolean isEndpointUnauthenticated(HttpServletRequest request) {
    String uri = request.getRequestURI();
    return Stream.of(SecurityConfig.UNAUTHENTICATED_URLS).anyMatch(uri::contains);
  }
}
