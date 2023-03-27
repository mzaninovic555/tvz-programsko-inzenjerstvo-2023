package hr.tvz.pios.modul.user.settings;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servis za korisničke postavke.
 */
@Service
public class UserSettingsService {
  public static final String EMAIL_REGEX =
      "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
          + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

  private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
  private final UserRepository userRepository;

  public UserSettingsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserSettingsResponse getByUsername(String username) {
    Optional<User> user = userRepository.getByUsername(username);
    if (user.isEmpty()) {
      throw PiosException.notFound(Message.error("User not found"));
    }
    return UserSettingsResponse.fromUser(user.get());
  }

  public Message setUserSettings(String username, UserSettingsRequest req) {
    Optional<User> user = userRepository.getByUsername(username);
    if (!user.isPresent()) {
      throw PiosException.badRequest(Message.error("User not found"));
    }

    User u = user.get();

    String newEmail = getNewEmail(u, req.email());
    String password =
        getNewPassword(u, req.oldPassword(), req.newPassword(), req.newPasswordRepeat());

    if (newEmail != null) {
      u.setEmail(newEmail);
    }
    if (password != null) {
      u.setPassword(password);
    }
    if (req.description() != null) {
      u.setDescription(req.description());
    }

    userRepository.updateById(u);
    return Message.success("User settings successfully updated");
  }

  private String getNewPassword(
      User user, String oldPassword, String newPassword, String newPasswordRepeat) {
    if (ObjectUtils.allNull(oldPassword, newPassword, newPasswordRepeat)) {
      return null;
    }

    List<Message> m = new ArrayList<>();
    if (oldPassword == null) {
      m.add(Message.error("Old password is required", "oldPassword"));
    }
    if (newPassword == null) {
      m.add(Message.error("Password is required", "newPassword"));
    }
    if (newPasswordRepeat == null) {
      m.add(Message.error("Repeated password is required", "newPasswordRepeat"));
    }
    if (!m.isEmpty()) {
      throw PiosException.badRequest(m);
    }

    if (!newPassword.equals(newPasswordRepeat)) {
      throw PiosException.badRequest(
          Message.error("Passwords must match", "newPassword"),
          Message.error("Passwords must match", "newPasswordRepeat"));
    }

    if (!bcryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
      throw PiosException.badRequest(Message.error("Incorrect password", "oldPassword"));
    }

    return bcryptPasswordEncoder.encode(newPassword);
  }

  private String getNewEmail(User user, String email) {
    if (email == null) {
      return null;
    }

    if (user.getEmail() != null && user.getEmail().equals(email)) {
      return null;
    }

    if (!email.matches(EMAIL_REGEX)) {
      throw PiosException.badRequest(Message.error("Invalid email format", "email"));
    }

    if (userRepository.isEmailTaken(email)) {
      throw PiosException.badRequest(
          Message.error("This email address is already in use", "email"));
    }

    return email;
  }
}
