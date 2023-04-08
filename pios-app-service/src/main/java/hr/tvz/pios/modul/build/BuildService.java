package hr.tvz.pios.modul.build;

import hr.tvz.pios.common.Message;
import hr.tvz.pios.common.Type;
import hr.tvz.pios.common.exception.BasicResponse;
import hr.tvz.pios.common.exception.PiosException;
import hr.tvz.pios.config.security.user.UserAuthentication;
import hr.tvz.pios.modul.build.compatibility.CompatibilityError;
import hr.tvz.pios.modul.build.compatibility.CompatibilityService;
import hr.tvz.pios.modul.component.Component;
import hr.tvz.pios.modul.component.ComponentRepository;
import hr.tvz.pios.modul.forum.ForumRepository;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Servis za buildove.
 */
@Service
public class BuildService {
  private final BuildRepository buildRepository;
  private final ComponentRepository componentRepository;
  private final UserRepository userRepository;
  private final ForumRepository forumRepository;

  public BuildService(
      BuildRepository buildRepository,
      ComponentRepository componentRepository,
      UserRepository userRepository, ForumRepository forumRepository) {
    this.buildRepository = buildRepository;
    this.componentRepository = componentRepository;
    this.userRepository = userRepository;
    this.forumRepository = forumRepository;
  }

  public List<BuildResponse> getUserBuilds(UserAuthentication auth) {
    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User does not exist"));
    }

