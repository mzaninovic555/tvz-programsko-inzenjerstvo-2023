package hr.tvz.pios.repository.role;

import hr.tvz.pios.modul.role.Role;
import hr.tvz.pios.modul.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoleRepositoryTest {

  @Autowired
  RoleRepository roleRepository;

  @Test
  void getById(){
    assertTrue(roleRepository.getById(1L).isPresent());
  }

  @Test
  void getByName(){
    assertTrue(roleRepository.getByName("ROLE_USER").isPresent());
  }

  @Test
  @Transactional
  void insert(){
    roleRepository.insert(Role.builder().role("ROLE_SMRT").build());
    assertTrue(roleRepository.getByName("ROLE_SMRT").isPresent());
  }

  @Test
  @Transactional
  void deleteById(){
    roleRepository.insert(Role.builder().role("ROLE_SMRT").build());
    Optional<Role> optionalRole = roleRepository.getByName("ROLE_SMRT");
    roleRepository.deleteById(optionalRole.get().getId());
    assertFalse(roleRepository.getById(optionalRole.get().getId()).isPresent());
  }
}
