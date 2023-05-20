package subway.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.service.PathService;
import subway.business.service.dto.ShortestPathResponse;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
public class PathControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @DisplayName("역과 역 사이의 최단 경로 정보를 가져온다.")
    @Test
    void shouldReturnShortestPathResponseWhenRequestSourceStationIdAndDestStationId() throws Exception {
        ShortestPathResponse shortestPathResponse = new ShortestPathResponse(
                List.of("강남역", "역삼역"),
                10,
                1250);
        given(pathService.getShortestPath(1L, 2L)).willReturn(shortestPathResponse);

        mockMvc.perform(get("/path?sourceStationId=1&destStationId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNamesOfShortestPath[0]").value("강남역"))
                .andExpect(jsonPath("$.stationNamesOfShortestPath[1]").value("역삼역"))
                .andExpect(jsonPath("$.totalDistance").value(10))
                .andExpect(jsonPath("$.totalFare").value(1250));

    }
}
