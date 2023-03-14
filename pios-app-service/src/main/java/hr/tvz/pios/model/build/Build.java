package hr.tvz.pios.model.build;

import hr.tvz.pios.model.component.Component;
import hr.tvz.pios.model.user.User;
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

  Boolean isPublic;

  User user;

  List<Component> components;
}
