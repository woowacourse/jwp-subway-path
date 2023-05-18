package subway.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.LineRouteService;
import subway.dto.LineResponse;
import subway.dto.LineRouteResponse;
import subway.dto.StationResponse;

@WebMvcTest(LineRouteController.class)
class LineRouteControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private LineRouteService lineRouteService;
    
    @Test
    @DisplayName("모든 호선 조회 테스트")
    void findAllLinesMvcTest() throws Exception {
        // given
        final LineRouteResponse lineRouteResponse = new LineRouteResponse(new LineResponse(1L, "2호선", "초록색"),
                List.of(new StationResponse(1L, "교대역"),
                        new StationResponse(2L, "강남역"),
                        new StationResponse(3L, "역삼역")));
        // when
        when(this.lineRouteService.findAllStations()).thenReturn(List.of(lineRouteResponse));
        
        // then
        this.mockMvc.perform(get("/lines/stations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].line.id").value(1L))
                .andExpect(jsonPath("$[0].line.name").value("2호선"))
                .andExpect(jsonPath("$[0].line.color").value("초록색"))
                .andExpect(jsonPath("$[0].stations[0].id").value(1L))
                .andExpect(jsonPath("$[0].stations[0].name").value("교대역"))
                .andExpect(jsonPath("$[0].stations[1].id").value(2L))
                .andExpect(jsonPath("$[0].stations[1].name").value("강남역"))
                .andExpect(jsonPath("$[0].stations[2].id").value(3L))
                .andExpect(jsonPath("$[0].stations[2].name").value("역삼역"));
    }
    
    @Test
    @DisplayName("호선에 추가된 역 조회 테스트")
    void getStationsMvcTest() throws Exception {
        // given
        final long lineId = 1L;
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "초록색");
        final List<StationResponse> stationResponses = List.of(new StationResponse(1L, "교대역"),
                new StationResponse(2L, "강남역"),
                new StationResponse(3L, "역삼역"));
        final LineRouteResponse lineRouteResponse = new LineRouteResponse(lineResponse, stationResponses);
        // when
        when(this.lineRouteService.findAllStationsInLine(lineId)).thenReturn(lineRouteResponse);
        
        // then
        this.mockMvc.perform(get("/lines/{lineId}/stations", lineId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.line.id").value(1L))
                .andExpect(jsonPath("$.line.name").value("2호선"))
                .andExpect(jsonPath("$.line.color").value("초록색"))
                .andExpect(jsonPath("$.stations[0].id").value(1L))
                .andExpect(jsonPath("$.stations[0].name").value("교대역"))
                .andExpect(jsonPath("$.stations[1].id").value(2L))
                .andExpect(jsonPath("$.stations[1].name").value("강남역"))
                .andExpect(jsonPath("$.stations[2].id").value(3L))
                .andExpect(jsonPath("$.stations[2].name").value("역삼역"));
    }
    
}