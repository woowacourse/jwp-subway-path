package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SectionsTest {

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
    @DisplayName("주어진 역이 노선 구간에 포홤되어 있으면 true 그렇지 않으면 false를 반환한다.")
    void find_all_station() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );
        Sections lineSection = new Sections(sections);

        Station jamsil = new Station("잠실");

        // when
        boolean result = lineSection.isContainStation(jamsil);


        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("한 라인에서 한 역과 방향이 주어지면 그 구간을 찾아낸다.")
    void find_section_with_station_and_direction() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );
        Sections lineSection = new Sections(sections);

        Station baseStation = new Station(2L, "선릉");
        String direction = "right";

        // when
        Section result = lineSection.findSection(baseStation, direction);

        // then
        assertThat(result.getLeftStation().getName()).isEqualTo(sections.get(1).getLeftStation().getName());
        assertThat(result.getRightStation().getName()).isEqualTo(sections.get(1).getRightStation().getName());

    }

    @Test
    @DisplayName("종점역이 주어지면 종점역을 포함하는 구간을 찾아낸다.")
    void find_last_section_with_last_station() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );
        Sections lineSection = new Sections(sections);

        Station baseStation = new Station(1L, "잠실");

        // when
        Section result = lineSection.findBoundSection(baseStation);

        assertThat(result.getLeftStation()).isEqualTo(sections.get(0).getLeftStation());
        assertThat(result.getRightStation()).isEqualTo(sections.get(0).getRightStation());
    }

    @Test
    @DisplayName("중간역이 주어지면 중간역을 포함하는 구간을 모두 찾아낸다.")
    void find_inter_section_by_inter_station() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );
        Sections lineSection = new Sections(sections);

        Station station = new Station(2L, "선릉");

        // when
        List<Section> result = lineSection.findInterSections(station);

        // then
        assertThat(result.get(0).getLeftStation()).isEqualTo(sections.get(0).getLeftStation());
        assertThat(result.get(0).getRightStation()).isEqualTo(sections.get(0).getRightStation());
        assertThat(result.get(0).getDistance()).isEqualTo(sections.get(0).getDistance());
        assertThat(result.get(1).getLeftStation()).isEqualTo(sections.get(1).getLeftStation());
        assertThat(result.get(1).getRightStation()).isEqualTo(sections.get(1).getRightStation());
        assertThat(result.get(1).getDistance()).isEqualTo(sections.get(1).getDistance());
    }

    @Test
    @DisplayName("두개의 구간이 주어지면 하나의 구간으로 합쳐준다.")
    void combine_section_by_given_sections() {
        // given
        List<Section> sections = List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10)
        );
        Sections lineSection = new Sections(sections);

        // when
        Section result = lineSection.linkSections(sections);

        // then
        assertThat(result.getLeftStation()).isEqualTo(sections.get(0).getLeftStation());
        assertThat(result.getRightStation()).isEqualTo(sections.get(1).getRightStation());
        assertThat(result.getDistance()).isEqualTo(sections.get(0).getDistance() + sections.get(1).getDistance());
    }
}
