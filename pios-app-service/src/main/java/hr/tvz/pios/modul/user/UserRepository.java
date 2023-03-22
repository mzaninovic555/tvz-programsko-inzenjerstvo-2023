package hr.tvz.pios.modul.user;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {

  /**
   * Postoji li korisnik sa korisničkim imenom.
   * @return {@link Boolean}
   */
  Boolean isUsernameTaken(String username);

  /**
   * Postoji li korisnik sa email adresom.
   * @return {@link Boolean}
   */
  Boolean isEmailTaken(String email);

  Optional<User> getById(Long id);

  Optional<User> getByUsername(String username);

  Optional<User> getByEmail(String email);

  void insert(User user);

  Integer updateById(User user);

  Integer deleteById(Long id);
}
