package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineTest {

    @Test
    @DisplayName("라인을 처음에 이름과 색상으로 생성합니다.")
    void validate_stations_sections_size() {
        // given
        String lineName = "2";
        String colorCode = "#FFFFFF";
        Line line = new Line(lineName, colorCode);

        // when + then
        assertEquals(lineName, line.getName());
        assertEquals(colorCode, line.getColor());
    }

    @Test
    @DisplayName("역에 두개의 역을 등록해 역을 초기화합니다.")
    void init_line_success() {
        // given
        String lineName = "2";
        String colorCode = "#FFFFFF";
        Line line = new Line(lineName, colorCode);
        Station leftStation = new Station("잠실역");
        Station rightStation = new Station("삼성역");

        // when
        line.initStations(leftStation, rightStation, 10);

        // then
        assertEquals(lineName, line.getName());
        assertEquals(colorCode, line.getColor());
        assertEquals(2, line.getStations().size());
    }

    @Test
    @DisplayName("노선의 오른쪽에 종점을 추가합니다.")
    void add_right_last_station() {
        // given
        String lineName = "2";
        String colorCode = "#FFFFFF";
        Line line = new Line(lineName, colorCode);
        Station leftStation = new Station(1L, "left");
        Station rightStation = new Station(2L, "right");
        Station lastStation = new Station(3L, "last");
        line.initStations(leftStation, rightStation, 10);

        // when
        line.addStation(lastStation, rightStation, Direction.RIGHT, 5);

        // then
        assertEquals(lineName, line.getName());
        assertEquals(colorCode, line.getColor());
        assertEquals(3, line.getStations().size());
    }

    @Test
    @DisplayName("노선의 가운데에 역을 기준의 오른쪽에 추가합니다.")
    void add_right_inner_station() {
        // given
        String lineName = "2";
        String colorCode = "#FFFFFF";
        Line line = new Line(lineName, colorCode);
        Station leftStation = new Station(1L, "left");
        Station rightStation = new Station(2L, "right");
        Station centerStation = new Station(3L, "last");
        line.initStations(leftStation, rightStation, 10);

        // when
        line.addStation(centerStation, leftStation, Direction.RIGHT, 5);

        // then
        assertEquals(lineName, line.getName());
        assertEquals(colorCode, line.getColor());
        assertEquals(3, line.getStations().size());
    }

    @Test
    @DisplayName("노선의 왼쪽에 종점을 추가합니다.")
    void add_left_last_station() {
        // given
        String lineName = "2";
        String colorCode = "#FFFFFF";
        Line line = new Line(lineName, colorCode);
        Station leftStation = new Station(1L, "left");
        Station rightStation = new Station(2L, "right");
        Station lastStation = new Station(3L, "last");
        line.initStations(leftStation, rightStation, 10);

        // when
        line.addStation(lastStation, leftStation, Direction.LEFT, 5);

        // then
        assertEquals(lineName, line.getName());
        assertEquals(colorCode, line.getColor());
        assertEquals(3, line.getStations().size());
    }

    @Test
    @DisplayName("노선에 가운데에 역을 기준의 왼쪽에 추가합니다.")
    void add_left_inner_station() {
        // given
        String lineName = "2";
        String colorCode = "#FFFFFF";
        Line line = new Line(lineName, colorCode);
        Station leftStation = new Station(1L, "left");
        Station rightStation = new Station(2L, "right");
        Station centerStation = new Station(3L, "last");
        line.initStations(leftStation, rightStation, 10);

        // when
        line.addStation(centerStation, rightStation, Direction.LEFT, 5);

        // then
        assertEquals(lineName, line.getName());
        assertEquals(colorCode, line.getColor());
        assertEquals(3, line.getStations().size());
    }

    @Test
    @DisplayName("노선의 가운데 역을 삭제합니다.")
    void delete_station() {
        // given
        String lineName = "2";
        String colorCode = "#FFFFFF";
        Line line = new Line(lineName, colorCode);
        Station leftStation = new Station(1L, "left");
        Station rightStation = new Station(2L, "right");
        Station centerStation = new Station(3L, "last");
        line.initStations(leftStation, rightStation, 10);
        line.addStation(centerStation, leftStation,Direction.RIGHT,5);

        // when
        line.deleteStation(centerStation);

        // then
        assertEquals(lineName, line.getName());
        assertEquals(colorCode, line.getColor());
        assertEquals(2, line.getStations().size());
    }
}
