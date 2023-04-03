package hr.tvz.pios.modul.forum;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/{id}")
  public ForumResponse getById(@PathVariable Long id) {
    return forumService.getById(id);
  }
}
