package subway.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.application.PathService;
import subway.application.dto.ShortestPathInfoDto;
import subway.application.dto.ShortestPathsDto;
import subway.domain.fare.FareAmount;
import subway.domain.line.Line;
import subway.domain.path.graph.PathEdge;
import subway.domain.path.graph.PathEdges;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.station.Station;
import subway.exception.GlobalExceptionHandler;

@WebMvcTest(controllers = PathController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class PathGraphControllerTest {

    @MockBean
    PathService pathService;

    @Autowired
    PathController pathController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pathController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();
    }

    @Test
    void findShortestPathAndFare_메소드는_출발_역과_도착_역의_id를_전달하면_최단_경로와_요금을_반환한다()
            throws Exception {
        final Station first = Station.of(1L, "1역");
        final Station second = Station.of(2L, "2역");
        final Line line = Line.of(1L, "1호선", "bg-red-500");
        line.createSection(first, second, Distance.from(5), Direction.DOWN);
        final PathEdge pathEdge = PathEdge.of(first, second, line);
        final PathEdges pathEdges = PathEdges.create();
        pathEdges.add(pathEdge);
        final ShortestPathsDto shortestPathsDto = ShortestPathsDto.from(List.of(pathEdges));
        final FareAmount fareAmount = FareAmount.from(1250);
        final ShortestPathInfoDto shortestPathInfoDto = ShortestPathInfoDto.of(shortestPathsDto, fareAmount);
        given(pathService.findShortestPathInfo(anyLong(), anyLong())).willReturn(shortestPathInfoDto);

        mockMvc.perform(get("/paths/shortest/{sourceStationId}/{targetStationId}", 1, 2))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.paths[0].stations[0].id", is(1)),
                        jsonPath("$.paths[0].stations[0].name", is("1역")),
                        jsonPath("$.paths[0].stations[1].id", is(2)),
                        jsonPath("$.paths[0].stations[1].name", is("2역")),
                        jsonPath("$.paths[0].pathDistance", is(5)),
                        jsonPath("$.totalDistance", is(5)),
                        jsonPath("$.fare", is(1250))
                );
    }
}
