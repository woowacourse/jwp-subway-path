package subway.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.Direction.DOWN;
import static subway.domain.Direction.UP;

class LineTest {

    @DisplayName("역을 순서대로 정렬한다.")
    @Test
    void sortTest() {
        final Line line = new Line(1L, "1호선", "파랑", Map.of(
                new Station(1L, "성남"),
                new Path(new Station(3L, "성대"), 5)
                , new Station(3L, "성대"),
                new Path(new Station(2L, "강남"), 10)));

        final List<Station> stations = line.sortStations();

        assertThat(stations).map(Station::getName).containsExactly("성남", "성대", "강남");
    }

    @DisplayName("경로가 없을 때 정렬하면 빈 리스트를 반환한다.")
    @Test
    void sortEmptyTest() {
        final Line line = new Line(1L, "1호선", "blue", Map.of());

        final List<Station> stations = line.sortStations();

        assertThat(stations).isEmpty();
    }

    @DisplayName("역을 추가한다.")
    @Test
    void addTest() {
        //given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");
        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationA, new Path(stationB, 5)
                        , stationB, new Path(stationC, 10)
                ))
        );
        final Station newStation = new Station(4L, "D");

        //when
        line.addPath(stationA, newStation, 3, DOWN);
        final Path pathAfterStationA = line.getPaths().get(stationA);
        final Path pathAfterNewStation = line.getPaths().get(newStation);

        //then
        assertAll(
                () -> assertThat(pathAfterStationA.getNext()).isEqualTo(newStation),
                () -> assertThat(pathAfterStationA.getDistance()).isEqualTo(3),
                () -> assertThat(pathAfterNewStation.getDistance()).isEqualTo(2)
        );
    }

    @DisplayName("역을 추가한다.")
    @Test
    void addTest2() {
        //given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");
        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationA, new Path(stationB, 5)
                        , stationB, new Path(stationC, 10)
                ))
        );
        final Station newStation = new Station(4L, "D");

        //when
        line.addPath(stationA, newStation, 3, UP);
        final Station stationAfterNew = line.getPaths().get(newStation).getNext();

        //then
        assertThat(stationAfterNew).isEqualTo(stationA);
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void removeStationTest() {
        //given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");
        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationA, new Path(stationB, 5)
                        , stationB, new Path(stationC, 10)
                ))
        );

        //when
        line.removeStation(stationB);

        //then
        Assertions.assertAll(
                () -> assertThat(line.getPaths().get(stationA).getDistance()).isEqualTo(15),
                () -> assertThat(line.getPaths().get(stationA).getNext()).isEqualTo(stationC)
        );
    }

    @DisplayName("역을 추가할 때 거리를 초과하면 예외를 던진다.")
    @Test
    void addStationToPathException() {
        //given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");
        final Station stationD = new Station(4L, "D");
        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationA, new Path(stationB, 5)
                        , stationB, new Path(stationC, 10)
                ))
        );

        //when,then
        assertThatThrownBy(() -> line.addPath(stationA, stationD, 6, DOWN))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
