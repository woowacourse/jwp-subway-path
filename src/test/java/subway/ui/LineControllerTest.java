package subway.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.fixture.FixtureForLineTest.line2WithOneSection;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.domain.line.Line;
import subway.business.domain.line.Section;
import subway.business.domain.line.Station;
import subway.business.service.LineService;
import subway.business.service.dto.LineResponse;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.StationAddToLineRequest;
import subway.ui.dto.StationDeleteRequest;

@WebMvcTest(LineController.class)
public class LineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @DisplayName("노선과 두 개의 역을 추가한다")
    @Test
    void shouldCreateLineWhenRequest() throws Exception {
        Line line = line2WithOneSection();
        given(lineService.createLine(any())).willReturn(LineResponse.from(line));

        LineSaveRequest lineSaveRequest = new LineSaveRequest(
                "2호선",
                0, "몽촌토성역", "잠실역",
                5
        );
        String jsonRequest = objectMapper.writeValueAsString(lineSaveRequest);

        mockMvc.perform(post("/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.name").value("2호선"))
                .andExpect(jsonPath("$.sections[0].upwardStation.name").value("잠실역"))
                .andExpect(jsonPath("$.sections[0].downwardStation.name").value("몽촌토성역"))
                .andExpect(status().isCreated());
    }

    @DisplayName("노선에 역 한개를 등록한다.")
    @Test
    void shouldAddStationToLineWhenRequest() throws Exception {
        StationAddToLineRequest stationAddToLineRequest = new StationAddToLineRequest(
                "잠실역",
                "몽촌토성역",
                "상행",
                3
        );
        String jsonRequest = objectMapper.writeValueAsString(stationAddToLineRequest);

        mockMvc.perform(post("/lines/1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @DisplayName("노선에 포함된 역 한 개를 제외한다.")
    @Test
    void shouldRemoveStationFromLineWhenRequest() throws Exception {
        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest("잠실역");
        String jsonRequest = objectMapper.writeValueAsString(stationDeleteRequest);

        mockMvc.perform(delete("/lines/1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent());
    }

    @DisplayName("노선의 이름과 모든 역의 이름을 반환한다.")
    @Test
    void shouldReturnLineNameAndAllStationsOfLineWhenRequest() throws Exception {
        Line line = line2WithOneSection();
        given(lineService.findLineResponseById(any())).willReturn(LineResponse.from(line));

        mockMvc.perform(get("/lines/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2호선"))
                .andExpect(jsonPath("$.sections[0].upwardStation.name").value("잠실역"))
                .andExpect(jsonPath("$.sections[0].downwardStation.name").value("몽촌토성역"));
    }

    @DisplayName("모든 노선의 이름과 모든 역의 이름을 반환한다.")
    @Test
    void shouldReturnAllLineNameAndAllStationsOfLineWhenRequest() throws Exception {
        Line line1 = new Line(
                1L,
                "1호선",
                List.of(new Section(1L, Station.from("인천역"), Station.from("부평역"), 5)),
                BigDecimal.valueOf(0));
        Line line2 = new Line(
                2L,
                "2호선",
                List.of(new Section(2L, Station.from("잠실역"), Station.from("몽촌토성역"), 5)),
                BigDecimal.valueOf(0)
        );
        given(lineService.findLineResponses()).willReturn(List.of(LineResponse.from(line1), LineResponse.from(line2)));

        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("1호선"))
                .andExpect(jsonPath("$[0].sections[0].upwardStation.name").value("인천역"))
                .andExpect(jsonPath("$[1].sections[0].downwardStation.name").value("몽촌토성역"));
    }
}
