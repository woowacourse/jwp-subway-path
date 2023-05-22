package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Direction;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.exception.NoSuchShortestPathException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PathTest {

    @DisplayName("경로가 있는 경우 햐당 경로를 반환한다.")
    @Test
    void get_path() {
        //given
        final int distance1 = 10;
        final int distance2 = 20;
        final int expectDistance = distance1 + distance2;
        Station station1 = new Station(1L, "1");
        Station station2 = new Station(2L, "2");
        Station station3 = new Station(3L, "3");
        Line line = new Line("1", "#123456");
        line.initStations(station1, station2, distance1);
        line.addStation(station3, station2, Direction.RIGHT, distance2);

        //when
        StationGraph path = new ShortestStationGraph(List.of(line), List.of(station1, station2, station3));
        ShortestPath shortestPath = path.getShortestPath(station1, station3);

        //then
        assertEquals(expectDistance, shortestPath.getDistance());
        assertEquals(station1, shortestPath.getPath().get(0));
        assertEquals(station3, shortestPath.getPath().get(shortestPath.getPath().size() - 1));
    }

    @DisplayName("환승하는 경로가 있는 경우 해당 경로를 반환한다.")
    @Test
    void transfer_path() {
        final int extraDistance = 987654321;
        final int distance1 = 10;
        final int distance2 = 20;
        final int expectDistance = distance1 + distance2;
        Station station1 = new Station(1L, "1");
        Station station2 = new Station(2L, "2");
        Station station3 = new Station(3L, "3");
        Line line1 = new Line("1", "#123456");
        line1.initStations(station1, station2, distance1);
        line1.addStation(station3, station2, Direction.RIGHT, extraDistance);

        Station station4 = new Station(4L, "4");
        Station station5 = new Station(5L, "5");
        Line line2 = new Line("2", "#123456");
        line2.initStations(station4, station2, extraDistance);
        line2.addStation(station5, station2, Direction.RIGHT, distance2);

        //when
        StationGraph path = new ShortestStationGraph(List.of(line1, line2), List.of(station1, station2, station3, station4, station5));
        ShortestPath shortestPath = path.getShortestPath(station1, station5);

        //then
        assertEquals(expectDistance, shortestPath.getDistance());
        assertEquals(station1, shortestPath.getPath().get(0));
        assertEquals(station5, shortestPath.getPath().get(shortestPath.getPath().size() - 1));
    }

    @DisplayName("최단경로가 없는 경우 예외를 던진다.")
    @Test
    void not_exist_shortest_path() {
        final int extraDistance = 987654321;
        Station station1 = new Station(1L, "1");
        Station station2 = new Station(2L, "2");
        Station station3 = new Station(3L, "3");
        Line line1 = new Line("1", "#123456");
        line1.initStations(station1, station2, extraDistance);
        line1.addStation(station3, station2, Direction.RIGHT, extraDistance);

        Station station4 = new Station(4L, "4");
        Station station5 = new Station(5L, "5");
        Station station6 = new Station(6L, "6");
        Line line2 = new Line("2", "#123456");
        line2.initStations(station4, station5, extraDistance);
        line2.addStation(station6, station5, Direction.RIGHT, extraDistance);

        //when
        ShortestStationGraph path = new ShortestStationGraph(List.of(line1, line2), List.of(station1, station2, station3, station4, station5, station6));

        //then
        assertThrows(NoSuchShortestPathException.class, () -> path.getShortestPath(station1, station5));
    }

    @DisplayName("환승하는 최단경로가 있는 경우 해당 경로를 반환한다.")
    @Test
    void transfer_shortest_path() {
        final int extraDistance = 987654321;
        final int distance1 = 10;
        final int distance2 = 20;
        final int expectDistance = distance1 + distance2;

        Station station1 = new Station(1L, "1");
        Station station2 = new Station(2L, "2");
        Station station3 = new Station(3L, "3");

        Line line1 = new Line("1", "#123456");
        line1.initStations(station1, station2, distance1);
        line1.addStation(station3, station2, Direction.RIGHT, extraDistance);

        Line line2 = new Line("2", "#123456");
        line2.initStations(station2, station3, distance2);

        //when
        StationGraph path = new ShortestStationGraph(List.of(line1, line2), List.of(station1, station2, station3));
        ShortestPath shortestPath = path.getShortestPath(station1, station3);

        //then
        assertEquals(expectDistance, shortestPath.getDistance());
        assertEquals(station1, shortestPath.getPath().get(0));
        assertEquals(station3, shortestPath.getPath().get(shortestPath.getPath().size() - 1));
    }
}
