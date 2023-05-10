package subway.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.dto.InsertionResult;

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

        //when
        final var line = Line.of(name, color,
                List.of(new StationEdge(stationId, 0), new StationEdge(stationId2, distance)));

        //then
        Assertions.assertThat(line)
                .isInstanceOf(Line.class)
                .isNotNull();
    }

    @ParameterizedTest(name = "노선에 역을 등록한다.")
    @MethodSource("provideInsertedStation")
    void insertStation(Long adjacentStationId, LineDirection direction, int expectedInsertEdgeDistance,
                       int expectedUpdatedEdgeDistance) {
        //given
        Long stationId = 1L;
        Long stationId2 = 2L;
        int distance = 5;
        String name = "2호선";
        String color = "초록색";

        Line line = Line.of(name, color, List.of(new StationEdge(stationId, 0), new StationEdge(stationId2, distance)));

        //when
        Long newStationId = 3L;
        InsertionResult changedEdges = line.insertStation(newStationId, adjacentStationId, 2, direction);
        //then
        assertSoftly(softly -> {
            softly.assertThat(changedEdges.getInsertedEdge().getDistance()).isEqualTo(expectedInsertEdgeDistance);
            softly.assertThat(changedEdges.getUpdatedEdge().getDistance()).isEqualTo(expectedUpdatedEdgeDistance);
        });
    }

    private static Stream<Arguments> provideInsertedStation() {
        return Stream.of(
                Arguments.arguments(1L, LineDirection.DOWN, 3, 2),
                Arguments.arguments(1L, LineDirection.UP, 0, 2)
        );
    }

    @Test
    @DisplayName("하행 종점을 추가한다.")
    void insertDownEndStation() {
        //given
        Long stationId = 1L;
        Long stationId2 = 2L;
        int distance = 5;
        String name = "2호선";
        String color = "초록색";

        Line line = Line.of(name, color, List.of(new StationEdge(stationId, 0), new StationEdge(stationId2, distance)));

        //when
        Long newStationId = 3L;
        InsertionResult changedEdges = line.insertStation(newStationId, stationId2, 2, LineDirection.DOWN);
        //then
        assertSoftly(softly -> {
            softly.assertThat(changedEdges.getInsertedEdge().getDistance()).isEqualTo(2);
            softly.assertThat(changedEdges.getUpdatedEdge()).isNull();
        });
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given
        Long stationId = 1L;
        Long stationId2 = 2L;
        int distance = 5;
        String name = "2호선";
        String color = "초록색";
        final Line line = Line.of(name, color,
                List.of(new StationEdge(stationId, 0), new StationEdge(stationId2, distance)));
        line.insertStation(3L, stationId, 2, LineDirection.DOWN);

        //when
        StationEdge changedStationEdge = line.deleteStation(3L);

        //then
        assertSoftly(softly -> {
            softly.assertThat(changedStationEdge.getDownStationId()).isEqualTo(stationId2);
            softly.assertThat(changedStationEdge.getDistance()).isEqualTo(distance);
        });
    }
}