package hr.tvz.pios.modul.user;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {

  Optional<User> getById(Long id);

  Optional<User> getByUsername(String username);

  void insert(User user);

  Integer updateById(User user);

  Integer deleteById(Long id);
}
