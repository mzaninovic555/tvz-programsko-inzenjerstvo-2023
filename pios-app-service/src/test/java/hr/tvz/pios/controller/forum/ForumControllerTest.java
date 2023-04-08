package hr.tvz.pios.controller.forum;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.forum.ForumPostCreateRequest;
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
public class ForumControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  JwtService jwtService;

  User user = User.builder().username("johndoe").role(Role.builder().role("ROLE_USER").build()).build();

  @Test
  void getAllPosts() throws Exception {
    String title = "TestTitula";
    this.mockMvc.perform(
        get("/api/v1/forum")
          .contentType(MediaType.APPLICATION_JSON)
          .param("title", title))
      .andExpect(status().isOk());
  }

  @Test
  void getPostById() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/forum/id")
          .contentType(MediaType.APPLICATION_JSON)
          .param("id", "1"))
      .andExpect(status().isOk());
  }

  @Test
  void getPostByIdInvalid() throws Exception {
    Integer id = 999999;
    this.mockMvc.perform(
        get("/api/v1/forum/id/"+id)
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }

  @Test
  void getLatestPosts() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/forum/latest")
          .contentType(MediaType.APPLICATION_JSON)
          .param("count", "1"))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void createForumPost() throws Exception {
    ForumPostCreateRequest forumPostCreateRequest = new ForumPostCreateRequest(2L, "title of test post", "content of test post");
    this.mockMvc.perform(
        post("/api/v1/forum/create")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .content(objectMapper.writeValueAsString(forumPostCreateRequest))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void createForumPostExpect400() throws Exception {
    ForumPostCreateRequest forumPostCreateRequest = new ForumPostCreateRequest(3L, "title of test post", "content of test post");
    this.mockMvc.perform(
            post("/api/v1/forum/create")
                .header("authorization", "Bearer " + jwtService.createJwtToken(user))
                .content(objectMapper.writeValueAsString(forumPostCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void deleteForumPost() throws Exception {
    Integer id = 1;
    this.mockMvc.perform(
        post("/api/v1/forum/delete/"+id)
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
}
