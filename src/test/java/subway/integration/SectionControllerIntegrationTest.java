package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.SectionCreateRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/section_initialize.sql")
class SectionControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Long lineId = 1L;

    @Test
    @DisplayName("빈 노선에 구간을 추가하면 추가에 성공한다.")
    void addSection_toEmptyLine_success() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", 50);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[0].distance").value(50));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가하면 추가에 성공한다.")
    void addSection_betweenStation_success() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", 50);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("잠실역", "대구역", 20);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isOk());

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$[0].endStationName").value("대구역"))
                .andExpect(jsonPath("$[0].distance").value(20))
                .andExpect(jsonPath("$[1].startStationName").value("대구역"))
                .andExpect(jsonPath("$[1].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[1].distance").value(30));
    }

    @Test
    @DisplayName("노선의 상행 종점 역 앞에 새로운 역을 추가하면 추가에 성공한다.")
    void addSection_frontOfStartStation_success() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", 50);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("대구역", "잠실역", 20);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isOk());

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$[0].startStationName").value("대구역"))
                .andExpect(jsonPath("$[0].endStationName").value("잠실역"))
                .andExpect(jsonPath("$[0].distance").value(20))
                .andExpect(jsonPath("$[1].startStationName").value("잠실역"))
                .andExpect(jsonPath("$[1].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[1].distance").value(50));
    }

    @Test
    @DisplayName("노선의 하행 종점 역 뒤에 역을 추가하면 추가에 성공한다.")
    void addSection_behindEndStation_success() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", 50);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("삼성역", "대구역", 20);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isOk());

        // expect
        mockMvc.perform(get("/sections/{lineId}", lineId))
                .andExpect(jsonPath("$[0].startStationName").value("잠실역"))
                .andExpect(jsonPath("$[0].endStationName").value("삼성역"))
                .andExpect(jsonPath("$[0].distance").value(50))
                .andExpect(jsonPath("$[1].startStationName").value("삼성역"))
                .andExpect(jsonPath("$[1].endStationName").value("대구역"))
                .andExpect(jsonPath("$[1].distance").value(20));
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가할 때, 기준이 되는 역이 없다면 추가에 실패한다.")
    void addSection_betweenStation_noStandard_fail() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", 50);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("포항역", "대구역", 20);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("노선의 두 역 사이에 역을 추가할 때, 모든 역이 노선에 존재한다면 추가에 실패한다.")
    void addSection_betweenStation_allInLine_fail() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", 50);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("삼성역", "잠실역", 20);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {50, 51})
    @DisplayName("기존 구간에 역을 추가할 때, 기존 구간보다 길이가 같거나 길다면 추가에 실패한다.")
    void addSection_betweenSection_overExistingSectionDistance(int distance) throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", 50);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // expect
        SectionCreateRequest newSectionAddRequest = new SectionCreateRequest("잠실역", "대구역", distance);
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newSectionAddRequest)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("노선에 구간을 추가할 때, 구간의 길이가 0이하라면 추가에 실패한다.")
    void addSection_notPositiveDistance_fail(int distance) throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "삼성역", distance);

        // expect
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("노선에 역을 추가할 때, 존재하지 않는 역이면 추가에 실패한다. ")
    void addSection_notExistsStation_fail() throws Exception {
        // given
        SectionCreateRequest request = new SectionCreateRequest("장승배기역", "상도역", 10);

        // expect
        mockMvc.perform(post("/sections/{lineId}", lineId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
