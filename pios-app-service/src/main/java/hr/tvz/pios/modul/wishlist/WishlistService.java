package hr.tvz.pios.modul.wishlist;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.security.user.UserAuthentication;
import hr.tvz.pios.modul.component.Component;
import hr.tvz.pios.modul.component.ComponentRepository;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
  private final ComponentRepository componentRepository;
  private final UserRepository userRepository;
  private final WishlistRepository wishlistRepository;

  public WishlistService(
      ComponentRepository componentRepository,
      UserRepository userRepository,
      WishlistRepository wishlistRepository) {
    this.componentRepository = componentRepository;
    this.userRepository = userRepository;
    this.wishlistRepository = wishlistRepository;
  }

  public List<WishlistResponse> getUserWishlist(UserAuthentication auth) {
    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User not found"));
    }

    List<WishlistEntry> entries = wishlistRepository.getAllForUser(user.get().getId());
    return entries.stream().map(WishlistResponse::fromWishlistEntry).toList();
  }

  public BasicResponse clearUserWishlist(UserAuthentication auth) {
    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User not found"));
    }

    if (wishlistRepository.getAllForUser(user.get().getId()).isEmpty()) {
      throw PiosException.badRequest(Message.warn("Wishlist is already empty"));
    }

    wishlistRepository.clearUserWishlist(user.get().getId());

    return new BasicResponse(Message.success("Wishlist successfully cleared"));
  }

  public BasicResponse deleteItemFromWishlist(UserAuthentication auth, Long id) {
    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User not found"));
    }

    Optional<WishlistEntry> toDelete = wishlistRepository.getById(id);
    if (toDelete.isEmpty()) {
      throw PiosException.badRequest(Message.error("That item is not on your wishlist"));
    }

    wishlistRepository.removeComponentFromWishlist(user.get().getId(), id);
    return new BasicResponse(
        Message.success(
            "Removed '" + toDelete.get().getComponent().getName() + "' from your wishlist"));
  }

  public BasicResponse addToWishlist(UserAuthentication auth, Long componentId) {
    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User not found"));
    }

    Optional<Component> component = componentRepository.getById(componentId);
    if (component.isEmpty()) {
      throw PiosException.badRequest(Message.error("Request component does not exist"));
    }

    wishlistRepository.addToUserWishlist(user.get().getId(), component.get().getId());

    return new BasicResponse(Message.success("Successfully added the component to your wishlist"));
  }
}
