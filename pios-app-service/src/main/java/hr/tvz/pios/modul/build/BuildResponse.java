package hr.tvz.pios.modul.build;

import hr.tvz.pios.modul.component.Component;
import java.util.List;

/**
 * Rekord za slanje buildova kod dohvaćanja.
 */
public record BuildResponse(
    String link,
    String title,
    String description,
    boolean isPublic,
    boolean isFinalized,
    List<Component> components,
    String ownerUsername) {
  public static BuildResponse fromBuild(Build build) {
    return new BuildResponse(
        build.getLink(),
        build.getTitle(),
        build.getDescription(),
        build.isPublic(),
        build.isFinalized(),
        build.getComponents(),
        build.getUser() == null ? null : build.getUser().getUsername());
  }
}
