package hr.tvz.pios.modul.forum;

import java.util.List;

/**
 * Record koji predstavlja HTTP response za dohvat liste postova na forumu.
 */
public record ForumResponse(List<Post> posts) { }