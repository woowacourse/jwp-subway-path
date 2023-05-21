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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subway.ui.dto.station.StationCreateRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("새로운 역을 저장할 수 있다.")
    void createStation_success() throws Exception {
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new StationCreateRequest("정자역"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("정자역"));
    }

    @Test
    @DisplayName("이미 존재하는 이름의 역을 추가하면 예외가 발생한다.")
    void createStation_fail() throws Exception {
        // given
        createNewStation("미금역");
        StationCreateRequest request = new StationCreateRequest("미금역");

        // expect
        mockMvc.perform(post("/stations")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("저장된 모든 역을 조회할 수 있다.")
    void showStations() throws Exception {
        createNewStation("정자역");

        mockMvc.perform(get("/stations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    private void createNewStation(String name) throws Exception {
        StationCreateRequest createRequest = new StationCreateRequest(name);

        mockMvc.perform(post("/stations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createRequest)));
    }

}