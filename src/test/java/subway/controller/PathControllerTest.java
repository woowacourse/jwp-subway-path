package subway.controller;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.service.PathService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.fixture.StationFixture.*;

@WebMvcTest(PathController.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @Test
    void 두_역의_id를_통해서_최단_노선을_조회한다() throws Exception {
        final List<StationResponse> stationResponses = Stream.of(
                        EXPRESS_BUS_TERMINAL_STATION,
                        NEW_STATION,
                        SAPYEONG_STATION)
                .map(StationResponse::from)
                .collect(Collectors.toList());

        final int price = 5;
        final PathResponse pathResponse = new PathResponse(stationResponses, price);

        final Long sourceId = EXPRESS_BUS_TERMINAL_STATION.getId();
        final Long targetId = SAPYEONG_STATION.getId();
        when(pathService.findShortestPath(sourceId, targetId, 19))
                .thenReturn(pathResponse);

        mockMvc.perform(get("/paths?source={sourceId}&target={targetId}", sourceId, targetId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations[0].id").value(EXPRESS_BUS_TERMINAL_STATION.getId()))
                .andExpect(jsonPath("$.stations[0].name").value(EXPRESS_BUS_TERMINAL_STATION.getName()))
                .andExpect(jsonPath("$.stations[1].id").value(NEW_STATION.getId()))
                .andExpect(jsonPath("$.stations[1].name").value(NEW_STATION.getName()))
                .andExpect(jsonPath("$.stations[2].id").value(SAPYEONG_STATION.getId()))
                .andExpect(jsonPath("$.stations[2].name").value(SAPYEONG_STATION.getName()))
                .andExpect(jsonPath("$.price").value(price));
    }
}
