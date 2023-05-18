package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.SectionFixture.SECTIONS4;
import static subway.domain.SectionFixture.SECTIONS5;
import static subway.domain.SectionFixture.STATION1;
import static subway.domain.SectionFixture.STATION10;
import static subway.domain.SectionFixture.STATION2;
import static subway.domain.SectionFixture.STATION3;
import static subway.domain.SectionFixture.STATION4;
import static subway.domain.SectionFixture.STATION5;
import static subway.domain.SectionFixture.STATION9;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PathFinderTest {

    static Stream<Arguments> getPath() {
        return Stream.of(
                Arguments.of(
                        SECTIONS4,
                        STATION1,
                        STATION2,
                        List.of(STATION1, STATION2)
                ),
                Arguments.of(
                        SECTIONS4,
                        STATION3,
                        STATION10,
                        List.of(STATION3, STATION4, STATION10)
                ),
                Arguments.of(
                        SECTIONS5,
                        STATION3,
                        STATION4,
                        List.of(STATION3, STATION5, STATION4)
                ),
                Arguments.of(
                        SECTIONS5,
                        STATION2,
                        STATION9,
                        List.of(STATION2, STATION3, STATION5, STATION4, STATION9)
                )
        );
    }

    @DisplayName("거리의 합이 제일 짧은 경로를 선택한다.")
    @ParameterizedTest
    @MethodSource("getPath")
    void findShortestPath(final List<Section> sections, final Station from, final Station to, final List<Station> stations) {
        final PathFinder pathFinder = new PathFinder(sections);
        final List<Station> shortestPath = pathFinder.findShortestPath(from, to);
        assertThat(shortestPath).containsExactlyElementsOf(stations);
    }
}
