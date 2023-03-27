package hr.tvz.pios.modul.wishlist;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WishlistRepository {
  Optional<WishlistEntry> getById(Long id);

  void addToUserWishlist(Long userId, Long componentId);

  List<WishlistEntry> getAllForUser(Long userId);

  int removeComponentFromWishlist(Long userId, Long id);

  int clearUserWishlist(Long userId);
}
