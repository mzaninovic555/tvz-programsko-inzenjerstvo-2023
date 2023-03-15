package hr.tvz.pios.modul.user;

import hr.tvz.pios.modul.role.Role;
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
