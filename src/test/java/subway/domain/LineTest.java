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

    private static final Long stationId1 = 1L;
    private static final Long stationId2 = 2L;
    private static final int distance = 5;
    private static final String name = "2호선";
    private static final String color = "초록색";

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

    @ParameterizedTest(name = "노선에 역을 등록한다. {4}")
    @MethodSource("provideInsertedStation")
    void insertStation(Long adjacentStationId, LineDirection direction, int expectedInsertEdgeDistance,
                       int expectedUpdatedEdgeDistance, String testName) {
        //given
        Line line = createLine();

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
                Arguments.arguments(stationId1, LineDirection.DOWN, 2, 3, "역 중간에 추가"),
                Arguments.arguments(stationId1, LineDirection.UP, 0, 2, "상행 종점 추가")
        );
    }

    private Line createLine() {
        Line line = Line.of(name, color,
                List.of(new StationEdge(stationId1, 0), new StationEdge(stationId2, distance)));
        return line;
    }

    @Test
    @DisplayName("하행 종점을 추가한다.")
    void insertDownEndStation() {
        //given
        Line line = createLine();

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
        final Line line = createLine();
        line.insertStation(3L, stationId1, 2, LineDirection.DOWN);

        //when
        StationEdge changedStationEdge = line.deleteStation(3L);

        //then
        assertSoftly(softly -> {
            softly.assertThat(changedStationEdge.getDownStationId()).isEqualTo(stationId2);
            softly.assertThat(changedStationEdge.getDistance()).isEqualTo(distance);
        });
    }

    @Test
    @DisplayName("노선에 역이 존재하는지 확인한다.")
    void contains() {
        //given
        final Line line = createLine();
        //when
        boolean contains = line.contains(stationId1);
        //then
        Assertions.assertThat(contains).isTrue();
    }
}