package hr.tvz.pios.modul.user.register;

import hr.tvz.pios.common.Message;

/**
 * Record koji predstavlja HTTP response kod registracije.
 * @param message opcionalna poruka, {@link Message}
 */
public record RegisterResponse(Message message) { }
