package subway.integration;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.path.PathRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/path_data_initialize.sql")
class PathControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("경로를 조회하면 최단 경로와 요금이 조회되어야 한다.")
    void findPath_success() throws Exception {
        // given
        PathRequest request = new PathRequest("A역", "I역");

        // expect
        mockMvc.perform(post("/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("A역에서 I역까지의 경로가 조회되었습니다."))
                .andExpect(jsonPath("$.result.stations[0]").value("A역"))
                .andExpect(jsonPath("$.result.stations[1]").value("D역"))
                .andExpect(jsonPath("$.result.stations[2]").value("F역"))
                .andExpect(jsonPath("$.result.stations[3]").value("G역"))
                .andExpect(jsonPath("$.result.stations[4]").value("I역"))
                .andExpect(jsonPath("$.result.totalDistance").value(11))
                .andExpect(jsonPath("$.result.price").value(1350));
    }

    @Test
    @DisplayName("없는 역을 조회하면 조회에 실패해야 한다.")
    void findPath_noExistsStation() throws Exception {
        // given
        PathRequest request = new PathRequest("A역", "없는역");

        // expect
        mockMvc.perform(post("/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 역이 구간에 존재하지 않습니다."));
    }

    @Test
    @DisplayName("조회할 역이 같으면 조회에 실패해야 한다.")
    void findPath_sameStations() throws Exception {
        // given
        PathRequest request = new PathRequest("A역", "A역");

        // expect
        mockMvc.perform(post("/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("조회할 역이 서로 같습니다."));
    }

    @Test
    @DisplayName("조회할 역이 있지만, 구간에 존재하지 않으면 조회에 실패해야 한다.")
    void findPath_notExistingInSection() throws Exception {
        // given
        PathRequest request = new PathRequest("A역", "?역");

        // expect
        mockMvc.perform(post("/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 역이 구간에 존재하지 않습니다."));
    }

    @Test
    @DisplayName("경로가 없으면 조회에 실패해야 한다.")
    void findPath_invalidPath() throws Exception {
        // given
        PathRequest request = new PathRequest("A역", "Z역");

        // expect
        mockMvc.perform(post("/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 경로를 찾을 수 없습니다."));
    }
}
