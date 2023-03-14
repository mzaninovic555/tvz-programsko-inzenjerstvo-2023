package hr.tvz.pios.model.build;

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

  Long userId;
}
