package hr.tvz.pios.modul.review;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.security.user.UserAuthentication;
import hr.tvz.pios.modul.component.Component;
import hr.tvz.pios.modul.component.ComponentRepository;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Servis za recenzije.
 */
@Service
public class ReviewService {
  private final ComponentRepository componentRepository;
  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;

  public ReviewService(
      ComponentRepository componentRepository,
      ReviewRepository reviewRepository,
      UserRepository userRepository) {
    this.componentRepository = componentRepository;
    this.reviewRepository = reviewRepository;
    this.userRepository = userRepository;
  }

  public ReviewResponse createReview(UserAuthentication auth, ReviewRequest request) {
    Optional<Component> component = componentRepository.getById(request.componentId());
    if (component.isEmpty()) {
      throw PiosException.badRequest(Message.error("Requested component does not exist"));
    }

    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User does not exist"));
    }

    Optional<Review> existing = reviewRepository.getByUserAndComponentId(user.get().getId(), component.get().getId());

    if (existing.isPresent()) {
      existing.get().setRating(request.rating());
      reviewRepository.updateById(existing.get());
    } else {
      var review = new Review();
      review.setComponentId(component.get().getId());
      review.setRating(request.rating());
      review.setUserId(user.get().getId());

      reviewRepository.insert(review);
      component
          .get()
          .setReviewCount(
              component.get().getReviewCount() == null ? 1 : (component.get().getReviewCount() + 1));
    }

    Double newRating = reviewRepository.getRatingForComponent(component.get().getId());
    return new ReviewResponse(newRating, component.get().getReviewCount());
  }

  public ReviewResponse removeReview(UserAuthentication auth, Long componentId) {
    Optional<Component> component = componentRepository.getById(componentId);
    if (component.isEmpty()) {
      throw PiosException.badRequest(Message.error("Requested component does not exist"));
    }

    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User does not exist"));
    }

    if (reviewRepository.getByUserAndComponentId(user.get().getId(), component.get().getId()).isEmpty()) {
      throw PiosException.conflict(Message.error("You haven't reviewed this component yet"));
    }

    reviewRepository.deleteByUserAndComponent(user.get().getId(), componentId);
    component.get().setReviewCount(component.get().getReviewCount() - 1);

    Double newRating = reviewRepository.getRatingForComponent(component.get().getId());
    return new ReviewResponse(newRating, component.get().getReviewCount());
  }
}
