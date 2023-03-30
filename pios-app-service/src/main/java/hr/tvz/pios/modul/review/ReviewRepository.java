package hr.tvz.pios.modul.review;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Repozitorij za recenzije.
 */
@Mapper
@Repository
public interface ReviewRepository {

  Optional<Review> getById(Long id);

  Optional<Review> getByUserAndComponentId(Long userId, Long componentId);

  void insert(Review review);

  Integer updateById(Review user);

  Integer deleteById(Long id);

  void deleteByUserAndComponent(Long userId, Long componentId);

  Double getRatingForComponent(Long componentId);

  List<Long> getAllReviewedComponentsByUser(Long userId);
}
