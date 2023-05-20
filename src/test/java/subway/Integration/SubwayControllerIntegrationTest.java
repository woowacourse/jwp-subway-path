package subway.Integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

class SubwayControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Nested
    @DisplayName("두 역 사이의 최단 경로 조회시 ")
    class FindShortestPath {

        private Station upward;
        private Station middle;
        private Station downward;

        @BeforeEach
        void setUp() {
            final Line lineTwo = lineRepository.save(new Line("2호선", "초록색", 300));
            final Line lineFour = lineRepository.save(new Line("4호선", "하늘색", 400));
            upward = stationRepository.save(new Station("잠실역"));
            middle = stationRepository.save(new Station("사당역"));
            downward = stationRepository.save(new Station("서울역"));
            lineTwo.addSection(upward, middle, 3);
            lineFour.addSection(middle, downward, 4);
            lineRepository.update(lineTwo);
            lineRepository.update(lineFour);
        }

        @Test
        @DisplayName("유효한 정보라면 최단 경로 정보를 응답한다.")
        void findShortestPath() throws Exception {

            mockMvc.perform(get("/subways/shortest-path")
                            .queryParam("sourceStationId", String.valueOf(upward.getId()))
                            .queryParam("destinationStationId", String.valueOf(downward.getId()))
                            .queryParam("passengerAge", String.valueOf(13)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.stations", hasSize(3)))
                    .andExpect(jsonPath("$.stations[0].id").value(upward.getId()))
                    .andExpect(jsonPath("$.stations[0].name").value(upward.getName()))
                    .andExpect(jsonPath("$.stations[1].id").value(middle.getId()))
                    .andExpect(jsonPath("$.stations[1].name").value(middle.getName()))
                    .andExpect(jsonPath("$.stations[2].id").value(downward.getId()))
                    .andExpect(jsonPath("$.stations[2].name").value(downward.getName()))
                    .andExpect(jsonPath("$.distance").value(7))
                    .andExpect(jsonPath("$.fare").value(1390));
        }

        @Test
        @DisplayName("경로를 조회할 역 ID가 비어있을 경우 400 상태를 응답한다.")
        void findShortestPathWithoutStationId() throws Exception {
            mockMvc.perform(get("/subways/shortest-path")
                            .queryParam("sourceStationId", String.valueOf(upward.getId()))
                            .queryParam("destinationStationId", ""))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
