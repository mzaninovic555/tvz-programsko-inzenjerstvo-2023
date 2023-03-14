package hr.tvz.pios.model.component;

import hr.tvz.pios.common.Type;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ComponentRepository {

  Optional<Component> getById(Long id);

  Optional<Component> getByType(Type type);

  void insert(Component component);

  Integer updateById(Component component);

  Integer deleteById(Long id);
}
