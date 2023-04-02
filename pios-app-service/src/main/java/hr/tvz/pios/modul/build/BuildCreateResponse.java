package hr.tvz.pios.modul.build;

import hr.tvz.pios.common.Message;

/**
 * Odgovor za kreiranje buildova.
 */
public record BuildCreateResponse(String link, Message message) {}
