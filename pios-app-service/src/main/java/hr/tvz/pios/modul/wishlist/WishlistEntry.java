package hr.tvz.pios.modul.wishlist;

import hr.tvz.pios.modul.component.Component;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistEntry {
  Long id;
  Long userId;
  Component component;
  LocalDateTime creationDate;
}
