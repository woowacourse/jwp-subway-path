package subway.adapter.route.out.find;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.application.route.port.out.find.PathRequestDto;
import subway.application.route.port.out.find.PathResponseDto;
import subway.domain.route.Edges;
import subway.domain.route.InterStationEdge;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("경로 계산기는")
class JgraphtPathCalculatorTest {

    @Test
    void 경로를_계산한다() {
        // given
        final JgraphtPathCalculator jgraphtPathCalculator = new JgraphtPathCalculator();
        final PathRequestDto pathRequest = new PathRequestDto(1L, 2L,
                new Edges(List.of(new InterStationEdge(1L, 2L, 3L, 4L))));

        // when
        final var result = jgraphtPathCalculator.calculatePath(pathRequest);

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(new PathResponseDto(3, List.of(new InterStationEdge(1L, 2L, 3L, 4L))));
    }
}
