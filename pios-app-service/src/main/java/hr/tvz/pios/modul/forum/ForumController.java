package hr.tvz.pios.modul.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public ForumResponse getAll() {
    return forumService.getAll();
  }
}
