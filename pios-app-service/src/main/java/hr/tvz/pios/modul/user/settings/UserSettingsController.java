package hr.tvz.pios.modul.user.settings;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.config.security.user.UserAuthentication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler za korisničke postavke.
 */
@RestController
@RequestMapping("/api/v1")
public class UserSettingsController {
  private final UserSettingsService userSettingsService;

  public UserSettingsController(UserSettingsService userSettingsService) {
    this.userSettingsService = userSettingsService;
  }

  @GetMapping("/user-settings")
  public UserSettingsResponse getUserSettings(UserAuthentication auth) {
    return userSettingsService.getByUsername(auth.getUsername());
  }

  @PutMapping("/user-settings")
  public Message setUserSettings(
      UserAuthentication auth, @RequestBody @Valid UserSettingsRequest request) {
    return userSettingsService.setUserSettings(auth.getUsername(), request);
  }
}
