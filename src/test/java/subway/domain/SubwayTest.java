package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SubwayTest {

    public static final Station STATION_A = new Station(1L, "A");
    public static final Station STATION_B = new Station(2L, "B");
    public static final Station STATION_C = new Station(3L, "C");
    public static final Station STATION_D = new Station(4L, "D");
    public static final Station STATION_E = new Station(5L, "E");
    public static final Station STATION_F = new Station(6L, "F");

    @Nested
    @DisplayName("거리별 요금")
    class distanceCharge {

        @Test
        @DisplayName("10km이내는 기본운임이 적용된다")
        void distance_charge_smaller_than_10() {
            Line lineA = new Line(1L, "lineA", 0,
                    List.of(new Section(STATION_A, STATION_B, 5), new Section(STATION_B, STATION_C, 25)));

            Line lineB = new Line(2L, "lineB", 0,
                    List.of(new Section(STATION_D, STATION_B, 4), new Section(STATION_B, STATION_E, 3),
                            new Section(STATION_E, STATION_C, 1)));

            List<Line> lines = List.of(lineA, lineB);
            List<Station> stations = List.of(STATION_A, STATION_B, STATION_C, STATION_D, STATION_E, STATION_F);
            Subway subway = Subway.create(stations, lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);
            assertThat(path.getStations()).containsExactly(STATION_A, STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance()).isEqualTo(9);
            assertThat(path.getTotalCharge()).isEqualTo(1250);
        }

        @Test
        @DisplayName("10~50km는 5km마다 100원이 추가된다")
        void distance_charge_bigger_than_10_and_smaller_than_50() {
            Line lineA = new Line(1L, "lineA", 0,
                    List.of(new Section(STATION_A, STATION_B, 5), new Section(STATION_B, STATION_C, 25)));

            Line lineB = new Line(2L, "lineB", 0,
                    List.of(new Section(STATION_D, STATION_B, 4), new Section(STATION_B, STATION_E, 3),
                            new Section(STATION_E, STATION_C, 4)));

            List<Line> lines = List.of(lineA, lineB);
            List<Station> stations = List.of(STATION_A, STATION_B, STATION_C, STATION_D, STATION_E, STATION_F);
            Subway subway = Subway.create(stations, lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);
            assertThat(path.getStations()).containsExactly(STATION_A, STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance()).isEqualTo(12);
            assertThat(path.getTotalCharge()).isEqualTo(1350);
        }

        @Test
        @DisplayName("50km를 초과할 경우 8km마다 100원이 추가된다")
        void distance_charge_bigger_than_50() {
            Line lineA = new Line(1L, "lineA", 0,
                    List.of(new Section(STATION_A, STATION_B, 20), new Section(STATION_B, STATION_C, 1000)));

            Line lineB = new Line(2L, "lineB", 0,
                    List.of(new Section(STATION_D, STATION_B, 4), new Section(STATION_B, STATION_E, 20),
                            new Section(STATION_E, STATION_C, 18)));

            List<Line> lines = List.of(lineA, lineB);
            List<Station> stations = List.of(STATION_A, STATION_B, STATION_C, STATION_D, STATION_E, STATION_F);
            Subway subway = Subway.create(stations, lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);
            assertThat(path.getStations()).containsExactly(STATION_A, STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance()).isEqualTo(58);
            assertThat(path.getTotalCharge()).isEqualTo(2150);
        }
    }

    @Nested
    @DisplayName("노선별 추가 요금")
    class lineCharge {

        @Test
        @DisplayName("추가 요금이 있는 노선을 이용 할 경우 거리별 요금에 추가요금이 적용된다")
        void line_charge() {
            Line lineA = new Line(1L, "lineA", 1000,
                    List.of(new Section(STATION_A, STATION_B, 5), new Section(STATION_B, STATION_C, 25)));

            Line lineB = new Line(2L, "lineB", 0,
                    List.of(new Section(STATION_D, STATION_B, 4), new Section(STATION_B, STATION_E, 3),
                            new Section(STATION_E, STATION_C, 1)));

            List<Line> lines = List.of(lineA, lineB);
            List<Station> stations = List.of(STATION_A, STATION_B, STATION_C, STATION_D, STATION_E, STATION_F);
            Subway subway = Subway.create(stations, lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);
            assertThat(path.getStations()).containsExactly(STATION_A, STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance()).isEqualTo(9);
            assertThat(path.getTotalCharge()).isEqualTo(2250);
        }

        @Test
        @DisplayName("추가요금이 있는 여러 노선을 이용 할 경우 가장 높은 금액의 추가 요금만 적용")
        void biggest_line_charge() {
            Line lineA = new Line(1L, "lineA", 1000,
                    List.of(new Section(STATION_A, STATION_B, 5), new Section(STATION_B, STATION_C, 25)));

            Line lineB = new Line(2L, "lineB", 2000,
                    List.of(new Section(STATION_D, STATION_B, 4), new Section(STATION_B, STATION_E, 3),
                            new Section(STATION_E, STATION_C, 1)));

            List<Line> lines = List.of(lineA, lineB);
            List<Station> stations = List.of(STATION_A, STATION_B, STATION_C, STATION_D, STATION_E, STATION_F);
            Subway subway = Subway.create(stations, lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);
            assertThat(path.getStations()).containsExactly(STATION_A, STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance()).isEqualTo(9);
            assertThat(path.getTotalCharge()).isEqualTo(3250);
        }
    }

    @Nested
    @DisplayName("연령별 할인 적용")
    class ageDiscount {

        @Test
        @DisplayName("어린이(6세 이상~13세 미만)인 경우 운임에서 350원을 공제한 금액의 50% 할인")
        void children_discount() {
            Line lineA = new Line(1L, "lineA", 0,
                    List.of(new Section(STATION_A, STATION_B, 5), new Section(STATION_B, STATION_C, 25)));

            Line lineB = new Line(2L, "lineB", 0,
                    List.of(new Section(STATION_D, STATION_B, 4), new Section(STATION_B, STATION_E, 3),
                            new Section(STATION_E, STATION_C, 1)));

            List<Line> lines = List.of(lineA, lineB);
            List<Station> stations = List.of(STATION_A, STATION_B, STATION_C, STATION_D, STATION_E, STATION_F);
            Subway subway = Subway.create(stations, lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(8, STATION_A, STATION_C);
            assertThat(path.getStations()).containsExactly(STATION_A, STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance()).isEqualTo(9);
            assertThat(path.getTotalCharge()).isEqualTo(450);
        }

        @Test
        @DisplayName("청소년(13세 이상~19세 미만)인 경우 운임에서 350원을 공제한 금액의 20% 할인")
        void teenager_discount() {
            Line lineA = new Line(1L, "lineA", 0,
                    List.of(new Section(STATION_A, STATION_B, 5), new Section(STATION_B, STATION_C, 25)));

            Line lineB = new Line(2L, "lineB", 0,
                    List.of(new Section(STATION_D, STATION_B, 4), new Section(STATION_B, STATION_E, 3),
                            new Section(STATION_E, STATION_C, 1)));

            List<Line> lines = List.of(lineA, lineB);
            List<Station> stations = List.of(STATION_A, STATION_B, STATION_C, STATION_D, STATION_E, STATION_F);
            Subway subway = Subway.create(stations, lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(15, STATION_A, STATION_C);
            assertThat(path.getStations()).containsExactly(STATION_A, STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance()).isEqualTo(9);
            assertThat(path.getTotalCharge()).isEqualTo(720);
        }
    }
}