    return buildRepository.getByUserId(user.get().getId()).stream()
        .map(build -> BuildResponse.fromBuild(build, new Message[]{}))
        .toList();
  }

  public BuildCreateResponse createBuild(UserAuthentication auth) {
    Optional<User> user =
        auth == null ? Optional.empty() : userRepository.getByUsername(auth.getUsername());

    if (auth != null && user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User does not exist"));
    }

    Build build = new Build();
    if (user.isEmpty()) {
      build.setPublic(true);
    }
    build.setLink(UUID.randomUUID().toString());
    build.setUser(user.orElse(null));

    buildRepository.insert(build);

    return new BuildCreateResponse(build.getLink(), Message.info("Build created successfully"));
  }

  public BuildResponse getBuild(UserAuthentication auth, String link) {
    Optional<Build> build = buildRepository.getByLink(link);
    if (build.isEmpty()) {
      throw PiosException.notFound(Message.error("Build not found"));
    }

    if (!build.get().isPublic()
        && (auth == null || !build.get().getUser().getUsername().equals(auth.getUsername()))) {
      throw PiosException.forbidden(Message.error("You do not have permission to view this build"));
    }

    return BuildResponse.fromBuild(
        build.get(), checkComponentsCompatibility(build.get().getComponents()));
  }

  public BasicResponse removeBuild(UserAuthentication auth, String link) {
    Optional<User> user = userRepository.getByUsername(auth.getUsername());
    if (user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User does not exist"));
    }

    Optional<Build> build = buildRepository.getByLink(link);
    if (build.isEmpty()) {
      throw PiosException.badRequest(Message.error("Requested build does not exist"));
    }

    if (build.get().getUser() == null
        || !build.get().getUser().getUsername().equals(auth.getUsername())) {
      throw PiosException.forbidden(
          Message.error("You do not have permission to delete this build"));
    }

    try {
      buildRepository.deleteById(build.get().getId());
    } catch (org.springframework.dao.DataIntegrityViolationException ex) {
      throw PiosException.badRequest(
          Message.warn("You can't delete this build because it has a forum post associated to it. Delete the post first"));
    }

    return new BasicResponse(Message.success("Build deleted successfully"));
  }

  public BuildChangeResponse editInfo(UserAuthentication auth, BuildInfoChangeRequest req) {
    Build build = validateRequest(auth, req.link());

    // ak ocemo mijenjat build u private, i postoji forum post, baci exception
    if (build.isPublic() && req.isPublic() != build.isPublic && forumRepository.getById(build.getId()).isPresent()) {
      throw PiosException.badRequest(
          Message.error("You can't change build to private while it's associated with a forum post"));
    }

    build.setFinalized(req.isFinalized());
    if (build.getUser() != null) {
      build.setPublic(req.isPublic());
    }
    build.setTitle(req.title());
    build.setDescription(req.description());

    buildRepository.updateById(build);

    return new BuildChangeResponse(
        BuildResponse.fromBuild(build, checkComponentsCompatibility(build.getComponents())),
        Message.success("Build info updated successfully"));
  }

  public BuildChangeResponse editComponent(
      UserAuthentication auth, BuildComponentChangeRequest req) {
    Build build = validateRequest(auth, req.link());

    Optional<Component> component = componentRepository.getById(req.componentId());
    if (component.isEmpty()) {
      throw PiosException.badRequest(Message.error("Request component does not exist"));
    }

    if (!req.add() && !buildRepository.buildHasComponent(build.getId(), component.get().getId())) {
      throw PiosException.conflict(
          Message.error(
              "Component '" + component.get().getName() + "' is not a part of the build"));
    }

    if (req.add()) {
      buildRepository.addComponent(build.getId(), component.get().getId());
    } else {
      buildRepository.removeComponent(build.getId(), component.get().getId());
    }

    Build newBuild = buildRepository.getByLink(req.link()).get();

    String message =
        "Component '"
            + component.get().getName()
            + "' "
            + (req.add() ? "added" : "removed")
            + " successfully";

    return new BuildChangeResponse(
        BuildResponse.fromBuild(newBuild, checkComponentsCompatibility(newBuild.getComponents())),
        Message.success(message));
  }

  public Build validateRequest(UserAuthentication auth, String link) {
    Optional<User> user =
        auth == null ? Optional.empty() : userRepository.getByUsername(auth.getUsername());

    if (auth != null && user.isEmpty()) {
      throw PiosException.badRequest(Message.error("User does not exist"));
    }

    Optional<Build> build = buildRepository.getByLink(link);
    if (build.isEmpty()) {
      throw PiosException.badRequest(Message.error("Requested build does not exist"));
    }

    if (build.get().getUser() != null
        && (auth == null || !build.get().getUser().getUsername().equals(auth.getUsername()))) {
      throw PiosException.forbidden(Message.error("You do not have permission to edit this build"));
    }

    if (build.get().isFinalized() && auth == null) {
      throw PiosException.forbidden(Message.error("You cannot edit builds that are finalized"));
    }

    return build.get();
  }

  /**
   * Provjerava komponente za predefinirane probleme u kompatibilnosti.
   * @param components lista komponenti
   * @return lista poruka sa kompatibilnim problemima
   */
  // CHECKSTYLE:OFF
  public Message[] checkComponentsCompatibility(List<Component> components) {
    List<Message> messages = new ArrayList<>();
    Map<Type, Component> mapTypeComponent = components
        .stream()
        .collect(Collectors.toMap(Component::getType, Function.identity()));

    if (mapTypeComponent.containsKey(Type.PSU)) {
      Boolean check = CompatibilityService.checkMaxPowerAndPSUCompatibility(
          components, mapTypeComponent.get(Type.PSU).getData());
      if (!check) {
        messages.add(Message.warn(CompatibilityError.MAX_POWER_PSU.getErrorText()));
      }
    }

    if (mapTypeComponent.containsKey(Type.MOTHERBOARD) && mapTypeComponent.containsKey(Type.RAM)) {
      Boolean check = CompatibilityService.checkMotherboardAndRAMCompatibility(
          mapTypeComponent.get(Type.RAM).getData(), mapTypeComponent.get(Type.MOTHERBOARD).getData());
      if (!check) {
        messages.add(Message.warn(CompatibilityError.MOTHERBOARD_RAM.getErrorText()));
      }
    }

    if (mapTypeComponent.containsKey(Type.MOTHERBOARD) && mapTypeComponent.containsKey(Type.RAM)) {
      Boolean check = CompatibilityService.checkMotherboardAndRAMSlotNumberCompatibility(
          mapTypeComponent.get(Type.RAM).getData(), mapTypeComponent.get(Type.MOTHERBOARD).getData());
      if (!check) {
        messages.add(Message.warn(CompatibilityError.MOTHERBOARD_RAM_SLOTS.getErrorText()));
      }
    }

    if (mapTypeComponent.containsKey(Type.CASE) && mapTypeComponent.containsKey(Type.MOTHERBOARD)) {
      Boolean check = CompatibilityService.checkCaseAndMotherboardCompatibility(
          mapTypeComponent.get(Type.CASE).getData(), mapTypeComponent.get(Type.MOTHERBOARD).getData());
      if (!check) {
        messages.add(Message.warn(CompatibilityError.MOTHERBOARD_CASE.getErrorText()));
      }
    }

    if (mapTypeComponent.containsKey(Type.CPU) && mapTypeComponent.containsKey(Type.MOTHERBOARD)) {
      Boolean check = CompatibilityService.checkCPUAndMotherboardCompatibility(
          mapTypeComponent.get(Type.CPU).getData(), mapTypeComponent.get(Type.MOTHERBOARD).getData());
      if (!check) {
        messages.add(Message.warn(CompatibilityError.MOTHERBOARD_CPU.getErrorText()));
      }
    }

    if (mapTypeComponent.containsKey(Type.CPU) && mapTypeComponent.containsKey(Type.COOLER)) {
      Boolean check = CompatibilityService.checkCPUAndCoolerCompatibility(
          mapTypeComponent.get(Type.CPU).getData(), mapTypeComponent.get(Type.COOLER).getData());
      if (!check) {
        messages.add(Message.warn(CompatibilityError.CPU_COOLER.getErrorText()));
      }
    }
    return messages.toArray(new Message[0]);
  }
  // CHECKSTYLE:ON
}
