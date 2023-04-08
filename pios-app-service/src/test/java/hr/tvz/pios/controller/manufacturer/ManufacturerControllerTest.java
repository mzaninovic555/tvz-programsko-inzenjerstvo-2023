package hr.tvz.pios.controller.manufacturer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class ManufacturerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void validateToken() throws Exception {
    this.mockMvc.perform(
        get("/api/v1/manufacturer")
          .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
}
