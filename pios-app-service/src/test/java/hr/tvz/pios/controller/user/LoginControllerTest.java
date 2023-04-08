package hr.tvz.pios.controller.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.role.Role;
import hr.tvz.pios.modul.user.User;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class LoginControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  JwtService jwtService;

  User user = User.builder().username("johndoe").role(Role.builder().role("ROLE_USER").build()).build();
  private final Map<String, Object> bodyAdmin = Map.of(
    "username", "johndoe",
    "password", "admin1234"
  );
  private final Map<String, Object> bodyAdminInvalid = Map.of(
    "username", "johndoe",
    "password", "user1234"
  );
  private final Map<String, Object> bodyUser = Map.of(
    "username", "janedoe",
    "password", "user1234"
  );
  private final Map<String, Object> bodyUserInvalid = Map.of(
    "username", "janedoe",
    "password", "password"
  );

  @Test
  void loginAdmin() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyAdmin)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @Test
  void loginAdminInvalid() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyAdminInvalid)))
      .andExpect(status().isBadRequest());
  }

  @Test
  void loginUser() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyUser)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @Test
  void loginUserInvalid() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyUserInvalid)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void validatePassword() throws Exception {
    final Map<String, String> password = Map.of(
      "password", "admin1234"
    );
    this.mockMvc.perform(
        post("/api/v1/validate-password")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(password)))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void validateToken() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/validate-token")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void deactivateAccount() throws Exception {
    final Map<String, String> username = Map.of(
      "username", "johndoe"
    );

    this.mockMvc.perform(
        post("/api/v1/deactivate-account")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(username)))
      .andExpect(status().isOk());
  }
}
