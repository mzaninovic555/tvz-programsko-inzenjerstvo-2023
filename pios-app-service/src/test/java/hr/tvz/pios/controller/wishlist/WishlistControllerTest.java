package hr.tvz.pios.controller.wishlist;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.tvz.pios.config.security.jwt.JwtService;
import hr.tvz.pios.modul.role.Role;
import hr.tvz.pios.modul.user.User;
import hr.tvz.pios.modul.wishlist.WishlistRequest;
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
public class WishlistControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  JwtService jwtService;

  User user = User.builder().username("johndoe").role(Role.builder().role("ROLE_USER").build()).build();

  @Test
  void getUserWishlist() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/wishlist")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void clearUserWishlist() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/wishlist/clear")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void deleteItemFromWishlist() throws Exception {
    this.mockMvc.perform(
        delete("/api/v1/wishlist/1")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void deleteItemFromWishlistInvalid() throws Exception {
    this.mockMvc.perform(
        delete("/api/v1/wishlist/69")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void addToWishlist() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/wishlist/add")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(new WishlistRequest(5L))))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void addToWishlistInvalid() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/wishlist/add")
          .header("authorization", "Bearer " + jwtService.createJwtToken(user))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(new WishlistRequest(999999L))))
      .andExpect(status().isBadRequest());
  }
}
