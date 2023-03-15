package hr.tvz.pios.modul.login;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Rekord za podatke koji će se slati prilikom prijave.
 *
 * @param username Korisničko ime.
 * @param password Lozinka.
 */
public record LoginRequest(
    @NotEmpty @Size(max = 20, min = 3) String username,
    @NotEmpty @Size(max = 100, min = 8) String password) {}
