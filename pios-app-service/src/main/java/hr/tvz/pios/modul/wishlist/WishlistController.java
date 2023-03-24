package hr.tvz.pios.modul.wishlist;

import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.config.security.user.UserAuthentication;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class WishlistController {
  private final WishlistService wishlistService;

  public WishlistController(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  @GetMapping("/wishlist")
  public List<WishlistResponse> getUserWishlist(UserAuthentication user) {
    return wishlistService.getUserWishlist(user);
  }

  @PostMapping("/wishlist/clear")
  public BasicResponse clearUserWishlist(UserAuthentication user) {
    return wishlistService.clearUserWishlist(user);
  }

  @DeleteMapping("/wishlist/{id}")
  public BasicResponse deleteItemFromWishlist(UserAuthentication user, @PathVariable Long id) {
    return wishlistService.deleteItemFromWishlist(user, id);
  }

  @PostMapping("/wishlist/add")
  public BasicResponse addToWishlist(UserAuthentication user, @RequestBody @Valid WishlistRequest request) {
    return wishlistService.addToWishlist(user, request.componentId());
  }
}
