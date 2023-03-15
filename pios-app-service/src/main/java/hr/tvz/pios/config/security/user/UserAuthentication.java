package hr.tvz.pios.config.security.user;

import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserAuthentication extends AbstractAuthenticationToken {

  private final ApplicationUser principal;

  public UserAuthentication(ApplicationUser principal) {
    super(principal.getAuthorities());
    this.principal = principal;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return "NO";
  }

  @Override
  public ApplicationUser getPrincipal() {
    return principal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    UserAuthentication that = (UserAuthentication) o;
    return Objects.equals(principal, that.principal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), principal);
  }
}
