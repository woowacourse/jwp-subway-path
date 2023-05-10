package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SubwayGraphTest {

    @Nested
    @DisplayName("새로운 역 추가: ")
    class AddStationTest {
        @Test
        @DisplayName("하행 종점 아래")
        void name() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(SAPYEONG_STATION, newStation, 3);
        }

        @Test
        @DisplayName("두 역 사이에 하행 방향")
        void name2() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(EXPRESS_TERMINAL_STATION, newStation, 3);
        }

        @Test
        @DisplayName("상행 종점 위")
        void name3() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(newStation, EXPRESS_TERMINAL_STATION, 2);
        }

        @Test
        @DisplayName("두 역 사이에 상행 방향")
        void name4() {
            final SubwayGraph subwayGraph = new SubwayGraph(new Line("999호선"));
            final Station EXPRESS_TERMINAL_STATION = new Station("고속터미널");
            final Station SAPYEONG_STATION = new Station("사평역");
            final Station newStation = new Station("새 역");
            subwayGraph.createNewLine(EXPRESS_TERMINAL_STATION, SAPYEONG_STATION, 5);

            subwayGraph.addStation(newStation, SAPYEONG_STATION, 2);
        }
    }
}
