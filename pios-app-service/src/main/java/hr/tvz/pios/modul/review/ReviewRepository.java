package hr.tvz.pios.modul.review;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReviewRepository {

  Optional<Review> getById(Long id);

  void insert(Review user);

  Integer updateById(Review user);

  Integer deleteById(Long id);
}
