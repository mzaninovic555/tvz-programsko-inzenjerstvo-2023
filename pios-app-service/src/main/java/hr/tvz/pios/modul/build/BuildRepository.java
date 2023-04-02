package hr.tvz.pios.modul.build;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Repozitorij za buildove.
 */
@Mapper
@Repository
public interface BuildRepository {

  Optional<Build> getById(Long id);

  List<Build> getByUserId(Long userId);

  void insert(Build build);

  void insertBuildComponent(Long buildId, Long componentId);

  Integer updateById(Build build);

  void deleteById(Long id);

  Optional<Build> getByLink(String link);

  boolean buildHasComponent(Long buildId, Long componentId);

  void addComponent(Long buildId, Long componentId);

  void removeComponent(Long buildId, Long componentId);
}
