package hr.tvz.pios.config.security.user;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Setter
@Builder
public class ApplicationUser implements Serializable {

  private String username;
  private String password;
  private List<SimpleGrantedAuthority> authorities;
}
