package hr.tvz.pios.modul.login;

import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired
  JwtService jwtService;
  @Autowired
  UserRepository userRepository;

  //TODO odluciti sto vraca metoda
  public void login(String username, String password) {
    Optional<User> userOptional = userRepository.getByUsername(username);
    if (userOptional.isEmpty() || !isMatchingPassword(password, userOptional.get().getPassword())) {
      return;
    }

    jwtService.createJwtToken(userOptional.get()); //stvoriti login s ovim, bumo vidli
  }

  private Boolean isMatchingPassword(String loginPassword, String userPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(loginPassword, userPassword);
  }
}
