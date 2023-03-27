package hr.tvz.pios.modul.register;

/**
 * Record koji predstavlja HTTP request za aktivaciju korisnika.
 * @param activationToken generirani Base64 token. <code>username_timestamp</code>
 */
public record ActivateRequest(String activationToken) { }
