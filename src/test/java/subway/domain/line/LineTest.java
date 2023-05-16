package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.line.AlreadyExistStationException;
import subway.exception.line.InvalidDistanceException;
import subway.exception.line.NotDownBoundStationException;
import subway.exception.line.NotUpBoundStationException;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LineTest {

    @ParameterizedTest
    @MethodSource("createLine")
    @DisplayName("line의 section이 비어 있으면 false, 그렇지 않으면 true를 반환한다.")
    void check_section_exist(Line line, boolean expect) {
        // when
        boolean result = line.isEmpty();

        // then
        assertThat(result).isEqualTo(expect);
    }

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

    @Test
    @DisplayName("상행 종점에 추가할 때 상행 종점이 올바르지 않으면 에러를 발생한다.")
    void check_baseStation_is_upboundStation() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );

        Line line = new Line(1L, "2호선", "#123456", sections);

        Station baseStation = new Station(2L, "선릉");
        Station insertStation = new Station(4L, "강남역");

        // when + then
        assertThatThrownBy(() -> line.addUpBoundStation(baseStation, insertStation))
                .isInstanceOf(NotUpBoundStationException.class);
    }

    @Test
    @DisplayName("하행 종점에 추가할 때 하행 종점이 올바르지 않으면 에러를 발생한다.")
    void check_baseStation_is_downboundStation() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );

        Line line = new Line(1L, "2호선", "#123456", sections);

        Station baseStation = new Station(2L, "선릉");
        Station insertStation = new Station(4L, "강남역");

        // when + then
        assertThatThrownBy(() -> line.addDownBoundStation(baseStation, insertStation))
                .isInstanceOf(NotDownBoundStationException.class);
    }

    @Test
    @DisplayName("이미 존재하는 역을 추가하려고 하면 에러를 발생한다.")
    void check_station_is_already_exist() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );

        Line line = new Line(1L, "2호선", "#123456", sections);

        Station baseStation = new Station(2L, "선릉");
        Station insertStation = new Station(1L, "잠실");

        // when + then
        assertThatThrownBy(() -> line.addInterStation(baseStation, insertStation, "left",10))
                .isInstanceOf(AlreadyExistStationException.class);
    }

    @Test
    @DisplayName("노선 중간에 역을 추가할 때 기존 구간보다 길이가 같거나 크면 에러를 발생한다.")
    void check_new_section_is_longer_than_exist_section() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );

        Line line = new Line(1L, "2호선", "#123456", sections);

        Station baseStation = new Station(2L, "선릉");
        Station insertStation = new Station(1L, "건대입구");

        // when + then
        assertThatThrownBy(() -> line.addInterStation(baseStation, insertStation, "left",10))
                .isInstanceOf(InvalidDistanceException.class);
    }
}
