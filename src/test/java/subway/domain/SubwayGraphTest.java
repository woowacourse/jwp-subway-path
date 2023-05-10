package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayGraphTest {

    @Nested
    @DisplayName("새로운 역 추가: ")
    class AddStationTest {
        @Test
        @DisplayName("하행 종점 아래")
        void name() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_BUS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(SAPYEONG_STATION, newStation, 3);
            // 고속터미널 -> 사평역 -> 새 역
            assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                    EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, newStation);
        }

        @Test
        @DisplayName("두 역 사이에 하행 방향")
        void name2() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_BUS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, newStation, 3);
            // 고속터미널 -> 새 역 -> 사평역
            assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                    EXPRESS_BUS_TERMINAL_STATION, newStation, SAPYEONG_STATION);
        }

        @Test
        @DisplayName("상행 종점 위")
        void name3() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_BUS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(newStation, EXPRESS_BUS_TERMINAL_STATION, 2);
            // 새 역 -> 고속터미널 -> 사평역
            Assertions.assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                    newStation, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
        }

        @Test
        @DisplayName("두 역 사이에 상행 방향")
        void name4() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_BUS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(newStation, SAPYEONG_STATION, 2);
            // 고속터미널 -> 새 역 -> 사평역
            assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                    EXPRESS_BUS_TERMINAL_STATION, newStation, SAPYEONG_STATION);
        }
    }

    @Test
    @DisplayName("노선의 상행 종점을 찾을 수 있다.")
    void findUpEndStationTest() {
        final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
        final Station EXPRESS_BUS_TERMINAL_STATION = new Station("고속터미널");
        final Station SAPYEONG_STATION = new Station("사평역");
        final Station newStation = new Station("새 역");
        subwayGraph.createNewLine(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

        subwayGraph.addStation(newStation, EXPRESS_BUS_TERMINAL_STATION, 2);

        // 새 역 -> 고속터미널 -> 사평역
        final Station upEndStation = subwayGraph.findUpEndStation();
        assertThat(upEndStation).isEqualTo(newStation);
    }

    @Test
    @DisplayName("노선을 상행 종점부터 순서대로 조회할 수 있다.")
    void findAllStationsInOrder() {
        final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
        final Station EXPRESS_BUS_TERMINAL_STATION = new Station("고속터미널");
        final Station SAPYEONG_STATION = new Station("사평역");
        final Station newStation = new Station("새 역");
        subwayGraph.createNewLine(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

        subwayGraph.addStation(newStation, EXPRESS_BUS_TERMINAL_STATION, 2);

        // 새 역 -> 고속터미널 -> 사평역
        final List<Station> allStationsInOrder = subwayGraph.findAllStationsInOrder();
        assertThat(allStationsInOrder).containsExactly(newStation, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
    }
}
