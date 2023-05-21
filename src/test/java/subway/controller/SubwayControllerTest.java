package subway.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.dto.StationResponse;
import subway.controller.dto.SubwayShortestPathResponse;
import subway.service.SubwayService;

@WebMvcTest(SubwayController.class)
class SubwayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubwayService subwayService;

    @Nested
    @DisplayName("두 역 사이의 최단 경로 조회시 ")
    class FindShortestPath {

        @Test
        @DisplayName("유효한 정보라면 최단 경로 정보를 응답한다.")
        void findShortestPath() throws Exception {
            final List<StationResponse> stations = List.of(new StationResponse(1L, "잠실역"),
                    new StationResponse(2L, "사당역"), new StationResponse(3L, "서울역"));
            final SubwayShortestPathResponse response = new SubwayShortestPathResponse(stations, 7, 800);
            given(subwayService.findShortestPath(1L, 3L, 12))
                    .willReturn(response);

            mockMvc.perform(get("/subways/shortest-path")
                            .queryParam("sourceStationId", String.valueOf(1L))
                            .queryParam("destinationStationId", String.valueOf(3L))
                            .queryParam("passengerAge", String.valueOf(12)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.stations", hasSize(3)))
                    .andExpect(jsonPath("$.stations[0].id").value(1))
                    .andExpect(jsonPath("$.stations[0].name").value("잠실역"))
                    .andExpect(jsonPath("$.stations[1].id").value(2))
                    .andExpect(jsonPath("$.stations[1].name").value("사당역"))
                    .andExpect(jsonPath("$.stations[2].id").value(3))
                    .andExpect(jsonPath("$.stations[2].name").value("서울역"))
                    .andExpect(jsonPath("$.distance").value(7))
                    .andExpect(jsonPath("$.fare").value(800));
        }

        @ParameterizedTest
        @DisplayName("경로를 조회할 역 ID가 비어있을 경우 400 상태를 응답한다.")
        @CsvSource(value = {"1:", "3:"}, delimiter = ':')
        void findShortestPathWithoutStationId(Long sourceStationId, Long destinationStationId) throws Exception {
            mockMvc.perform(get("/subways/shortest-path")
                            .queryParam("sourceStationId", String.valueOf(sourceStationId))
                            .queryParam("destinationStationId", String.valueOf(destinationStationId)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
