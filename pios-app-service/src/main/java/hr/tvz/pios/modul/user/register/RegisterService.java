package hr.tvz.pios.modul.user.register;

import static hr.tvz.pios.modul.user.settings.UserSettingsService.EMAIL_REGEX;
import hr.tvz.pios.common.AccountType;
import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.PiosProperties;
import hr.tvz.pios.mail.EmailService;
import hr.tvz.pios.modul.role.Role;
import hr.tvz.pios.modul.role.RoleRepository;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Autowired
  PiosProperties piosProperties;
  @Autowired
  UserRepository userRepository;
  @Autowired
  RoleRepository roleRepository;
  @Autowired
  EmailService emailService;

  /**
   * Vrši registraciju korisnika uz provjeru o postojećem email i username.
   *
   * @param request {@link RegisterRequest}
   * @return {@link RegisterResponse}
   */
  public RegisterResponse register(RegisterRequest request) {
    if (userRepository.isUsernameTaken(request.username())) {
      throw PiosException.conflict(Message.error("Username is already in use", "username"));
    }

    if (!request.email().matches(EMAIL_REGEX)) {
      throw PiosException.badRequest(Message.error("Invalid email format", "email"));
    }

    if (userRepository.isEmailTaken(request.email())) {
      throw PiosException.conflict(Message.error("Email is already in use", "email"));
    }
    Role role = roleRepository.getByName(hr.tvz.pios.common.Role.ROLE_USER.name()).get();
    User newUser =  User.builder()
        .username(request.username())
        .password(encoder.encode(request.password()))
        .email(request.email())
        .isActivated(Boolean.FALSE)
        .creationDate(LocalDateTime.now())
        .role(role)
        .accountType(AccountType.LOCAL)
        .build();
    userRepository.insert(newUser);

    String activationToken = generateActivationToken(newUser);
    String activationURL = piosProperties.frontendUrl() + "/activate?" + activationToken;
    emailService.generirajActivationEmail(newUser, activationURL);

    return new RegisterResponse(Message.info(ActivationResult.ACTIVATION_REQUIRED.getType()));
  }

  /**
   * Aktivira registriranog korisnika po username i timestampu kreiranja.
   *
   * @param request {@link ActivateRequest}
   * @return {@link RegisterResponse}
   */
  public RegisterResponse activateUser(ActivateRequest request) {
    var pairUsernameTimestamp = decodeUsernameTimestamp(request.activationToken());
    Optional<User> userToActivate = userRepository.getByUsername(pairUsernameTimestamp.getLeft());

    if (userToActivate.isPresent() && userToActivate.get().getIsActivated()) {
      throw PiosException.badRequest(Message.error(ActivationResult.ALREADY_ACTIVATED.getType()));
    }

    if (userToActivate.isPresent()
        && userToActivate.get().getCreationDate().toLocalDate()
              .equals(pairUsernameTimestamp.getRight().toLocalDate())) {
      userRepository.updateIsActivatedById(userToActivate.get().getId(), Boolean.TRUE);
      return new RegisterResponse(Message.info(ActivationResult.ACTIVATION_SUCCESS.getType()));
    }
    return new RegisterResponse(Message.error(ActivationResult.ACTIVATION_ERROR.getType()));
  }

  /**
   * Dekodira token u par String LocalDateTime.
   *
   * @param activationToken {@link String}
   * @return par username timestamp
   */
  private Pair<String, LocalDateTime> decodeUsernameTimestamp(String activationToken) {
    byte[] decodedBytes =
        Base64.getDecoder().decode(activationToken.getBytes(StandardCharsets.UTF_8));
    String decodedToken = new String(decodedBytes);
    String[] usernameTimestamp = StringUtils.splitByWholeSeparator(decodedToken, "_");
    return Pair.of(usernameTimestamp[0], LocalDateTime.parse(usernameTimestamp[1]));
  }

  /**
   * Generira aktivacijski URL za registraciju. <code>Username_timestamp </code> base64 enkodirano.
   *
   * @return generirani link
   */
  private String generateActivationToken(User user) {
    String usernameCreationDate = user.getUsername() + "_" + user.getCreationDate();
    return Base64.getEncoder()
        .encodeToString(usernameCreationDate.getBytes(StandardCharsets.UTF_8));
  }
}
