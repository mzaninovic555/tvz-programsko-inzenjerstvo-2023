package hr.tvz.pios.controller.review;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.review.ReviewRequest;
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
public class ReviewControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  JwtService jwtService;

  User user = User.builder().username("johndoe").role(Role.builder().role("ROLE_USER").build()).build();

  @Test
  @Transactional
  void createReview() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/review/add")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(new ReviewRequest(1L, 5))))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void createReviewInvalid() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/review/add")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(new ReviewRequest(999999L, 5))))
      .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void removeReview() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/review/remove/1")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void removeReviewInvalid() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/review/remove/999999")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
}
