package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.subway.Path;
import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.domain.subway.Subway;
import subway.domain.vo.Charge;
import subway.domain.vo.Distance;

class SubwayTest {

    public static final Station STATION_A = new Station(1L, "A");
    public static final Station STATION_B = new Station(2L, "B");
    public static final Station STATION_C = new Station(3L, "C");
    public static final Station STATION_D = new Station(4L, "D");
    public static final Station STATION_E = new Station(5L, "E");
    public static final Station STATION_F = new Station(6L, "F");

    private static final Distance DISTANCE_1 = new Distance(1);
    private static final Distance DISTANCE_2 = new Distance(2);
    private static final Distance DISTANCE_3 = new Distance(3);
    private static final Distance DISTANCE_4 = new Distance(4);
    private static final Distance DISTANCE_5 = new Distance(5);
    private static final Distance DISTANCE_18 = new Distance(18);
    private static final Distance DISTANCE_25 = new Distance(25);
    private static final Distance DISTANCE_20 = new Distance(20);
    private static final Distance DISTANCE_1000 = new Distance(1000);

    @Nested
    @DisplayName("최소 거리 계산")
    class findShortestRoute {
        @Test
        @DisplayName("성공")
        void success() {
            // a-b-c
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_3), new Section(STATION_B, STATION_C, DISTANCE_25)));

            // d-b-e-c
            Line lineB = new Line(2L, "lineB", new Charge(0),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_3),
                            new Section(STATION_E, STATION_C, DISTANCE_1)));

            // c-f
            Line lineC = new Line(3L, "lineC", new Charge(0),
                    List.of(new Section(STATION_C, STATION_F, DISTANCE_2)));

            List<Line> lines = List.of(lineA, lineB, lineC);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C-F
            Path path = subway.findShortestRoute(0, STATION_A, STATION_F);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getRoutes().get(2).getStations()).containsExactly(STATION_C, STATION_F);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(9);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(1250);
        }

        @Test
        @DisplayName("실패 - 역이 노선에 등록되지 않은 경우")
        void fail1() {
            // a-b
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_3)));

            // c-f
            Line lineC = new Line(3L, "lineC", new Charge(0),
                    List.of(new Section(STATION_C, STATION_F, DISTANCE_2)));

            List<Line> lines = List.of(lineA, lineC);
            Subway subway = Subway.create(lines);

            // route :
            assertThatThrownBy(() -> subway.findShortestRoute(0, STATION_A, STATION_D))
                    .hasMessage("노선에 등록되지 않은 역입니다.");
        }

        @Test
        @DisplayName("실패 - 경로를 찾을 수 없는 경우")
        void fail2() {
            // a-b
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_3)));

            // c-f
            Line lineC = new Line(3L, "lineC", new Charge(0),
                    List.of(new Section(STATION_C, STATION_F, DISTANCE_2)));

            List<Line> lines = List.of(lineA, lineC);
            Subway subway = Subway.create(lines);

            // route :
            assertThatThrownBy(() -> subway.findShortestRoute(0, STATION_A, STATION_F))
                    .hasMessage("경로를 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("거리별 요금")
    class distanceCharge {

        @Test
        @DisplayName("10km이내는 기본운임이 적용된다")
        void distance_charge_smaller_than_10() {
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_5), new Section(STATION_B, STATION_C, DISTANCE_25)));

            Line lineB = new Line(2L, "lineB", new Charge(0),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_3),
                            new Section(STATION_E, STATION_C, DISTANCE_1)));

            List<Line> lines = List.of(lineA, lineB);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(9);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(1250);
        }

        @Test
        @DisplayName("10~50km는 5km마다 100원이 추가된다")
        void distance_charge_bigger_than_10_and_smaller_than_50() {
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_5), new Section(STATION_B, STATION_C, DISTANCE_25)));

            Line lineB = new Line(2L, "lineB", new Charge(0),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_3),
                            new Section(STATION_E, STATION_C, DISTANCE_4)));

            List<Line> lines = List.of(lineA, lineB);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(12);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(1350);
        }

        @Test
        @DisplayName("50km를 초과할 경우 8km마다 100원이 추가된다")
        void distance_charge_bigger_than_50() {
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_20), new Section(STATION_B, STATION_C, DISTANCE_1000)));

            Line lineB = new Line(2L, "lineB", new Charge(0),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_20),
                            new Section(STATION_E, STATION_C, DISTANCE_18)));

            List<Line> lines = List.of(lineA, lineB);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(58);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(2150);
        }
    }

    @Nested
    @DisplayName("노선별 추가 요금")
    class lineCharge {

        @Test
        @DisplayName("추가 요금이 있는 노선을 이용 할 경우 거리별 요금에 추가요금이 적용된다")
        void line_charge() {
            Line lineA = new Line(1L, "lineA", new Charge(1000),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_5), new Section(STATION_B, STATION_C, DISTANCE_25)));

            Line lineB = new Line(2L, "lineB", new Charge(0),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_3),
                            new Section(STATION_E, STATION_C, DISTANCE_1)));

            List<Line> lines = List.of(lineA, lineB);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(9);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(2250);
        }

        @Test
        @DisplayName("추가요금이 있는 여러 노선을 이용 할 경우 가장 높은 금액의 추가 요금만 적용")
        void biggest_line_charge() {
            Line lineA = new Line(1L, "lineA", new Charge(1000),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_5), new Section(STATION_B, STATION_C, DISTANCE_25)));

            Line lineB = new Line(2L, "lineB", new Charge(2000),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_3),
                            new Section(STATION_E, STATION_C, DISTANCE_1)));

            List<Line> lines = List.of(lineA, lineB);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(0, STATION_A, STATION_C);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(9);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(3250);
        }
    }

    @Nested
    @DisplayName("연령별 할인 적용")
    class ageDiscount {

        @Test
        @DisplayName("어린이(6세 이상~13세 미만)인 경우 운임에서 350원을 공제한 금액의 50% 할인")
        void children_discount() {
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_5), new Section(STATION_B, STATION_C, DISTANCE_25)));

            Line lineB = new Line(2L, "lineB", new Charge(0),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_3),
                            new Section(STATION_E, STATION_C, DISTANCE_1)));

            List<Line> lines = List.of(lineA, lineB);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(8, STATION_A, STATION_C);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(9);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(450);
        }

        @Test
        @DisplayName("청소년(13세 이상~19세 미만)인 경우 운임에서 350원을 공제한 금액의 20% 할인")
        void teenager_discount() {
            Line lineA = new Line(1L, "lineA", new Charge(0),
                    List.of(new Section(STATION_A, STATION_B, DISTANCE_5), new Section(STATION_B, STATION_C, DISTANCE_25)));

            Line lineB = new Line(2L, "lineB", new Charge(0),
                    List.of(new Section(STATION_D, STATION_B, DISTANCE_4), new Section(STATION_B, STATION_E, DISTANCE_3),
                            new Section(STATION_E, STATION_C, DISTANCE_1)));

            List<Line> lines = List.of(lineA, lineB);
            Subway subway = Subway.create(lines);

            // route : A-B-E-C
            Path path = subway.findShortestRoute(15, STATION_A, STATION_C);

            assertThat(path.getRoutes().get(0).getStations()).containsExactly(STATION_A, STATION_B);
            assertThat(path.getRoutes().get(1).getStations()).containsExactly(STATION_B, STATION_E, STATION_C);
            assertThat(path.getTotalDistance().getValue()).isEqualTo(9);
            assertThat(path.getTotalCharge().getValue()).isEqualTo(720);
        }
    }
}
