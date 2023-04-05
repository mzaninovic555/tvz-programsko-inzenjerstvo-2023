package hr.tvz.pios.modul.wishlist;

import hr.tvz.pios.modul.component.ComponentResponse;
import java.time.LocalDateTime;

public record WishlistResponse(Long id, LocalDateTime addedAt, ComponentResponse component) {
  static WishlistResponse fromWishlistEntry(WishlistEntry entry) {
    return new WishlistResponse(
        entry.getId(),
        entry.getCreationDate(),
        ComponentResponse.fromComponent(entry.getComponent()));
  }
}
