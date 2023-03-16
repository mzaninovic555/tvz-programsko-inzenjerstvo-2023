package hr.tvz.pios.modul.login;

/**
 * Rekord koji sadrži odgovor prilikom prijave.
 *
 * @param token JWT token.
 * @param message Opcionalna error poruka.
 */
public record LoginResponse(String token, String message) {}
