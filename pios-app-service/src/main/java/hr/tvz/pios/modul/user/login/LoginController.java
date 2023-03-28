package hr.tvz.pios.modul.user.login;

import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.config.security.user.UserAuthentication;
import jakarta.validation.Valid;
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

  @GetMapping("/validate-token")
  public BasicResponse validateToken(UserAuthentication auth) {
    return loginService.validateToken(auth);
  }
}
