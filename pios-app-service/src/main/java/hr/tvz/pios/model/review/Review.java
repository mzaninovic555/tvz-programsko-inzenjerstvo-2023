package hr.tvz.pios.model.review;

import hr.tvz.pios.model.post.Post;
import hr.tvz.pios.model.user.User;
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
