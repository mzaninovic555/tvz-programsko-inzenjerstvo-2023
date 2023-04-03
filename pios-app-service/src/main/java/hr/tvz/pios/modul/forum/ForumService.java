package hr.tvz.pios.modul.forum;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.modul.build.Build;
import hr.tvz.pios.modul.build.BuildRepository;
import hr.tvz.pios.modul.build.BuildResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servis za baratanje postova na forumu.
 */
@Service
public class ForumService {

  @Autowired
  ForumRepository forumRepository;
  @Autowired
  BuildRepository buildRepository;


  public List<Post> getAll(String title) {
    return forumRepository.getAllPosts(title);
  }

  public ForumResponse getById(Long id) {
    Optional<Post> postOptional = forumRepository.getById(id);
    Optional<Build> buildOptional = buildRepository.getById(id);
    if (postOptional.isEmpty() || buildOptional.isEmpty()) {
      throw PiosException.notFound(Message.error("Requested forum post not found"));
    }
    return new ForumResponse(postOptional.get(), BuildResponse.fromBuild(buildOptional.get()));
  }
}
