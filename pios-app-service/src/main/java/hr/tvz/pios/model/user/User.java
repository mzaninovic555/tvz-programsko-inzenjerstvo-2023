package hr.tvz.pios.model.user;

import hr.tvz.pios.model.role.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  Long id;

  String username;

  String password;

  String email;

  String description;

  LocalDateTime creationDate;

  Boolean isActivated;

  Role role;
}
