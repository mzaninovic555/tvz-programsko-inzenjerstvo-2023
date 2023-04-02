package hr.tvz.pios.modul.build;

import hr.tvz.pios.modul.component.Component;
import hr.tvz.pios.modul.user.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Build {

  Long id;

  String title;

  String description;

  String link;

  boolean isPublic = false;

  boolean isFinalized = false;

  User user;

  List<Component> components;
}
