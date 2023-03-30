package hr.tvz.pios.modul.component;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.Type;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.security.user.UserAuthentication;
import hr.tvz.pios.modul.review.ReviewRepository;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Servis za pretragu komponenata. */
@Service
public class ComponentService {

  @Autowired ComponentRepository componentRepository;

  @Autowired ReviewRepository reviewRepository;

  @Autowired UserRepository userRepository;

  public List<ComponentSearchResponse> getAllFiltered(
      UserAuthentication auth, String name, String type, Integer minPrice, Integer maxPrice) {
    Type componentType = type == null || type.isBlank() ? null : Type.valueOf(type);
    List<Component> components =
        componentRepository.getAllFiltered(name, componentType, minPrice, maxPrice);

    List<Long> reviewed = getReviewedByUser(auth);
    components.forEach(comp -> comp.setReviewed(reviewed.contains(comp.getId())));

    return components.stream().map(ComponentSearchResponse::new).toList();
  }

  private List<Long> getReviewedByUser(UserAuthentication auth) {
    if (auth == null) {
      return List.of();
    }

    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User does not exist"));
    }

    return reviewRepository.getAllReviewedComponentsByUser(user.get().getId());
  }

  public ComponentSearchResponse getById(UserAuthentication auth, Long id) {
    Optional<Component> componentOptional = componentRepository.getById(id);
    if (componentOptional.isEmpty()) {
      throw PiosException.notFound(Message.error("Component not found"));
    }

    List<Long> reviewed = getReviewedByUser(auth);
    componentOptional.get().setReviewed(reviewed.contains(componentOptional.get().getId()));

    return new ComponentSearchResponse(componentOptional.get());
  }
}
