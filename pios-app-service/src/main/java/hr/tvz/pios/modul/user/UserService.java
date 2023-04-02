package hr.tvz.pios.modul.user;

import hr.tvz.pios.common.AccountType;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.role.Role;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  public String processOAuthPostLogin(String username, String email) {
    Optional<User> existUser = userRepository.getByUsername(username);

    User finalUser = existUser.orElse(null);

    if (existUser.isPresent() && !existUser.get().isActive) {
      throw PiosException.badRequest();
    }

    if (existUser.isEmpty()) {
      User newUser = new User();
      newUser.setUsername(username);
      newUser.setEmail(email);
      newUser.setRole(new Role(2L, "ROLE_USER"));
      newUser.setIsActivated(true);
      newUser.setCreationDate(LocalDateTime.now());
      newUser.setAccountType(AccountType.GITHUB);

      userRepository.insert(newUser);

      finalUser = newUser;
    }

    return jwtService.createJwtToken(finalUser);
  }
}
