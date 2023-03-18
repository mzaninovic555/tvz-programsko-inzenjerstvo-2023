package hr.tvz.pios.modul.login;

import hr.tvz.pios.common.Message;

/**
 * Rekord koji sadrži odgovor prilikom prijave.
 *
 * @param token JWT token.
 * @param message Opcionalna error poruka.
 */
public record LoginResponse(String token, Message message) {}
