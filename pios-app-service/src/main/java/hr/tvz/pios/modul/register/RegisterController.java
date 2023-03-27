package hr.tvz.pios.modul.register;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST kontroler za registraciju i aktivaciju korisnika.
 */
@RestController
@RequestMapping("/api/v1")
public class RegisterController {

  @Autowired
  RegisterService registerService;

  @PostMapping("/register")
  public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
    return registerService.register(request);
  }

  @PostMapping("/activate")
  public RegisterResponse activate(@RequestBody @Valid ActivateRequest request) {
    return registerService.activateUser(request);
  }
}
