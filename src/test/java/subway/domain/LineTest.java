package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.Line.UP_END_EDGE_DISTANCE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {
    @Test
    @DisplayName("역 두 개와 거리로 생성한다.")
    void create() {
        //given
        Long stationId = 1L;
        Long stationId2 = 2L;
        int distance = 5;
        String name = "2호선";
        String color = "초록색";

        StationEdges stationEdges = StationEdges.from(
                List.of(new StationEdge(stationId, UP_END_EDGE_DISTANCE),
                        new StationEdge(stationId2, distance))
        );

        //when
        final var line = new Line(1L, name, color, stationEdges);

        //then
        assertThat(line)
                .isInstanceOf(Line.class)
                .isNotNull();
    }
}