package hr.tvz.pios.modul.forum;

import hr.tvz.pios.modul.build.BuildResponse;

/**
 * Record koji predstavlja HTTP response za dohvat liste postova na forumu.
 */
public record ForumResponse(Post post, BuildResponse build) { }