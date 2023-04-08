package hr.tvz.pios.repository.component;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hr.tvz.pios.common.Type;
import hr.tvz.pios.modul.component.Component;
import hr.tvz.pios.modul.component.ComponentRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles(profiles = "test")
public class ComponentRepositoryTest {

  @Autowired
  ComponentRepository componentRepository;

  @Test
  void getAllFiltered(){
    List<Component> foundComponents = componentRepository.getAllFiltered("Intel", Type.CPU, "Intel", 1, 5000);

    assertFalse(foundComponents.isEmpty());
  }

  @Test
  void getById(){
    assertFalse(componentRepository.getById(1L).isEmpty());
  }

  @Test
  @Transactional
  void deleteById(){
    componentRepository.deleteById(4L);
    assertTrue(componentRepository.getById(4L).isEmpty());
  }

  @Test
  void getTopRated(){
    assertFalse(componentRepository.getTopRated().isEmpty());
  }
}
