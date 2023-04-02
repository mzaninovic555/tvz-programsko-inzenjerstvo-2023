package hr.tvz.pios.modul.manufacturer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manufacturer")
public class ManufacturerController {

  @Autowired
  ManufacturerService manufacturerService;

  @GetMapping
  public List<ManufacturerResponse> getManufacturers() {
    return manufacturerService.getManufacturers();
  }
}
