package hr.tvz.pios.repository.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hr.tvz.pios.modul.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  String username = "johndoe";

  @Test
  void isUsernameTaken(){
    assertTrue(userRepository.isUsernameTaken(username));
  }

  @Test
  void isEmailTaken(){
    assertFalse(userRepository.isEmailTaken("email@email.com"));
  }

  @Test
  void getByUsername(){
    assertFalse(userRepository.getByUsername(username).isEmpty());
  }

  @Test
  @Transactional
  void deleteById(){
    userRepository.deleteById(5L);
    assertTrue(userRepository.getByUsername("jimmychoo").isEmpty());
  }

  @Test
  @Transactional
  void deactivateByUsername(){
    userRepository.deactivateByUsername(username);
    assertFalse(userRepository.getByUsername(username).isEmpty());
  }
}
