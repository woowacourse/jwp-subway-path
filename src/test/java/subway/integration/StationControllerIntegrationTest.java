package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.station.StationCreateRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("역을 저장할 수 있다.")
    void createStation() throws Exception {
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new StationCreateRequest("정자역"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationName").value("정자역"));
    }

    @Test
    @DisplayName("저장된 모든 역을 조회할 수 있다.")
    void showStations() throws Exception {
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new StationCreateRequest("정자역"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationName").value("정자역"));

        mockMvc.perform(get("/stations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

}