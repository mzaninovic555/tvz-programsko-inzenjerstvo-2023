package hr.tvz.pios.modul.forum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa koja predstavlja post na forumu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

  Long id;

  String title;

  String content;

  LocalDateTime createdAt;

  String authorUsername;

  BigDecimal totalPrice;
}
