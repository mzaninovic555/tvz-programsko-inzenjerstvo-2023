package hr.tvz.pios.repository.wishlist;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hr.tvz.pios.modul.wishlist.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WishlistRepositoryTest {

  @Autowired
  WishlistRepository wishlistRepository;

  @Test
  void getById(){
    assertTrue(wishlistRepository.getById(1L).isPresent());
  }

  @Test
  @Transactional
  void addToUserWishlist(){
    Integer wishlistComponentsNum = wishlistRepository.getAllForUser(1L).size();
    wishlistRepository.addToUserWishlist(1L, 5L);
    assertTrue(wishlistComponentsNum < wishlistRepository.getAllForUser(1L).size());
  }

  @Test
  void getAllForUser(){
    assertFalse(wishlistRepository.getAllForUser(1L).isEmpty());
  }

  @Test
  @Transactional
  void removeComponentFromWishlist(){
    wishlistRepository.removeComponentFromWishlist(1L, 1L);
    assertTrue(wishlistRepository.getAllForUser(1L).isEmpty());
  }

  @Test
  @Transactional
  void clearUserWishlist(){
    wishlistRepository.clearUserWishlist(1L);
    assertTrue(wishlistRepository.getAllForUser(1L).isEmpty());
  }
}
