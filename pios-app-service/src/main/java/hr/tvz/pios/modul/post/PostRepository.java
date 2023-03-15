package hr.tvz.pios.modul.post;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostRepository {

  Optional<Post> getById(Long id);

  void insert(Post post);

  Integer updateById(Post post);

  Integer deleteById(Long id);
}
