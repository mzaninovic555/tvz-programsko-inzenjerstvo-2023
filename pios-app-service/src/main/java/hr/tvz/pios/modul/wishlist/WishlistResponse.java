package hr.tvz.pios.modul.wishlist;

import hr.tvz.pios.modul.component.Component;
import java.time.LocalDateTime;

public record WishlistResponse(Long id, LocalDateTime addedAt, Component component) {
  static WishlistResponse fromWishlistEntry(WishlistEntry entry) {
    return new WishlistResponse(entry.getId(), entry.getCreationDate(), entry.getComponent());
  }
}
