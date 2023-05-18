package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LineTest {

    private static Stream<Arguments> createLine() {
        return Stream.of(
           Arguments.arguments(new Line(1L, "2호선", "#123456", List.of()), true),
           Arguments.arguments(new Line(1L, "2호선", "#123456", List.of(new Section(new Station("잠실"), new Station("선릉"), 10))), false)
        );
    }

    @ParameterizedTest
    @MethodSource("createStation")
    @DisplayName("주어진 station이 양 끝역인지 확인한다.")
    void check_station_is_boundStatoin(Station station, boolean expect) {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );

        Line line = new Line(1L, "2호선", "#123456", sections);

        // when
        boolean result = line.isBoundStation(station);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> createStation() {
        return Stream.of(
                Arguments.arguments(new Station(1L, "잠실"), true),
                Arguments.arguments(new Station(2L, "선릉"), false),
                Arguments.arguments(new Station(3L, "강남"), true)
        );
    }
}
