package hr.tvz.pios.modul.build;

import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.config.security.user.UserAuthentication;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler za buildove.
 */
@RestController
@RequestMapping("/api/v1/build")
public class BuildController {
  public final BuildService buildService;

  public BuildController(BuildService buildService) {
    this.buildService = buildService;
  }

  @GetMapping("/from-link/{link}")
  public BuildResponse getBuildByLink(UserAuthentication auth, @PathVariable String link) {
    return buildService.getBuild(auth, link);
  }

  @GetMapping("/my-builds")
  public List<BuildResponse> getUserBuilds(UserAuthentication auth) {
    return buildService.getUserBuilds(auth);
  }

  @PostMapping("/create")
  public BuildCreateResponse createBuild(UserAuthentication auth) {
    return buildService.createBuild(auth);
  }

  @PostMapping("/delete/{link}")
  public BasicResponse removeBuild(UserAuthentication auth, @PathVariable String link) {
    return buildService.removeBuild(auth, link);
  }

  @PostMapping("/edit/info")
  public BuildChangeResponse editInfo(
      UserAuthentication auth,
      @RequestBody @Valid BuildInfoChangeRequest req) {
    return buildService.editInfo(auth, req);
  }

  @PostMapping("/edit/component")
  public BuildChangeResponse editComponent(
      UserAuthentication auth, @RequestBody @Valid BuildComponentChangeRequest req) {
    return buildService.editComponent(auth, req);
  }
}
