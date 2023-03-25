package hr.tvz.pios.modul.component;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.exception.PiosException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servis za pretragu komponenata.
 */
@Service
public class ComponentService {

  @Autowired
  ComponentRepository componentRepository;

  public List<ComponentSearchResponse> getAll() {
    List<Component> components = componentRepository.getAll();
    return components.stream().map(ComponentSearchResponse::new).toList();
  }

  public ComponentSearchResponse getById(Long id) {
    Optional<Component> componentOptional = componentRepository.getById(id);
    if (componentOptional.isEmpty()) {
      throw PiosException.notFound(Message.error("Component not found"));
    }
    return new ComponentSearchResponse(componentOptional.get());
  }
}
