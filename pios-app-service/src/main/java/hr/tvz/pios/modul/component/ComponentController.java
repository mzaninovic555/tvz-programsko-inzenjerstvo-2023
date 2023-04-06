package hr.tvz.pios.modul.component;

import hr.tvz.pios.config.security.user.UserAuthentication;
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
  public List<ComponentResponse> getComponents(
      UserAuthentication auth,
      @RequestParam String name,
      @RequestParam String type,
      @RequestParam String manufacturer,
      @RequestParam Integer minPrice,
      @RequestParam Integer maxPrice) {
    return componentService.getAllFiltered(auth, name, type, manufacturer, minPrice, maxPrice);
  }

  @GetMapping("/{id}")
  public ComponentResponse getById(UserAuthentication auth, @PathVariable Long id) {
    return componentService.getById(auth, id);
  }

  @GetMapping("/top-rated")
  public List<ComponentResponse> getTopRatedComponents() {
    return componentService.getTopRatedComponents();
  }
}
