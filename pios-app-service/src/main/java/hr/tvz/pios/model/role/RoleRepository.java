package hr.tvz.pios.model.role;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleRepository {

  Optional<Role> getById(Long id);

  Optional<Role> getByName(String role);

  void insert(Role role);

  Integer deleteById(Long id);
}
