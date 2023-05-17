package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.SectionFixture.SECTIONS1;
import static subway.domain.SectionFixture.SECTIONS2;
import static subway.domain.SectionFixture.SECTIONS3;
import static subway.domain.SectionFixture.SECTIONS4;
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

    static Stream<Arguments> getSections() {
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
                Arguments.of(SECTIONS2, 5),
                Arguments.of(SECTIONS3, 0),
                Arguments.of(SECTIONS3, 1),
                Arguments.of(SECTIONS3, 2),
                Arguments.of(SECTIONS3, 3),
                Arguments.of(SECTIONS3, 4),
                Arguments.of(SECTIONS3, 5)
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

    @DisplayName("노선의 경로 조회 테스트")
    @ParameterizedTest
    @MethodSource("getSections")
    void findStations(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index))).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4,
                STATION5,
                STATION6,
                STATION7
        );
    }


    @DisplayName("노선의 경로 조회 테스트")
    @ParameterizedTest
    @MethodSource("getSections2")
    void getStations(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index))).containsExactly(
                STATION1,
                STATION2,
                STATION3,
                STATION4,
                STATION5,
                STATION6,
                STATION7
        );
    }

    @DisplayName("노선의 경로 조회 테스트")
    @ParameterizedTest
    @MethodSource("getSections3")
    void getStations2(final List<Section> sections, final int index) {
        final StationGraph stationGraph = StationGraph.of(sections);
        assertThat(stationGraph.findStations(sections.get(index))).containsExactly(
                STATION8,
                STATION9,
                STATION10,
                STATION4,
                STATION11,
                STATION12
        );
    }
}
