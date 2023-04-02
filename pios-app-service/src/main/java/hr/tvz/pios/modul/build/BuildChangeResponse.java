package hr.tvz.pios.modul.build;

import hr.tvz.pios.common.Message;

/**
 * Rekord za slanje odgovora pri promjeni builda.
 */
public record BuildChangeResponse(BuildResponse build, Message message) {}
