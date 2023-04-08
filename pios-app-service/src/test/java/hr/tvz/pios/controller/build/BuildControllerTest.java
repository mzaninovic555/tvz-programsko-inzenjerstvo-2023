package hr.tvz.pios.controller.build;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.build.BuildComponentChangeRequest;
import hr.tvz.pios.modul.build.BuildInfoChangeRequest;
import hr.tvz.pios.modul.role.Role;
import hr.tvz.pios.modul.user.User;
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
public class BuildControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  JwtService jwtService;

  User user = User.builder().username("johndoe").role(Role.builder().role("ROLE_USER").build()).build();

  final String link = "0c74b16c-6f8e-418b-9001-4119777b7906";
  final String link2 = "9566720e-1901-4b6d-8a3c-1c8a65231ae6";

  BuildInfoChangeRequest buildInfoChangeRequest = new BuildInfoChangeRequest(link, "titula", "description", true, true);
  BuildComponentChangeRequest buildComponentChangeRequest = new BuildComponentChangeRequest(link2, 1L, true);

  @Test
  void getBuildByLink() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/build/from-link/" + link)
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void getUserBuilds() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/build/my-builds")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void createBuild() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/build/create")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void removeBuild() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/build/delete/" + link2)
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void editInfo() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/build/edit/info")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .content(objectMapper.writeValueAsString(buildInfoChangeRequest))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void editComponent() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/build/edit/info")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .content(objectMapper.writeValueAsString(buildComponentChangeRequest))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
}
