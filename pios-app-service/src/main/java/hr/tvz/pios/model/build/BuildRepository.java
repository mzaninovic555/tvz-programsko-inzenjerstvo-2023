package hr.tvz.pios.model.build;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BuildRepository {

  Optional<Build> getById(Long id);

  Optional<Build> getByUserId(Long userId);

  void insert(Build build);

  Integer updateById(Build build);

  Integer deleteById(Long id);
}
