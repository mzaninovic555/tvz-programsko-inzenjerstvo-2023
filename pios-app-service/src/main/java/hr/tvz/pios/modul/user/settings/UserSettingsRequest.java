package hr.tvz.pios.modul.user.settings;

import jakarta.validation.constraints.Size;

/**
 * Rekord za slanje novih korisničkih postavki.
 */
public record UserSettingsRequest(
    @Size(max = 100, min = 8) String oldPassword,
    @Size(max = 100, min = 8) String newPassword,
    @Size(max = 100, min = 8) String newPasswordRepeat,
    @Size(max = 100) String email,
    @Size(max = 200) String description) {}
