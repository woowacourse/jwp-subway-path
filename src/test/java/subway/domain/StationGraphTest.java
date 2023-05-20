package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.SectionFixture.SECTIONS1;
import static subway.domain.SectionFixture.SECTIONS2;
import static subway.domain.SectionFixture.SECTIONS4;
import static subway.domain.SectionFixture.SECTIONS5;
import static subway.domain.SectionFixture.STATION1;
import static subway.domain.SectionFixture.STATION10;
import static subway.domain.SectionFixture.STATION11;
import static subway.domain.SectionFixture.STATION12;
import static subway.domain.SectionFixture.STATION2;
import static subway.domain.SectionFixture.STATION3;
import static subway.domain.SectionFixture.STATION4;
import static subway.domain.SectionFixture.STATION5;
import static subway.domain.SectionFixture.STATION6;
import static subway.domain.SectionFixture.STATION7;
import static subway.domain.SectionFixture.STATION8;
import static subway.domain.SectionFixture.STATION9;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StationGraphTest {

    static Stream<Arguments> getSections1() {
        return Stream.of(
                Arguments.of(SECTIONS1, 0),
                Arguments.of(SECTIONS1, 1),
                Arguments.of(SECTIONS1, 2),
                Arguments.of(SECTIONS1, 3),
                Arguments.of(SECTIONS1, 4),
                Arguments.of(SECTIONS1, 5),
                Arguments.of(SECTIONS2, 0),
                Arguments.of(SECTIONS2, 1),
                Arguments.of(SECTIONS2, 2),
                Arguments.of(SECTIONS2, 3),
                Arguments.of(SECTIONS2, 4),
                Arguments.of(SECTIONS2, 5)
        );
    }

    static Stream<Arguments> getSections2() {
        return Stream.of(
                Arguments.of(SECTIONS4, 0),
                Arguments.of(SECTIONS4, 1),
                Arguments.of(SECTIONS4, 2),
                Arguments.of(SECTIONS4, 3),
                Arguments.of(SECTIONS4, 4),
                Arguments.of(SECTIONS4, 5)
        );
    }

    static Stream<Arguments> getSections3() {
        return Stream.of(
                Arguments.of(SECTIONS4, 6),
                Arguments.of(SECTIONS4, 7),
                Arguments.of(SECTIONS4, 8),
                Arguments.of(SECTIONS4, 9),
                Arguments.of(SECTIONS4, 10)
        );
    }

    static Stream<Arguments> getSections4() {
        return Stream.of(
                Arguments.of(SECTIONS5, 0),
                Arguments.of(SECTIONS5, 1),
                Arguments.of(SECTIONS5, 2)
        );
    }

    static Stream<Arguments> getSections5() {
        return Stream.of(
                Arguments.of(SECTIONS5, 3),
                Arguments.of(SECTIONS5, 4),
                Arguments.of(SECTIONS5, 5)
        );
    }

    static Stream<Arguments> getSections6() {
        return Stream.of(
                Arguments.of(SECTIONS5, 6),
                Arguments.of(SECTIONS5, 7)
        );
    }

    @DisplayName("노선의 역 조회 테스트 1")
    @ParameterizedTest
    @MethodSource("getSections1")
    void findStations1(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index)).getStations()).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4,
                STATION5,
                STATION6,
                STATION7
        );
    }

    @DisplayName("노선의 구간 조회 테스트 1")
    @ParameterizedTest
    @MethodSource("getSections1")
    void findSections1(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findSections(sections.get(index)).getSections()).containsExactly(
                SECTIONS1.get(0),
                SECTIONS1.get(1),
                SECTIONS1.get(2),
                SECTIONS1.get(3),
                SECTIONS1.get(4),
                SECTIONS1.get(5)
        );
    }


    @DisplayName("노선의 역 조회 테스트 2")
    @ParameterizedTest
    @MethodSource("getSections2")
    void findStations2(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index)).getStations()).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4,
                STATION5,
                STATION6,
                STATION7
        );
    }

    @DisplayName("노선의 구간 조회 테스트 2")
    @ParameterizedTest
    @MethodSource("getSections2")
    void findSections2(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findSections(sections.get(index)).getSections()).containsExactly(
                SECTIONS4.get(0),
                SECTIONS4.get(1),
                SECTIONS4.get(2),
                SECTIONS4.get(3),
                SECTIONS4.get(4),
                SECTIONS4.get(5)
        );
    }

    @DisplayName("노선의 역 조회 테스트 3")
    @ParameterizedTest
    @MethodSource("getSections3")
    void findStations3(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index)).getStations()).containsExactly(
                STATION8,
                STATION9,
                STATION10,
                STATION4,
                STATION11,
                STATION12
        );
    }

    @DisplayName("노선의 구간 조회 테스트 3")
    @ParameterizedTest
    @MethodSource("getSections3")
    void findSections3(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findSections(sections.get(index)).getSections()).containsExactly(
                SECTIONS4.get(6),
                SECTIONS4.get(7),
                SECTIONS4.get(8),
                SECTIONS4.get(9),
                SECTIONS4.get(10)
        );
    }

    @DisplayName("노선의 역 조회 테스트 4")
    @ParameterizedTest
    @MethodSource("getSections4")
    void findStations4(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index)).getStations()).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4
        );
    }

    @DisplayName("노선의 구간 조회 테스트 4")
    @ParameterizedTest
    @MethodSource("getSections4")
    void findSections4(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findSections(sections.get(index)).getSections()).containsExactly(
                SECTIONS5.get(0),
                SECTIONS5.get(1),
                SECTIONS5.get(2)
        );
    }

    @DisplayName("노선의 역 조회 테스트 5")
    @ParameterizedTest
    @MethodSource("getSections5")
    void findStations5(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index)).getStations()).containsExactly(
                STATION3,
                STATION5,
                STATION4,
                STATION6
        );
    }

    @DisplayName("노선의 구간 조회 테스트 5")
    @ParameterizedTest
    @MethodSource("getSections5")
    void findSections5(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findSections(sections.get(index)).getSections()).containsExactly(
                SECTIONS5.get(3),
                SECTIONS5.get(4),
                SECTIONS5.get(5)
        );
    }

    @DisplayName("노선의 역 조회 테스트 6")
    @ParameterizedTest
    @MethodSource("getSections6")
    void findStations6(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index)).getStations()).containsExactly(
                STATION8,
                STATION4,
                STATION9
        );
    }

    @DisplayName("노선의 구간 조회 테스트 6")
    @ParameterizedTest
    @MethodSource("getSections6")
    void findSections6(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findSections(sections.get(index)).getSections()).containsExactly(
                SECTIONS5.get(6),
                SECTIONS5.get(7)
        );
    }
}
