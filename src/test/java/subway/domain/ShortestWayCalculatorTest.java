package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.util.ShortestWayCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShortestWayCalculatorTest {

    @DisplayName("최단 거리를 계산한다.")
    @Test
    void computeShortestPath() {
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
        final ShortestWay result = ShortestWayCalculator.calculate(stationA, stationC, of(line));
        final double distance = result.getDistance();

        //then
        assertThat(distance).isEqualTo(15);
    }

    @DisplayName("최단 경로를 계산한다.")
    @Test
    void computeShortestPath2() {
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
        final ShortestWay result = ShortestWayCalculator.calculate(stationA, stationC, of(line));
        final List<Station> way = result.getStations();

        //then
        assertThat(way).containsExactly(stationA, stationB, stationC);
    }

    @DisplayName("환승을 포함한 최단 거리를 계산한다.")
    @Test
    void computeShortestPath3() {
        //given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");
        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationA, new Path(stationB, 10000)
                        , stationB, new Path(stationC, 10)
                ))
        );
        final Line line2 = new Line(2L, "3호선", "검정",
                new HashMap<>(Map.of(
                        stationC, new Path(stationA, 5)
                ))
        );

        //when
        final ShortestWay result = ShortestWayCalculator.calculate(stationA, stationC, of(line, line2));
        final double distance = result.getDistance();

        //then
        assertThat(distance).isEqualTo(5);
    }

    @DisplayName("최단 거리를 계산할 때 경로가 없다면 예외를 던진다.")
    @Test
    void computeShortestPathException() {
        //given
        final Station stationA = new Station(1L, "A");
        final Station stationB = new Station(3L, "B");
        final Station stationC = new Station(2L, "C");
        final Station stationD = new Station(4L, "D");
        final Line line = new Line(1L, "1호선", "파랑",
                new HashMap<>(Map.of(
                        stationB, new Path(stationC, 10)
                ))
        );
        final Line line2 = new Line(2L, "3호선", "검정",
                new HashMap<>(Map.of(
                        stationA, new Path(stationD, 5)
                ))
        );

        //when,then
        assertThatThrownBy(() -> ShortestWayCalculator.calculate(stationB, stationD, of(line, line2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
