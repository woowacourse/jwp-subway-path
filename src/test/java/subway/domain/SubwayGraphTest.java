package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.StationAlreadyExistException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.*;

class SubwayGraphTest {

    private SubwayGraph createSubwayGraph() {
        final SubwayGraph subwayGraph = new SubwayGraph(LINE_999);
        subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);
        return subwayGraph;
    }

    @Test
    @DisplayName("새로운 노선(2개의 역)을 등록한다")
    void createNewLine() {
        final SubwayGraph subwayGraph = new SubwayGraph(LINE_999);
        subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

        assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
    }

    @Nested
    @DisplayName("새로운 역 추가: ")
    class AddStationTest {
        @Test
        @DisplayName("하행 종점 아래")
        void name() {
            final SubwayGraph subwayGraph = createSubwayGraph();

            subwayGraph.addStation(SAPYEONG_STATION, NEW_STATION, 3);
            // 고속터미널 -> 사평역 -> 새 역
            assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                    EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, NEW_STATION);
        }

        @Test
        @DisplayName("두 역 사이에 하행 방향")
        void name2() {
            final SubwayGraph subwayGraph = createSubwayGraph();

            subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 3);
            // 고속터미널 -> (3) 새 역 -> (2) 사평역

            assertAll(
                    () -> assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                            EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, SAPYEONG_STATION),
                    () -> assertThat(subwayGraph.findEdgeEntity(EXPRESS_BUS_TERMINAL_STATION).getDistanceToNext())
                            .isEqualTo(3),
                    () -> assertThat(subwayGraph.findEdgeEntity(NEW_STATION).getDistanceToNext())
                            .isEqualTo(2)
            );
        }

        @Test
        @DisplayName("상행 종점 위")
        void name3() {
            final SubwayGraph subwayGraph = createSubwayGraph();

            subwayGraph.addStation(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, 2);
            // 새 역 -> 고속터미널 -> 사평역
            assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                    NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
        }

        @Test
        @DisplayName("두 역 사이에 상행 방향")
        void name4() {
            final SubwayGraph subwayGraph = createSubwayGraph();

            subwayGraph.addStation(NEW_STATION, SAPYEONG_STATION, 2);
            // 고속터미널 -> 새 역 -> 사평역
            assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                    EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, SAPYEONG_STATION);
        }
    }

    @Test
    @DisplayName("노선의 상행 종점을 찾을 수 있다.")
    void findUpEndStationTest() {
        final SubwayGraph subwayGraph = createSubwayGraph();

        subwayGraph.addStation(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, 2);

        // 새 역 -> 고속터미널 -> 사평역
        final Station upEndStation = subwayGraph.findUpEndStation();
        assertThat(upEndStation).isEqualTo(NEW_STATION);
    }

    @Test
    @DisplayName("노선을 상행 종점부터 순서대로 조회할 수 있다.")
    void findAllStationsInOrder() {
        final SubwayGraph subwayGraph = createSubwayGraph();

        subwayGraph.addStation(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, 2);

        // 새 역 -> 고속터미널 -> 사평역
        final List<Station> allStationsInOrder = subwayGraph.findAllStationsInOrder();
        assertThat(allStationsInOrder).containsExactly(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
    }

    @Nested
    @DisplayName("거리 검증: ")
    class AddStationDistanceValidation {

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 짧은 거리로 하행 방향에 새로운 역을 추가할 수 있다.")
        @ValueSource(ints = {1, 2, 3, 4})
        void addStationToDownLineTest(int distance) {
            final SubwayGraph subwayGraph = createSubwayGraph();
            assertDoesNotThrow(() -> subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, distance));
        }

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 추가되는 하행 방향의 역과의 거리가 길면 예외가 발생한다.")
        @ValueSource(ints = {5, 6, 7, 8})
        void addStationToDownLineExceptionTest(int distance) {
            final SubwayGraph subwayGraph = createSubwayGraph();

            assertThatThrownBy(
                    () -> subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, distance))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 짧은 거리로 상행 방향에 새로운 역을 추가할 수 있다.")
        @ValueSource(ints = {1, 2, 3, 4})
        void addStationToUpLineTest(int distance) {
            final SubwayGraph subwayGraph = createSubwayGraph();
            assertDoesNotThrow(() -> subwayGraph.addStation(NEW_STATION, SAPYEONG_STATION, distance));
        }

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 추가되는 상행 방향의 역과의 거리가 길면 에외가 발생한다.")
        @ValueSource(ints = {5, 6, 7, 8})
        void addStationToUpLineExceptionTest(int distance) {
            final SubwayGraph subwayGraph = createSubwayGraph();

            assertThatThrownBy(
                    () -> subwayGraph.addStation(NEW_STATION, SAPYEONG_STATION, distance))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }
    }

    @Nested
    @DisplayName("새로운 역을 추가할 때 예외 처리:")
    class AddStationExceptionTest {

        @Test
        @DisplayName("입력한 두 역이 같은 역이면 예외가 발생한다.")
        void sameStations() {
            final SubwayGraph subwayGraph = new SubwayGraph(LINE_999);
            subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 4);
            assertThatThrownBy(
                    () -> subwayGraph.addStation(SAPYEONG_STATION, SAPYEONG_STATION, 2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("서로 다른 역을 입력해 주세요.");
        }

        @Test
        @DisplayName("입력한 두 역이 모두 새로운 역이면 예외가 발생한다.")
        void onlyNewStations() {
            final SubwayGraph subwayGraph = new SubwayGraph(LINE_999);
            subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 4);
            assertThatThrownBy(
                    () -> subwayGraph.addStation(SAPYEONG_STATION, SINNONHYEON_STATION, 2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("모두 새로운 역입니다. 새로운 역과 기존 역을 입력해 주세요.");
        }

        @Test
        @DisplayName("입력한 두 역이 모두 이미 존재하는 역이면 예외가 발생한다.")
        void onlyExistingStations() {
            final SubwayGraph subwayGraph = new SubwayGraph(LINE_999);
            subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);
            assertThatThrownBy(
                    () -> subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 2)).isInstanceOf(StationAlreadyExistException.class)
                    .hasMessageContaining("해당 역이 이미 존재 합니다.");
        }

        @ParameterizedTest
        @DisplayName("역 사이의 거리는 양의 정수이다.")
        @ValueSource(ints = {0, -1})
        void createNewLineDistanceNegativeTest(int distance) {
            final SubwayGraph subwayGraph = new SubwayGraph(LINE_999);
            assertThatThrownBy(
                    () -> subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, distance))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("역 사이 거리는 양의 정수로 입력해 주세요.");
        }

        @ParameterizedTest
        @DisplayName("역 사이의 거리는 양의 정수이다.")
        @ValueSource(ints = {0, -1})
        void addStationDistanceNegativeTest(int distance) {
            final SubwayGraph subwayGraph = createSubwayGraph();
            assertThatThrownBy(
                    () -> subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, distance))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("역 사이 거리는 양의 정수로 입력해 주세요.");
        }
    }

    @Test
    @DisplayName("하나의 역과 연결된 역을 제거할 수 있다.")
    void removeStationAtEnd() {
        SubwayGraph subwayGraph = createSubwayGraph();
        subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 3);
        // 고속터미널 -> 새 역 -> 사평역

        subwayGraph.delete(SAPYEONG_STATION);
        // 고속터미널 -> 새 역

        assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                EXPRESS_BUS_TERMINAL_STATION, NEW_STATION);
    }

    @Test
    @DisplayName("하나의 역과 연결된 역을 제거할 수 있다.")
    void removeStationInMiddle() {
        SubwayGraph subwayGraph = createSubwayGraph();
        subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 3);
        // 고속터미널 -> (3) 새 역 -> (2) 사평역

        subwayGraph.delete(NEW_STATION);
        // 고속터미널 -> (5) 사평역

        assertThat(subwayGraph.findAllStationsInOrder()).containsExactly(
                EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);

        assertThat(subwayGraph.findEdgeEntity(EXPRESS_BUS_TERMINAL_STATION).getDistanceToNext())
                .isEqualTo(5);
    }

    @Test
    @DisplayName("연결된 다음 역을 찾을 수 있다")
    void findNextStation() {
        SubwayGraph subwayGraph = createSubwayGraph();
        subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 2);
        Station nextStation = subwayGraph.findNextStation(EXPRESS_BUS_TERMINAL_STATION);

        assertThat(nextStation).isEqualTo(NEW_STATION);
    }

    @Test
    @DisplayName("연결된 다음 역의 거리를 찾을 수 있다")
    void findWeight() {
        SubwayGraph subwayGraph = createSubwayGraph();
        subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 2);
        int weight = subwayGraph.findDistance(EXPRESS_BUS_TERMINAL_STATION);

        assertThat(weight).isEqualTo(2);
    }

    @Test
    @DisplayName("연결된 다음 역의 거리가 종점인 경우 거리는 null이다.")
    void findWeight1() {
        SubwayGraph subwayGraph = createSubwayGraph();
        subwayGraph.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 2);
        Integer weight = subwayGraph.findDistance(SAPYEONG_STATION);

        assertThat(weight).isNull();
    }

}