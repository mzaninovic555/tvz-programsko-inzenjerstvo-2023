package hr.tvz.pios.modul.review;

import hr.tvz.pios.config.security.user.UserAuthentication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler za recenzije.
 */
@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping("/add")
  public ReviewResponse createReview(
      UserAuthentication auth, @RequestBody @Valid ReviewRequest request) {
    return reviewService.createReview(auth, request);
  }

  @PostMapping("/remove/{componentId}")
  public ReviewResponse removeReview(UserAuthentication auth, @PathVariable Long componentId) {
    return reviewService.removeReview(auth, componentId);
  }
}
