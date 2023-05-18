package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.SectionFixture.SECTIONS1;
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
import org.junit.jupiter.api.Test;
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
                        List.of(STATION1, STATION2),
                        new Distance(3)
                ),
                Arguments.of(
                        SECTIONS4,
                        STATION3,
                        STATION10,
                        List.of(STATION3, STATION4, STATION10),
                        new Distance(6)
                ),
                Arguments.of(
                        SECTIONS5,
                        STATION3,
                        STATION4,
                        List.of(STATION3, STATION5, STATION4),
                        new Distance(8)
                ),
                Arguments.of(
                        SECTIONS5,
                        STATION2,
                        STATION9,
                        List.of(STATION2, STATION3, STATION5, STATION4, STATION9),
                        new Distance(17)
                )
        );
    }

    @DisplayName("거리의 합이 제일 짧은 경로를 선택한다.")
    @ParameterizedTest
    @MethodSource("getPath")
    void findShortestPath(
            final List<Section> sections,
            final Station from,
            final Station to,
            final List<Station> stations,
            final Distance distance
    ) {
        final PathFinder pathFinder = new PathFinder(sections);
        final Path shortestPath = pathFinder.findShortestPath(from, to);
        assertThat(shortestPath.getStations()).containsExactlyElementsOf(stations);
        assertThat(shortestPath.getDistance()).isEqualTo(distance);
    }

    @DisplayName("존재하지 않는 역이 들어오면 예외를 발생시킨다.")
    @Test
    void validateSectionsHasStation() {
        final PathFinder pathFinder = new PathFinder(SECTIONS1);
        assertThatThrownBy(() -> pathFinder.findShortestPath(new Station(8L), new Station(9L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 등록되지 않은 역입니다.: " + 8L);
    }

    @DisplayName("id만 있는 station이 요청으로 들어와도 이름을 추가해 반환한다.")
    @Test
    void returnStationWithName() {
        final PathFinder pathFinder = new PathFinder(SECTIONS1);
        final Path result = pathFinder.findShortestPath(new Station(1L), new Station(2L));
        assertAll(
                () -> assertThat(result.getStations().get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.getStations().get(0).getName()).isEqualTo("1L"),
                () -> assertThat(result.getStations().get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.getStations().get(1).getName()).isEqualTo("2L")
        );
    }
}
