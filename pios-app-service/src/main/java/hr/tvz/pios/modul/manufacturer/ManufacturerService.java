package hr.tvz.pios.modul.manufacturer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService {

  @Autowired
  ManufacturerRepository manufacturerRepository;

  public List<ManufacturerResponse> getManufacturers() {
    return manufacturerRepository.getAll().stream().map(ManufacturerResponse::new).toList();
  }
}
