package hr.tvz.pios.modul.user.login;

import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.config.security.user.UserAuthentication;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Kontroler za prijavu. */
@RestController
@RequestMapping("/api/v1")
public class LoginController {
  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody @Valid LoginRequest data) {
    return loginService.login(data.username(), data.password());
  }

  @PostMapping("/validate-password")
  public BasicResponse validatePassword(@RequestBody Map<String, String> password, UserAuthentication auth) {
    return loginService.validatePassword(auth, password.get("password"));
  }

  @GetMapping("/validate-token")
  public BasicResponse validateToken(UserAuthentication auth) {
    return loginService.validateToken(auth);
  }

  @PostMapping("/deactivate-account")
  public BasicResponse deactivateAccount(@RequestBody Map<String, String> username, UserAuthentication auth) {
    return loginService.deactivateAccount(auth, username.get("username"));
  }
}
