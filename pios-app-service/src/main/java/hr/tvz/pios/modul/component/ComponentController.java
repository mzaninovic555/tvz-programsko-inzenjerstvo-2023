package hr.tvz.pios.modul.component;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST kontroler za pretragu komponenata.
 */
@RestController
@RequestMapping("/api/v1/component")
public class ComponentController {

  @Autowired
  ComponentService componentService;

  @GetMapping
  public List<ComponentSearchResponse> getComponents(
      @RequestParam String name,
      @RequestParam String type,
      @RequestParam Integer minPrice,
      @RequestParam Integer maxPrice) {
    return componentService.getAllFiltered(name, type, minPrice, maxPrice);
  }

  @GetMapping("/{id}")
  public ComponentSearchResponse getById(@PathVariable Long id) {
    return componentService.getById(id);
  }
}
