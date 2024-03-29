package hr.tvz.pios.modul.user.register;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Record koji predstavlja HTTP request za registraciju korisnika.
 * @param email {@link String}
 * @param username {@link String}
 * @param password {@link String}
 * @param description {@link String}
 */
public record RegisterRequest(
    @NotEmpty @Size(max = 100) String email,
    @NotEmpty @Size(min = 3, max = 20) String username,
    @NotEmpty @Size(min = 8, max = 100) String password,
    @Size(max = 200) String description
) { }
