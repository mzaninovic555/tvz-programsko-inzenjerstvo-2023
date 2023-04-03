package hr.tvz.pios.modul.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servis za baratanje postova na forumu.
 */
@Service
public class ForumService {

  @Autowired
  ForumRepository forumRepository;

  public ForumResponse getAll() {
    return new ForumResponse(forumRepository.getAllPosts());
  }
}
