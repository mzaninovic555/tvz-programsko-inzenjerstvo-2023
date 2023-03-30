package hr.tvz.pios.modul.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

  Long id;

  Integer rating;

  Long componentId;

  Long userId;
}
