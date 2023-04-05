package hr.tvz.pios.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.role.Role;
import hr.tvz.pios.modul.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserSettingsControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private final Map<String, Object> bodyUser = Map.of(
    "oldPassword", "admin1234",
    "newPassword", "pass1234",
    "newPasswordRepeat", "pass1234"
  );

  private final Map<String, Object> bodyUserInvalid = Map.of(
    "oldPassword", "pass1234",
    "newPassword", "pass1234",
    "newPasswordRepeat", "pass1234"
  );

  @Autowired
  JwtService jwtService;

  User user = User.builder().username("johndoe").role(Role.builder().role("ROLE_USER").build()).build();

  @Test
  @Transactional
  void getUserSettings() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/user-settings")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void setUserSettings() throws Exception {
    this.mockMvc.perform(
        put("/api/v1/user-settings")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyUser)))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void setUserSettingsFail() throws Exception {
    this.mockMvc.perform(
        put("/api/v1/user-settings")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyUserInvalid)))
      .andExpect(status().isBadRequest());
  }
}
