package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.section.domain.Distance;
import subway.section.domain.Section;
import subway.station.domain.Station;

class SubwayMapTest {

    private static final Station station1 = new Station(1L, "1L");
    private static final Station station2 = new Station(2L, "2L");
    private static final Station station3 = new Station(3L, "3L");
    private static final Station station4 = new Station(4L, "4L");
    private static final Station station5 = new Station(5L, "5L");
    private static final Station station6 = new Station(6L, "6L");
    private static final Station station7 = new Station(7L, "7L");

    static Stream<Arguments> getSections() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Section(1L, 1L, station1, station2, new Distance(3)),
                                new Section(2L, 1L, station2, station3, new Distance(3)),
                                new Section(3L, 1L, station3, station4, new Distance(3)),
                                new Section(4L, 1L, station4, station5, new Distance(3)),
                                new Section(5L, 1L, station5, station6, new Distance(3)),
                                new Section(6L, 1L, station6, station7, new Distance(3))
                        )
                ),
                Arguments.of(
                        List.of(
                                new Section(4L, 1L, station4, station5, new Distance(3)),
                                new Section(6L, 1L, station6, station7, new Distance(3)),
                                new Section(2L, 1L, station2, station3, new Distance(3)),
                                new Section(5L, 1L, station5, station6, new Distance(3)),
                                new Section(1L, 1L, station1, station2, new Distance(3)),
                                new Section(3L, 1L, station3, station4, new Distance(3))
                        )
                ),
                Arguments.of(
                        List.of(
                                new Section(1L, 1L, station4, station5, new Distance(3)),
                                new Section(2L, 1L, station6, station7, new Distance(3)),
                                new Section(3L, 1L, station2, station3, new Distance(3)),
                                new Section(4L, 1L, station5, station6, new Distance(3)),
                                new Section(5L, 1L, station1, station2, new Distance(3)),
                                new Section(6L, 1L, station3, station4, new Distance(3))
                        )
                )
        );
    }

    @DisplayName("노선의 경로 조회 테스트")
    @ParameterizedTest
    @MethodSource("getSections")
    void getStations(final List<Section> sections) {
        final SubwayMap subwayMap = SubwayMap.of(sections);
        assertThat(subwayMap.getStations()).containsExactly(
                station1,
                station2,
                station3,
                station4,
                station5,
                station6,
                station7
        );
    }
}
