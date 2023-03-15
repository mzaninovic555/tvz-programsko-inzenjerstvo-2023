package hr.tvz.pios.modul.review;

import hr.tvz.pios.modul.post.Post;
import hr.tvz.pios.modul.user.User;
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

  Character rating;

  Post post;

  User user;
}
