package subway.route.jgrapht;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.route.application.dto.request.PathRequestDto;
import subway.route.application.dto.response.PathResponseDto;
import subway.route.domain.InterStationEdge;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("경로 계산기는")
class JgraphtPathCalculatorTest {

    @Test
    void 경로를_계산한다() {
        // given
        JgraphtPathCalculator jgraphtPathCalculator = new JgraphtPathCalculator();
        PathRequestDto pathRequest = new PathRequestDto(1L, 2L,
                List.of(new InterStationEdge(1L, 2L, 3L, 4L)));

        // when
        var result = jgraphtPathCalculator.calculatePath(pathRequest);

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(new PathResponseDto(3, List.of(new InterStationEdge(1L, 2L, 3L, 4L))));
    }
}
