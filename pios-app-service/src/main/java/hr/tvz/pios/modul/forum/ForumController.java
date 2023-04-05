package hr.tvz.pios.modul.forum;

import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.config.security.user.UserAuthentication;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST kontroler za dohvat i dodavanje postova na forumu.
 */
@RestController
@RequestMapping("/api/v1/forum")
public class ForumController {

  @Autowired
  ForumService forumService;

  @GetMapping
  public List<Post> getAll(@RequestParam String title) {
    return forumService.getAll(title);
  }

  @GetMapping("/id/{id}")
  public ForumResponse getById(@PathVariable Long id) {
    return forumService.getById(id);
  }

  @GetMapping("/latest")
  public List<Post> getLatestPosts(@RequestParam Integer count) {
    if (count == null) {
      count = 5;
    }
    return forumService.getLatestPosts(count);
  }

  @PostMapping("/create")
  public ForumPostCreateResponse createForumPost(UserAuthentication auth,
      @RequestBody ForumPostCreateRequest request) {
    return forumService.createForumPost(auth, request);
  }

  @PostMapping("/delete/{id}")
  public BasicResponse deleteForumPost(UserAuthentication auth, @PathVariable Long id) {
    return forumService.deletePost(auth, id);
  }
}
