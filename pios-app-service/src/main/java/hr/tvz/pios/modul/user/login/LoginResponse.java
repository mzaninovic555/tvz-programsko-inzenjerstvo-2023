package hr.tvz.pios.modul.user.login;

/**
 * Rekord koji sadrži odgovor (token) prilikom prijave.
 *
 * @param token JWT token.
 */
public record LoginResponse(String token) {}
