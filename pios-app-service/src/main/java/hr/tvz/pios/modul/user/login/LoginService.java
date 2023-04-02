package hr.tvz.pios.modul.user.login;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.config.security.user.UserAuthentication;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servis za provodbu logiranja u aplikaciju.
 */
@Service
public class LoginService {

  final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Autowired
  JwtService jwtService;
  @Autowired
  UserRepository userRepository;

  /**
   * Provodi akciju logiranja u aplikaciju.
   *
   * @param username Korisničko ime.
   * @param password Lozinka.
   * @return @{@link LoginResponse} koji sadrži ili error poruku ili JWT token.
   */
  public LoginResponse login(String username, String password) {
    Optional<User> userOptional = userRepository.getByUsername(username);
    if (userOptional.isEmpty() || !isMatchingPassword(password, userOptional.get().getPassword())) {
      throw PiosException.badRequest(Message.error("Invalid username/password"));
    }

    if (!userOptional.get().getIsActivated()) {
      throw PiosException.badRequest(Message.error("User isn't activated yet"));
    }

    if (!userOptional.get().getIsActive()) {
      throw PiosException.badRequest(Message.info("User has been deactivated"));
    }

    String token = jwtService.createJwtToken(userOptional.get());
    return new LoginResponse(token);
  }

  /**
   * Provjerava podudaraju li se lozinke iz unosa forme i enkriptirana lozinka iz baze.
   * @param loginPassword lozinka iz forme
   * @param userPassword enkriptirana lozinka iz baze
   * @return TRUE - podudaraju se
   */
  private Boolean isMatchingPassword(String loginPassword, String userPassword) {
    return encoder.matches(loginPassword, userPassword);
  }

  public BasicResponse validateToken(UserAuthentication auth) {
    if (userRepository.getByUsername(auth.getUsername()).isPresent()) {
      return new BasicResponse();
    }
    throw PiosException.badRequest(Message.error("User does not exist"));
  }

  /**
   * Validira lozinku iz forme i lozinku računa logiranog korisnika.
   * @param auth logirani korisnik
   * @param passwordToValidate lozinka iz forme
   * @return {@link BasicResponse}
   */
  public BasicResponse validatePassword(UserAuthentication auth, String passwordToValidate) {
    Optional<User> userOptional = userRepository.getByUsername(auth.getUsername());
    if (userOptional.isEmpty()) {
      throw PiosException.badRequest(Message.error("User doesn't exist"));
    }

    Boolean isPasswordMatch = isMatchingPassword(passwordToValidate, userOptional.get().getPassword());
    if (isPasswordMatch) {
      return new BasicResponse();
    }
    throw PiosException.badRequest(Message.error("Passwords don't match"));
  }

  /**
   * Deaktivira traženi korisnički račun.
   * @param auth logirani korisnik
   * @param username korisničko ime koje deaktiviramo
   * @return {@link BasicResponse}
   */
  public BasicResponse deactivateAccount(UserAuthentication auth, String username) {
    if (!auth.getUsername().equals(username)) {
      throw PiosException.badRequest(Message.error("Usernames don't match"));
    }

    Integer deactivated = userRepository.deactivateByUsername(username);
    if (deactivated == 0) {
      throw PiosException.notFound(Message.error("User not found"));
    }

    return new BasicResponse(Message.info("User successfully deactivated"));
  }
}
