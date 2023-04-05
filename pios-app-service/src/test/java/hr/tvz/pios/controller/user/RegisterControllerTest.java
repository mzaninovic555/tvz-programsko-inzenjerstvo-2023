package hr.tvz.pios.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  private final Map<String, Object> bodyUser = Map.of(
    "email", "tmil@tvz.hr",
    "username", "tmilakovi",
    "password", "tpassword"
  );

  private final Map<String, Object> bodyUserInvalid = Map.of(
    "email", "tmil@tvz.hr",
    "username", "tm",
    "password", "tpassword"
  );

  @Test
  @Transactional
  void registerSuccess() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyUser)))
      .andExpect(status().isOk());
  }

  @Test
  @Transactional
  void registerFail() throws Exception {
    this.mockMvc.perform(
        post("/api/v1/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(bodyUserInvalid)))
      .andExpect(status().isBadRequest());
  }
}
