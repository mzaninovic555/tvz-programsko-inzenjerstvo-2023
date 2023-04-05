package hr.tvz.pios.modul.build;

import hr.tvz.pios.modul.component.ComponentResponse;
import java.util.List;

/**
 * Rekord za slanje buildova kod dohvaćanja.
 */
public record BuildResponse(
    Long id,
    String link,
    String title,
    String description,
    Boolean isPublic,
    Boolean isFinalized,
    Boolean isPublished,
    List<ComponentResponse> components,
    String ownerUsername) {
  public static BuildResponse fromBuild(Build build) {
    return new BuildResponse(
        build.getId(),
        build.getLink(),
        build.getTitle(),
        build.getDescription(),
        build.isPublic(),
        build.isFinalized(),
        build.getIsPublished(),
        build.getComponents().stream().map(ComponentResponse::fromComponent).toList(),
        build.getUser() == null ? null : build.getUser().getUsername());
  }
}
