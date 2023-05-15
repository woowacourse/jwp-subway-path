package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        List<StationEdge> beforeEdges = new ArrayList<>(line.getStationEdges());
        StationEdge adjacentStationEdge = line.getStationEdges().stream()
                .filter(stationEdge -> stationEdge.getDownStationId().equals(stationId2)).findFirst().get();

        //when
        Long newStationId = 3L;
        line.addStationUpperFrom(newStationId, stationId2, 2);

        List<StationEdge> afterEdges = new ArrayList<>(line.getStationEdges());


        Optional<StationEdge> addedEdge = afterEdges.stream()
                .filter(stationEdge -> stationEdge.getDownStationId().equals(newStationId))
                .findFirst();

        //then
        assertSoftly(softly -> {
            softly.assertThat(addedEdge).isNotEmpty();
            softly.assertThat(beforeEdges.indexOf(adjacentStationEdge)).isEqualTo(afterEdges.indexOf(addedEdge.get()));
        });
    }

    @Test
    @DisplayName("하행 방향으로 역을 추가한다.")
    void insertDownStation() {
        //given
        Line line = createLine();
        List<StationEdge> beforeEdges = new ArrayList<>(line.getStationEdges());
        StationEdge adjacentStationEdge = line.getStationEdges().stream()
                .filter(stationEdge -> stationEdge.getDownStationId().equals(stationId1)).findFirst().get();

        //when
        Long newStationId = 3L;
        line.addStationDownFrom(newStationId, stationId1, 2);

        //then
        List<StationEdge> afterEdges = new ArrayList<>(line.getStationEdges());
        Optional<StationEdge> addedEdge = afterEdges.stream()
                .filter(stationEdge -> stationEdge.getDownStationId().equals(newStationId))
                .findFirst();
        assertSoftly(softly -> {
            softly.assertThat(addedEdge).isNotEmpty();
            softly.assertThat(beforeEdges.indexOf(adjacentStationEdge)+1).isEqualTo(afterEdges.indexOf(addedEdge.get()));
        });
    }

    private Line createLine() {
        Line line = Line.of(name, color, stationId1, stationId2, distance);
        return line;
    }

    @Test
    @DisplayName("하행 종점을 추가한다.")
    void insertDownEndStation() {
        //given
        Line line = createLine();

        //when
        Long newStationId = 3L;
        line.addStationDownFrom(newStationId, stationId2, 2);

        //then
        List<StationEdge> edgesAfterAddition = line.getStationEdges();
        assertSoftly(softly -> {
            softly.assertThat(edgesAfterAddition.get(2).getDistance()).isEqualTo(2);
            softly.assertThat(edgesAfterAddition).hasSize(3);
        });
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given
        final Line line = createLine();
        line.addStationDownFrom(3L, stationId1, 2);

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

    @Test
    @DisplayName("역이 2개 이상이면 삭제 가능하다.")
    void canDeleteStation() {
        //given
        final Line lineWithTwoStation = createLine();
        lineWithTwoStation.addStationDownFrom(3L, stationId1, 2);
        final Line lineWithThreeStation = lineWithTwoStation;

        //when
        boolean canDeleteStation = lineWithThreeStation.canDeleteStation();

        //then
        assertThat(canDeleteStation).isTrue();
    }

    @Test
    @DisplayName("역이 2개이면 삭제가 불가능하다.")
    void cannotDeleteStation() {
        //given
        final Line lineWithTwoStation = createLine();

        //when
        boolean canDeleteStation = lineWithTwoStation.canDeleteStation();

        //then
        assertThat(canDeleteStation).isFalse();
    }

}