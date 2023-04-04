package hr.tvz.pios.modul.forum;

import hr.tvz.pios.common.Message;

/**
 * Record koji predstavlja HTTP response za kreiranje novog forum posta.
 */
public record ForumPostCreateResponse(Long id, Message message) { }
