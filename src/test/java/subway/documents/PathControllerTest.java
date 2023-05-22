package subway.documents;

import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.dto.ShortestPathResponse;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
public class PathControllerTest extends DocumentationSteps {

    @Test
    void 가장_짧은_경로를_찾는다() throws Exception {
        // given
        final Long start = 1L;
        final Long end = 4L;

        final List<Station> stations = List.of(
                new Station(start, "잠실역"),
                new Station(2L, "잠실새내역"),
                new Station(3L, "종합운동장역"),
                new Station(end, "삼성역"));
        final ShortestPath shortestPath = new ShortestPath(stations, Distance.from(25));
        final ShortestPathResponse pathResponse = ShortestPathResponse.from(shortestPath, Fare.from(1250));

        given(pathService.findShortestPath(start, end)).willReturn(pathResponse);

        // when, then
        mockMvc.perform(get("/path")
                        .param("start", String.valueOf(start))
                        .param("end", String.valueOf(end)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pathResponse)))
                .andDo(document("paths"));
    }
}
