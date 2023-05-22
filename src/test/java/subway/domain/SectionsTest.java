package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.graph.Graph;
import subway.domain.graph.SubwayGraph;
import subway.exeption.InvalidDistanceException;
import subway.exeption.InvalidStationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.*;

class SectionsTest {

    private final Graph graph = new SubwayGraph();

    private Sections createSections() {
        final Sections sections = new Sections(LINE_999, graph);
        sections.createInitialSection(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);
        return sections;
    }

    @Test
    @DisplayName("새로운 노선(2개의 역)을 등록한다")
    void createNewLine() {
        final Sections sections = new Sections(LINE_999, graph);
        sections.createInitialSection(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, 5);

        assertThat(sections.findAllStationsInOrder()).containsExactly(
                EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
    }

    @Nested
    @DisplayName("새로운 역 추가: ")
    class AddStationTest {
        @Test
        @DisplayName("하행 종점 아래")
        void name() {
            final Sections sections = createSections();

            sections.addStation(SAPYEONG_STATION, NEW_STATION, 3);
            // 고속터미널 -> 사평역 -> 새 역
            assertThat(sections.findAllStationsInOrder()).containsExactly(
                    EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, NEW_STATION);
        }

        @Test
        @DisplayName("두 역 사이에 하행 방향")
        void name2() {
            final Sections sections = createSections();

            sections.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 3);
            // 고속터미널 -> (3) 새 역 -> (2) 사평역

            assertAll(
                    () -> assertThat(sections.findAllStationsInOrder()).containsExactly(
                            EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, SAPYEONG_STATION),
                    () -> assertThat(sections.findDistanceBetween(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION))
                            .isEqualTo(3),
                    () -> assertThat(sections.findDistanceBetween(NEW_STATION, SAPYEONG_STATION))
                            .isEqualTo(2)
            );
        }

        @Test
        @DisplayName("상행 종점 위")
        void name3() {
            final Sections sections = createSections();

            sections.addStation(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, 2);
            // 새 역 -> 고속터미널 -> 사평역
            assertThat(sections.findAllStationsInOrder()).containsExactly(
                    NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
        }

        @Test
        @DisplayName("두 역 사이에 상행 방향")
        void name4() {
            final Sections sections = createSections();

            sections.addStation(NEW_STATION, SAPYEONG_STATION, 2);
            // 고속터미널 -> 새 역 -> 사평역
            assertThat(sections.findAllStationsInOrder()).containsExactly(
                    EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, SAPYEONG_STATION);
        }
    }

    @Test
    @DisplayName("노선의 상행 종점을 찾을 수 있다.")
    void findUpEndStationTest() {
        final Sections sections = createSections();

        sections.addStation(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, 2);

        // 새 역 -> 고속터미널 -> 사평역
        final Station upEndStation = sections.findUpEndStation();
        assertThat(upEndStation).isEqualTo(NEW_STATION);
    }

    @Test
    @DisplayName("노선을 상행 종점부터 순서대로 조회할 수 있다.")
    void findAllStationsInOrder() {
        final Sections sections = createSections();

        sections.addStation(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, 2);

        // 새 역 -> 고속터미널 -> 사평역
        final List<Station> allStationsInOrder = sections.findAllStationsInOrder();
        assertThat(allStationsInOrder).containsExactly(NEW_STATION, EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);
    }

    @Nested
    @DisplayName("거리 검증: ")
    class AddStationDistanceValidation {

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 짧은 거리로 하행 방향에 새로운 역을 추가할 수 있다.")
        @ValueSource(ints = {1, 2, 3, 4})
        void addStationToDownLineTest(int distance) {
            final Sections sections = createSections();
            assertDoesNotThrow(() -> sections.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, distance));
        }

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 추가되는 하행 방향의 역과의 거리가 길면 예외가 발생한다.")
        @ValueSource(ints = {5, 6, 7, 8})
        void addStationToDownLineExceptionTest(int distance) {
            final Sections sections = createSections();

            assertThatThrownBy(
                    () -> sections.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, distance))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessageContaining("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 짧은 거리로 상행 방향에 새로운 역을 추가할 수 있다.")
        @ValueSource(ints = {1, 2, 3, 4})
        void addStationToUpLineTest(int distance) {
            final Sections sections = createSections();
            assertDoesNotThrow(() -> sections.addStation(NEW_STATION, SAPYEONG_STATION, distance));
        }

        @ParameterizedTest
        @DisplayName("두 역 사이의 거리보다 추가되는 상행 방향의 역과의 거리가 길면 에외가 발생한다.")
        @ValueSource(ints = {5, 6, 7, 8})
        void addStationToUpLineExceptionTest(int distance) {
            final Sections sections = createSections();

            assertThatThrownBy(
                    () -> sections.addStation(NEW_STATION, SAPYEONG_STATION, distance))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessageContaining("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }
    }

    @Nested
    @DisplayName("새로운 역을 추가할 때 예외 처리:")
    class AddStationExceptionTest {

        @Test
        @DisplayName("입력한 두 역이 같은 역이면 예외가 발생한다.")
        void sameStations() {
            final Sections sections = new Sections(LINE_999, graph);
            sections.createInitialSection(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 4);
            assertThatThrownBy(
                    () -> sections.addStation(SAPYEONG_STATION, SAPYEONG_STATION, 2))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessageContaining("서로 다른 역을 입력해 주세요.");
        }

        @ParameterizedTest
        @DisplayName("역 사이의 거리는 양의 정수이다.")
        @ValueSource(ints = {0, -1})
        void createNewLineDistanceNegativeTest(int distance) {
            final Sections sections = new Sections(LINE_999, graph);
            assertThatThrownBy(
                    () -> sections.createInitialSection(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION, distance))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessageContaining("역 사이 거리는 양의 정수로 입력해 주세요.");
        }

        @ParameterizedTest
        @DisplayName("역 사이의 거리는 양의 정수이다.")
        @ValueSource(ints = {0, -1})
        void addStationDistanceNegativeTest(int distance) {
            final Sections sections = createSections();
            assertThatThrownBy(
                    () -> sections.createInitialSection(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, distance))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessageContaining("역 사이 거리는 양의 정수로 입력해 주세요.");
        }
    }

    @Test
    @DisplayName("하나의 역과 연결된 역을 제거할 수 있다.")
    void removeStationAtEnd() {
        Sections sections = createSections();
        sections.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 3);
        // 고속터미널 -> 새 역 -> 사평역

        sections.deleteStation(SAPYEONG_STATION);
        // 고속터미널 -> 새 역

        assertThat(sections.findAllStationsInOrder()).containsExactly(
                EXPRESS_BUS_TERMINAL_STATION, NEW_STATION);
    }

    @Test
    @DisplayName("하나의 역과 연결된 역을 제거할 수 있다.")
    void removeStationInMiddle() {
        Sections sections = createSections();
        sections.addStation(EXPRESS_BUS_TERMINAL_STATION, NEW_STATION, 3);
        // 고속터미널 -> (3) 새 역 -> (2) 사평역

        sections.deleteStation(NEW_STATION);
        // 고속터미널 -> (5) 사평역

        assertThat(sections.findAllStationsInOrder()).containsExactly(
                EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION);

        assertThat(sections.findDistanceBetween(EXPRESS_BUS_TERMINAL_STATION, SAPYEONG_STATION))
                .isEqualTo(5);
    }
}
