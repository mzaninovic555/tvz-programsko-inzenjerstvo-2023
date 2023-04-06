package hr.tvz.pios.repository.manufacturer;

import hr.tvz.pios.modul.manufacturer.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ManufacturerRepositoryTest {

  @Autowired
  ManufacturerRepository manufacturerRepository;

  @Test
  void getAllManufacturer(){
    assertFalse(manufacturerRepository.getAll().isEmpty());
  }

  @Test
  void getById(){
    assertFalse(manufacturerRepository.getById(1L).isEmpty());
  }

  @Test
  @Transactional
  void deleteById(){
    Long id = 999L;
    manufacturerRepository.deleteById(id);
    assertTrue(manufacturerRepository.getById(id).isEmpty());
  }
}
