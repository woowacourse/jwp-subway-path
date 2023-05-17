package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.service.LineService;
import subway.business.service.dto.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        given(lineService.createLine(any())).willReturn(new LineResponse(1L, "2호선"));

        LineSaveRequest lineSaveRequest = new LineSaveRequest(
                "2호선",
                1L,
                2L,
                10
        );
        String jsonRequest = objectMapper.writeValueAsString(lineSaveRequest);

        mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isCreated());
    }

    @DisplayName("노선에 역 한개를 등록한다.")
    @Test
    void shouldAddStationToLineWhenRequest() throws Exception {
        StationAddToLineRequest stationAddToLineRequest = new StationAddToLineRequest(
                3L,
                2L,
                "상행",
                3
        );
        String jsonRequest = objectMapper.writeValueAsString(stationAddToLineRequest);

        mockMvc.perform(post("/lines/1/station")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isOk());
    }

    @DisplayName("노선에 포함된 역 한 개를 제외한다.")
    @Test
    void shouldRemoveStationFromLineWhenRequest() throws Exception {
        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(1L);
        String jsonRequest = objectMapper.writeValueAsString(stationDeleteRequest);

        mockMvc.perform(delete("/lines/1/station")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @DisplayName("노선의 이름과 모든 역의 이름을 반환한다.")
    @Test
    void shouldReturnLineNameAndAllStationsOfLineWhenRequest() throws Exception {
        given(lineService.findLineResponseById(any())).willReturn(new LineStationsResponse(
                "2호선",
                List.of("강남역", "잠실역")
        ));

        mockMvc.perform(get("/lines/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2호선"))
                .andExpect(jsonPath("$.stations[0]").value("강남역"))
                .andExpect(jsonPath("$.stations[1]").value("잠실역"));
    }

    @DisplayName("모든 노선의 이름과 모든 역의 이름을 반환한다.")
    @Test
    void shouldReturnAllLineNameAndAllStationsOfLineWhenRequest() throws Exception {
        given(lineService.findLineResponses()).willReturn(List.of(
                new LineStationsResponse(
                        "2호선",
                        List.of("몽촌토성역", "잠실역")
                ),
                new LineStationsResponse(
                        "1호선",
                        List.of("인천역", "부평역")
                )
        ));

        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("2호선"))
                .andExpect(jsonPath("$[0].stations[0]").value("몽촌토성역"))
                .andExpect(jsonPath("$[0].stations[1]").value("잠실역"));
    }
}
