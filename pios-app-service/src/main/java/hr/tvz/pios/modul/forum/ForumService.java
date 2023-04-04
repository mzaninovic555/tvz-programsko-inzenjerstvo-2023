package hr.tvz.pios.modul.forum;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.security.user.UserAuthentication;
import hr.tvz.pios.modul.build.Build;
import hr.tvz.pios.modul.build.BuildRepository;
import hr.tvz.pios.modul.build.BuildResponse;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.time.LocalDateTime;
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
  @Autowired
  UserRepository userRepository;


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

  public ForumPostCreateResponse createForumPost(UserAuthentication auth, ForumPostCreateRequest request) {
    Optional<Build> postBuildOptional = buildRepository.getById(request.id());
    Optional<User> userOptional = userRepository.getByUsername(auth.getUsername());

    if (postBuildOptional.isEmpty()) {
      throw PiosException.notFound(Message.error("Build doesn't exist"));
    }
    Build build = postBuildOptional.get();

    // build mora biti public za kreiranje posta
    if (!build.isPublic()) {
      throw PiosException.badRequest(Message.error("Build has to be public for creating a forum post"));
    }

    if (userOptional.isPresent() && !build.getUser().getId().equals(userOptional.get().getId())) {
      throw PiosException.forbidden(Message.error("You don't have permission to post the selected build"));
    }

    if (forumRepository.getById(build.getId()).isPresent()) {
      throw PiosException.conflict(Message.error("A post associated with that build already exists"));
    }

    // id-evi posta i builda na koji se veze su jednaki
    Post post = Post.builder()
        .id(build.getId())
        .title(request.title())
        .content(request.content())
        .createdAt(LocalDateTime.now())
        .build();
    forumRepository.insert(post);

    return new ForumPostCreateResponse(post.id, Message.success("Successfully created forum post"));
  }
}
