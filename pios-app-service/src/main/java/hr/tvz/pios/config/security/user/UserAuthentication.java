package hr.tvz.pios.config.security.user;

import java.util.Collection;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Klasa koja predstavlja autenticiranog korisnika.
 */
public class UserAuthentication extends AbstractAuthenticationToken {
  private final String username;

  public UserAuthentication(Collection<? extends GrantedAuthority> authorities, String username) {
    super(authorities);
    this.username = username;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public String getPrincipal() {
    return username;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    UserAuthentication that = (UserAuthentication) o;

    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (username != null ? username.hashCode() : 0);
    return result;
  }
}
