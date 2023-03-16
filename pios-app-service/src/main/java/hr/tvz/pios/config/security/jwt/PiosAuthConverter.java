package hr.tvz.pios.config.security.jwt;

import hr.tvz.pios.config.security.user.UserAuthentication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

/**
 * Klasa za pretvorbu JWT tokena u autenticiranog korisnika.
 */
@Component
public class PiosAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  @Override
  public AbstractAuthenticationToken convert(Jwt source) {
    List<String> errors = new ArrayList<>();

    String username = source.getClaimAsString("sub");
    if (StringUtils.isEmptyOrWhitespace(username)) {
      errors.add("username is missing");
    }

    String roles = source.getClaimAsString("roles");
    if (StringUtils.isEmptyOrWhitespace(roles)) {
      errors.add("authorities is missing");
    }

    if (!errors.isEmpty()) {
      throw new JwtValidationException(
          "Invalid token data",
          List.of(
              new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, String.join(", ", errors), null)));
    }

    Set<SimpleGrantedAuthority> rolesMapped =
        Arrays.stream(roles.split(", "))
            .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
            .collect(Collectors.toSet());

    UserAuthentication auth = new UserAuthentication(rolesMapped, username);
    auth.setAuthenticated(true);
    return auth;
  }
}
