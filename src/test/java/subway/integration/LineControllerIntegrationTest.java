package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subway.ui.dto.line.LineCreateRequest;
import subway.ui.dto.line.LineUpdateRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/line_initialize.sql")
@ActiveProfiles("test")
class LineControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("새로운 노선을 추가할 수 있다.")
    void createLine_success() throws Exception {
        // given
        LineCreateRequest request = new LineCreateRequest("5호선", "보라색");

        // expect
        mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/lines/4"))
            .andExpect(jsonPath("$.id").value(4))
            .andExpect(jsonPath("$.name").value("5호선"))
            .andExpect(jsonPath("$.color").value("보라색"))
            .andExpect(jsonPath("$.sections").isEmpty());
    }

    @Test
    @DisplayName("이미 존재하는 이름, 또는 색의 노선을 추가하면 예외가 발생한다.")
    void createLine_fail() throws Exception {
        // given
        LineCreateRequest request = new LineCreateRequest("2호선", "보라색");

        // expect
        mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("한 노선의 모든 구간 정보를 조회할 수 있다.")
    void findLineById_success() throws Exception {
        // expect
        mockMvc.perform(get("/lines/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("2호선"))
            .andExpect(jsonPath("$.color").value("green"))
            .andExpect(jsonPath("$.sections[0].startStationName").value("사당역"))
            .andExpect(jsonPath("$.sections[0].endStationName").value("방배역"))
            .andExpect(jsonPath("$.sections[0].distance").value(10))
            .andExpect(jsonPath("$.sections.size()").value(3));
    }

    @Test
    @DisplayName("존재하지 않는 노선을 조회하면 예외가 발생한다.")
    void findLineById_fail() throws Exception {
        // expect
        mockMvc.perform(get("/lines/6"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("노선을 수정할 수 있다.")
    void updateLine_success() throws Exception {
        // given
        LineUpdateRequest request = new LineUpdateRequest("5호선", "보라색");

        // expect
        mockMvc.perform(put("/lines/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("5호선"))
            .andExpect(jsonPath("$.color").value("보라색"));
    }

    @Test
    @DisplayName("역 정보가 포함된 모든 노선이 조회되어야 한다.")
    void findAllLines_success() throws Exception {
        // expect
        mockMvc.perform(get("/lines"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("2호선"))
            .andExpect(jsonPath("$[0].color").value("green"))
            .andExpect(jsonPath("$[0].sections[0].startStationName").value("사당역"))
            .andExpect(jsonPath("$[0].sections[0].endStationName").value("방배역"))
            .andExpect(jsonPath("$[0].sections[0].distance").value(10))
            .andExpect(jsonPath("$[0].sections.size()").value(3))

            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("3호선"))
            .andExpect(jsonPath("$[1].color").value("orange"))
            .andExpect(jsonPath("$[1].sections[0].startStationName").value("양재역"))
            .andExpect(jsonPath("$[1].sections[0].endStationName").value("남부터미널역"))
            .andExpect(jsonPath("$[1].sections[0].distance").value(10))
            .andExpect(jsonPath("$[1].sections.size()").value(2))

            .andExpect(jsonPath("$[2].id").value(3))
            .andExpect(jsonPath("$[2].name").value("4호선"))
            .andExpect(jsonPath("$[2].color").value("blue"))
            .andExpect(jsonPath("$[2].sections[0].startStationName").value("사당역"))
            .andExpect(jsonPath("$[2].sections[0].endStationName").value("이수역"))
            .andExpect(jsonPath("$[2].sections[0].distance").value(10))
            .andExpect(jsonPath("$[2].sections.size()").value(2));
    }
}
