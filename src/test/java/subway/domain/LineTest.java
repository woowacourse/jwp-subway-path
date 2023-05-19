package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.line.Line;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("두 역 사이의 거리를 얻는다.")
    void get_station_edge_test() {
        // given
        final Line line = createLine();

        // when
        final int actualDistance = line.getEdgeDistanceBetween(stationId2, stationId1);

        // then
        assertThat(actualDistance).isEqualTo(distance);
    }

    private Line createLine() {
        return Line.of(name, color, stationId1, stationId2, distance);
    }

    @ParameterizedTest(name = "역 추가 방향 : {1}")
    @DisplayName("노선에 역을 추가한다")
    @CsvSource(value = {"2:UP", "1:DOWN"}, delimiter = ':')
    void insertStation(
            final Long adjacentStationId,
            final LineDirection lineDirection
    ) {
        //given
        Line line = createLine();

        //when
        Long newStationId = 3L;
        line.insertStation(newStationId, adjacentStationId, lineDirection, 2);

        //then
        final List<Long> stationIdsByOrder = line.getStationIdsByOrder();
        assertThat(stationIdsByOrder).containsExactly(1L, 3L, 2L);
    }

    @Test
    @DisplayName("하행 종점을 추가한다.")
    void insertDownEndStation() {
        //given
        Line line = createLine();

        //when
        Long newStationId = 3L;
        line.insertStation(newStationId, stationId2, LineDirection.DOWN, 2);

        //then
        final Long downEndStationId = line.getStationEdges().findDownEndStationId();
        assertThat(downEndStationId).isEqualTo(3L);
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given
        final Line line = createLine();
        line.insertStation(3L, stationId1, LineDirection.DOWN, 2);

        //when
        line.removeStation(2L);

        //then
        final List<Long> stationIdsByOrder = line.getStationIdsByOrder();
        assertThat(stationIdsByOrder).containsExactly(1L, 3L);
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
