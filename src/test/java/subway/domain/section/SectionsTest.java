package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.station.Station;
import subway.exception.common.NotFoundStationException;
import subway.exception.input.InvalidNewSectionDistanceException;
import subway.exception.line.AlreadyExistStationException;
import subway.exception.line.LineIsInitException;
import subway.exception.line.LineIsNotInitException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SectionsTest {

    @Test
    @DisplayName("빈 sections 아닌 상황에 section(두개의 역)을 추가하면 예외를 발생시킨다.")
    void exception_evoke_when_add_two_station_to_sections_not_empty() {
        // given
        List<Section> lineSection = new ArrayList<>();
        lineSection.add(new Section(new Station("잠실"), new Station("선릉"), 10));
        Sections sections = new Sections(lineSection);

        Section section = new Section(new Station("선릉"), new Station("강남"), 10);

        // when + then
        assertThatThrownBy(() -> sections.addInitSection(section))
                .isInstanceOf(LineIsNotInitException.class);
    }

    @Test
    @DisplayName("빈 sections에 section을 추가하면 예외가 발생하지 않는다.")
    void success_add_two_station() {
        // given
        Sections sections = new Sections(new ArrayList<>());
        Section section = new Section(new Station("잠실"), new Station("선릉"), 10);

        // when + then
        assertDoesNotThrow(() -> sections.addInitSection(section));
    }

    @Test
    @DisplayName("역 하나를 추가하려고 할 때 sections가 비어있으면 에러를 발생한다.")
    void exception_evoke_when_add_station_to_sections_empty() {
        // given
        Sections sections = new Sections(new ArrayList<>());
        Station baseStation = new Station("잠실");
        String direction = "right";
        Station insertStation = new Station("선릉");
        int distance = 10;

        // when
        assertThatThrownBy(() -> sections.addSection(baseStation, direction, insertStation, distance))
                .isInstanceOf(LineIsInitException.class);
    }

    @ParameterizedTest(name = "{displayName}")
    @MethodSource("baseStationAndRegisterStationAndResult")
    @DisplayName("기준이 되는 station이 없거나 새로 추가하려는 station이 존재하면 예외를 발생시킵니다.")
    void exception_evoke_no_base_station_or_already_have_register_station(Station baseStation, Station registerStation, Exception exception) {
        // given
        List<Section> lineSection = new ArrayList<>();
        lineSection.add(new Section(new Station("잠실"), new Station("선릉"), 10));
        Sections sections = new Sections(lineSection);

        // when + then
        assertThatThrownBy(() -> sections.addSection(baseStation, "right", registerStation, 10))
                .isInstanceOf(exception.getClass());
    }

    private static Stream<Arguments> baseStationAndRegisterStationAndResult() {
        return Stream.of(
                Arguments.arguments(new Station("잠실"), new Station("선릉"), new AlreadyExistStationException()),
                Arguments.arguments(new Station("강남"), new Station("선릉"), new NotFoundStationException())
        );
    }

    @Test
    @DisplayName("상행 종점으로 역을 추가한다.")
    void add_station_to_upbound_station() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section = new Section(new Station("잠실"), new Station("선릉"), 10);
        lineSection.add(section);
        Sections sections = new Sections(lineSection);

        Station registStation = new Station("충무로");

        List<Section> expect = List.of(
                new Section(new Station("잠실"), new Station("선릉"), 10),
                new Section(new Station("충무로"), new Station("잠실"), 10)
        );

        // when
        sections.addSection(sections.findUpBoundStation(), "left", registStation, 10);

        // then
        assertThat(sections.getSections()).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("하행 종점으로 역을 추가한다.")
    void add_station_to_downbound_station() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section = new Section(new Station("잠실"), new Station("선릉"), 10);
        lineSection.add(section);
        Sections sections = new Sections(lineSection);

        Station registStation = new Station("충무로");

        List<Section> expect = List.of(
                new Section(new Station("잠실"), new Station("선릉"), 10),
                new Section(new Station("선릉"), new Station("충무로"), 10)
        );

        // when
        sections.addSection(sections.findDownBoundStation(), "right", registStation, 10);

        // then
        assertThat(sections.getSections()).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("구간 중간에 역을 추가한다.(왼쪽으로)")
    void add_station_to_inter_section_by_left() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section1 = new Section(new Station("잠실"), new Station("선릉"), 10);
        Section section2 = new Section(new Station("선릉"), new Station("강남"), 10);
        lineSection.add(section1);
        lineSection.add(section2);
        Sections sections = new Sections(lineSection);

        List<Section> expect = List.of(
                new Section(new Station("선릉"), new Station("강남"), 10),
                new Section(new Station("잠실"), new Station("역삼"), 5),
                new Section(new Station("역삼"), new Station("선릉"), 5)
        );

        // when
        sections.addSection(section1.getRightStation(), "left", new Station("역삼"), 5);

        // then
        assertThat(sections.getSections()).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("구간 중간에 역을 추가한다.(오른쪽)")
    void add_station_to_inter_section_by_right() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section1 = new Section(new Station("잠실"), new Station("선릉"), 10);
        Section section2 = new Section(new Station("선릉"), new Station("강남"), 10);
        lineSection.add(section1);
        lineSection.add(section2);
        Sections sections = new Sections(lineSection);

        List<Section> expect = List.of(
                new Section(new Station("잠실"), new Station("선릉"), 10),
                new Section(new Station("선릉"), new Station("교대"), 5),
                new Section(new Station("교대"), new Station("강남"), 5)
        );

        // when
        sections.addSection(section1.getRightStation(), "right", new Station("교대"), 5);

        // then
        assertThat(sections.getSections()).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("새로 추가되는 구간이 기존 구간보다 길면 예외를 반환한다.")
    void exception_evoke_new_section_distance_is_longer_than_exist_station() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section = new Section(new Station("잠실"), new Station("선릉"), 10);
        lineSection.add(section);
        Sections sections = new Sections(lineSection);

        Station registStation = new Station("충무로");

        // when + then
        assertThatThrownBy(() -> sections.addSection(sections.findUpBoundStation(), "right", registStation, 10))
                .isInstanceOf(InvalidNewSectionDistanceException.class);
    }

    @Test
    @DisplayName("양 끝 역을 삭제한다.")
    void delete_bound_station() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section1 = new Section(new Station("잠실"), new Station("선릉"), 10);
        Section section2 = new Section(new Station("선릉"), new Station("강남"), 10);
        lineSection.add(section1);
        lineSection.add(section2);
        Sections sections = new Sections(lineSection);

        Station deleteStation = new Station("잠실");
        List<Section> expect = List.of(new Section(new Station("선릉"), new Station("강남"), 10));

        // when
        sections.deleteSection(deleteStation);

        // then
        assertThat(sections.getSections()).isEqualTo(expect);
    }

    @Test
    @DisplayName("중간 역을 삭제한다.")
    void delete_inter_station() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section1 = new Section(new Station("잠실"), new Station("선릉"), 10);
        Section section2 = new Section(new Station("선릉"), new Station("강남"), 10);
        lineSection.add(section1);
        lineSection.add(section2);
        Sections sections = new Sections(lineSection);

        Station station = new Station("선릉");

        List<Section> expect = List.of(new Section(section1.getLeftStation(), section2.getRightStation(), section1.getDistance() + section2.getDistance()));

        // when
        sections.deleteSection(station);

        // then
        assertThat(sections.getSections()).isEqualTo(expect);
    }

    @Test
    @DisplayName("삭제하려는 역이 없으면 에러를 발생시킨다.")
    void exception_evoke_when_delete_station_not_found() {
        // given
        List<Section> lineSection = new ArrayList<>();
        Section section1 = new Section(new Station("잠실"), new Station("선릉"), 10);
        Section section2 = new Section(new Station("선릉"), new Station("강남"), 10);
        lineSection.add(section1);
        lineSection.add(section2);
        Sections sections = new Sections(lineSection);

        Station station = new Station("충무로");

        // when + then
        assertThatThrownBy(() -> sections.deleteSection(station))
                .isInstanceOf(NotFoundStationException.class);
    }


    @Test
    @DisplayName("상행 종점과 하행 종점을 찾는다.")
    void find_upper_bound_station() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );

        Sections lineSection = new Sections(sections);

        // when
        Station upBoundStation = lineSection.findUpBoundStation();
        Station downBoundStation = lineSection.findDownBoundStation();

        // then
        assertThat(upBoundStation.getName()).isEqualTo(sections.get(0).getLeftStation().getName());
        assertThat(downBoundStation.getName()).isEqualTo(sections.get(1).getRightStation().getName());
    }

    @Test
    @DisplayName("정렬된 순서로 역들을 반환한다.(상향종점 -> 하향종점)")
    void sort_station() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(3L, "강남"), new Station(4L, "을지로"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );
        Sections lineSection = new Sections(sections);

        List<Station> expect = List.of(
                new Station(1L, "잠실"),
                new Station(2L, "선릉"),
                new Station(3L, "강남"),
                new Station(4L, "을지로"));

        // when
        List<Station> result = lineSection.sortStation();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }
}
