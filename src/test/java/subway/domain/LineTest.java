package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("상행 방향으로 역을 추가한다.")
    void insertUpStation() {
        //given
        Line line = createLine();

        //when
        Long newStationId = 3L;
        line.insertUpStation(newStationId, stationId2, 2);
        final StationEdge middle = line.getStationEdges().get(1);
        final StationEdge downEnd = line.getStationEdges().get(2);

        //then
        assertSoftly(softly -> {
            softly.assertThat(middle.getDistance()).isEqualTo(3);
            softly.assertThat(downEnd.getDistance()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("하행 방향으로 역을 추가한다.")
    void insertDownStation() {
        //given
        Line line = createLine();

        //when
        Long newStationId = 3L;
        line.insertDownStation(newStationId, stationId1, 2);
        final StationEdge middle = line.getStationEdges().get(1);
        final StationEdge downEnd = line.getStationEdges().get(2);

        //then
        assertSoftly(softly -> {
            softly.assertThat(middle.getDistance()).isEqualTo(2);
            softly.assertThat(downEnd.getDistance()).isEqualTo(3);
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
        line.insertDownStation(newStationId, stationId2, 2);
        final StationEdge downEnd = line.getStationEdges().get(2);
        //then
        assertThat(downEnd.getDistance()).isEqualTo(2);
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given
        final Line line = createLine();
        line.insertDownStation(3L, stationId1, 2);

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
