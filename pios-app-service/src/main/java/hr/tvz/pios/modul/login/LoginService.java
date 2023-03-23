package hr.tvz.pios.modul.login;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servis za provodbu logiranja u aplikaciju.
 */
@Service
public class LoginService {

  final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Autowired JwtService jwtService;
  @Autowired UserRepository userRepository;

  /**
   * Provodi akciju logiranja u aplikaciju.
   *
   * @param username Korisničko ime.
   * @param password Lozinka.
   * @return @{@link LoginResponse} koji sadrži ili error poruku ili JWT token.
   */
  public ResponseEntity<LoginResponse> login(String username, String password) {
    Optional<User> userOptional = userRepository.getByUsername(username);
    if (userOptional.isEmpty() || !isMatchingPassword(password, userOptional.get().getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(null, Message.error("Invalid username/password")));
    }

    if (!userOptional.get().getIsActivated()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(null, Message.error("User isn't activated yet")));
    }

    String token = jwtService.createJwtToken(userOptional.get());
    return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token, null));
  }

  private Boolean isMatchingPassword(String loginPassword, String userPassword) {
    return encoder.matches(loginPassword, userPassword);
  }
}
