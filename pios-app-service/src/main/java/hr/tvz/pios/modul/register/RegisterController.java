package hr.tvz.pios.modul.register;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RegisterController {

  @Autowired
  RegisterService registerService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
    return registerService.register(request);
  }
}