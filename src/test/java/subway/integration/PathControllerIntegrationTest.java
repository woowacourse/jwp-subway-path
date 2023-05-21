package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import subway.ui.dto.line.LineCreateRequest;
import subway.ui.dto.path.PathFindRequest;
import subway.ui.dto.section.SectionCreateRequest;
import subway.ui.dto.station.StationCreateRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/line_initialize.sql")
class PathControllerIntegrationTest {
    private static final String BASE_URL = "/paths";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    

    @Test
    @DisplayName("요청한 출발역에서 도착역까지의 최단경로와 거리, 비용을 조회할 수 있다.")
    void findPath_success() throws Exception {
        // given
        PathFindRequest request = new PathFindRequest("양재역", "서초역");

        // expect
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pathStations.size()").value(4))
                .andExpect(jsonPath("$.distance").value(30))
                .andExpect(jsonPath("$.fare").value(1250 + 400));
    }

    @ParameterizedTest
    @CsvSource(value = {" :교대역", ":", "교대역:"}, delimiter = ':')
    @DisplayName("출발역 또는 도착역이 비어있다면 예외가 발생한다.")
    void findPath_emptyRequest_fail(String startStationName, String endStationName) throws Exception {
        // given
        PathFindRequest request = new PathFindRequest(startStationName, endStationName);

        // expect
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("출발역 또는 도착역이 존재하지 않는 역이라면 예외가 발생한다.")
    void findPath_stationNotFound_fail() throws Exception {
        // given
        PathFindRequest request = new PathFindRequest("신촌역", "교대역");

        // expect
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("출발역 또는 도착역이 노선 상에 존재하지 않는다면 예외가 발생한다.")
    void findPath_stationNotInLine_fail() throws Exception {
        // given
        addNewStationNotInLine("신촌역");
        PathFindRequest request = new PathFindRequest("신촌역", "교대역");

        // expect
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("출발역에서 도착역으로 갈 수 있는 경로가 없다면 예외가 발생한다.")
    void findPath_noPath_fail() throws Exception {
        // given
        addNewStationInNewLineNotConnected("정자역", "미금역");
        PathFindRequest request = new PathFindRequest("교대역", "미금역");

        // expect
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private void addNewStationNotInLine(String name) throws Exception {
        StationCreateRequest request = new StationCreateRequest(name);

        mockMvc.perform(post("/stations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));
    }

    private void addNewStationInNewLineNotConnected(String stationName1, String stationName2) throws Exception {
        addNewStationNotInLine(stationName1);
        addNewStationNotInLine(stationName2);

        LineCreateRequest lineCreateRequest = new LineCreateRequest("분당선", "yellow");
        mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(lineCreateRequest)));

        SectionCreateRequest sectionCreateInNewLineRequest = new SectionCreateRequest(stationName1, stationName2, 3);
        mockMvc.perform(post("/lines/4/sections")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(sectionCreateInNewLineRequest)));
    }

}