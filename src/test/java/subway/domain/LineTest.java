package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
        final var line = Line.of(name, color, stationId, stationId2, distance);

        //then
        assertThat(line)
                .isInstanceOf(Line.class)
                .isNotNull();
    }

    @ParameterizedTest(name = "역 추가 방향 : {1}")
    @DisplayName("노선에 역을 추가한다")
    @CsvSource(value = {"2:UP:3:2", "1:DOWN:2:3"}, delimiter = ':')
    void insertStation(
            final Long adjacentStationId,
            final LineDirection lineDirection,
            final int expectedMiddleEdgeDistance,
            final int expectedDownEndEdgeDistance
    ) {
        //given
        Line line = createLine();

        //when
        Long newStationId = 3L;
        line.insertStation(newStationId, adjacentStationId, lineDirection, 2);
        final StationEdge middle = line.getStationEdges().get(1);
        final StationEdge downEnd = line.getStationEdges().get(2);

        //then
        assertSoftly(softly -> {
            softly.assertThat(middle.getDistance()).isEqualTo(expectedMiddleEdgeDistance);
            softly.assertThat(downEnd.getDistance()).isEqualTo(expectedDownEndEdgeDistance);
        });
    }

    private Line createLine() {
        return Line.of(name, color, stationId1, stationId2, distance);
    }

    @Test
    @DisplayName("하행 종점을 추가한다.")
    void insertDownEndStation() {
        //given
        Line line = createLine();

        //when
        Long newStationId = 3L;
        line.insertStation(newStationId, stationId2, LineDirection.DOWN, 2);
        final StationEdge downEnd = line.getStationEdges().get(2);
        //then
        assertThat(downEnd.getDistance()).isEqualTo(2);
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given
        final Line line = createLine();
        line.insertStation(3L, stationId1, LineDirection.DOWN, 2);

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
        assertThat(contains).isTrue();
    }
}
