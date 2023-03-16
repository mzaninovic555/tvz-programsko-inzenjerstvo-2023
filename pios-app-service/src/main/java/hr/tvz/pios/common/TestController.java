package hr.tvz.pios.common;

import hr.tvz.pios.config.security.user.UserAuthentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Testni kontroler za provjeru security-a.
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ErrorResponse getAdmin(UserAuthentication auth) {
    return new ErrorResponse("security works!");
  }

  @GetMapping("/user")
  @PreAuthorize("hasAnyRole('USER')")
  public ErrorResponse getUser(UserAuthentication auth) {
    return new ErrorResponse("security works!");
  }
}
