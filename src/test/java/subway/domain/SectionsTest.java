package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Sections sections;

    private Station station1;
    private Station station2;
    private Station station3;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "잠실역");
        station2 = new Station(2L, "강남역");
        station3 = new Station(3L, "선릉역");

        Section section1 = new Section(station1, station2, 5);
        Section section2 = new Section(station2, station3, 7);
        sections = new Sections(new LinkedList<>(List.of(section1, section2)));
    }

    @DisplayName("구간이 해당 역을 가지고 있으면 true를 반환한다.")
    @Test
    void hasStationTrue() {
        // when, then
        assertSoftly(softly -> {
            softly.assertThat(sections.hasStationInSections(station1)).isTrue();
            softly.assertThat(sections.hasStationInSections(station2)).isTrue();
            softly.assertThat(sections.hasStationInSections(station3)).isTrue();
        });
    }

    @DisplayName("구간이 해당 역을 가지고 있지 않으면 false를 반환한다.")
    @Test
    void hasStationFalse() {
        // given
        Station station = new Station(5L, "신림역");

        // when, then
        assertThat(sections.hasStationInSections(station)).isFalse();
    }

    @DisplayName("구간의 왼쪽 역과 같은 역이 주어지면 true를 반환한다.")
    @Test
    void hasLeftStationInSectionTrue() {
        // given
        Station station = new Station(1L, "잠실역");

        // when, then
        assertThat(sections.hasLeftStationInSections(station)).isTrue();
    }

    @DisplayName("구간의 오른쪽 역과 같은 역이 주어지면 true를 반환한다.")
    @Test
    void hasRightStationInSectionTrue() {
        // given
        Station station = new Station(3L, "선릉역");

        // when, then
        assertThat(sections.hasRightStationInSections(station)).isTrue();
    }

    @DisplayName("찾는 역이 구간의 왼쪽 역에 해당하는 구간을 반환한다.")
    @Test
    void findSectionByLeftStation() {
        // given
        Station station = new Station(1L, "잠실역");

        // when
        Section section = sections.findSectionByLeftStation(station);

        // then
        assertThat(section.getLeft()).isEqualTo(station);
    }

    @DisplayName("찾는 역이 구간의 오른쪽 역에 해당하는 구간을 반환한다.")
    @Test
    void findSectionByRightStation() {
        // given
        Station station = new Station(3L, "선릉역");

        // when
        Section section = sections.findSectionByRightStation(station);

        // then
        assertThat(section.getRight()).isEqualTo(station);
    }
}
