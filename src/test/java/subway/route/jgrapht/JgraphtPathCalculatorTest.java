package subway.route.jgrapht;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.route.domain.InterStationEdge;
import subway.route.dto.request.PathRequest;
import subway.route.dto.response.PathResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("경로 계산기는")
class JgraphtPathCalculatorTest {

    @Test
    void 경로를_계산한다() {
        // given
        JgraphtPathCalculator jgraphtPathCalculator = new JgraphtPathCalculator();
        PathRequest pathRequest = new PathRequest(1L, 2L,
                List.of(new InterStationEdge(1L, 2L, 3L, 4L)));

        // when
        var result = jgraphtPathCalculator.calculatePath(pathRequest);

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(new PathResponse(3, List.of(new InterStationEdge(1L, 2L, 3L, 4L))));
    }
}
