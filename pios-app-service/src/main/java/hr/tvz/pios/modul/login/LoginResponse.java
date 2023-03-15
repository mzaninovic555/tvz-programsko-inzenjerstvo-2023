package hr.tvz.pios.modul.login;

/**
 * Rekord koji sadrži odgovor prilikom prijave.
 *
 * @param token JWT token.
 */
public record LoginResponse(String token, String message) {}
