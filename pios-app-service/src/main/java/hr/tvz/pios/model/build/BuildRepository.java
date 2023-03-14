package hr.tvz.pios.model.build;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BuildRepository {

  Optional<Build> getById(Long id);

  List<Build> getByUserId(Long userId);

  void insert(Build build);

  void insertBuildComponent(Long buildId, Long ComponentId);

  Integer updateById(Build build);

  Integer deleteById(Long id);
}
