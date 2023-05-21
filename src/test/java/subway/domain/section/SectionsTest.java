package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("주어진 섹션을 포함하는 상위섹션을 찾는다.")
    void findSectionContainSectionTest() {
        // given
        Sections sections = new Sections(List.of(SECTION_대림역_TO_잠실역, SECTION_잠실역_TO_건대역, SECTION_건대역_TO_성수역));
        Section section = SECTION_잠실역_TO_강변역;
        Section expectSection = SECTION_잠실역_TO_건대역;

        // when
        Optional<Section> findSection = sections.findSectionContainSection(section);

        // then
        assertThat(findSection).contains(expectSection);
    }

    @Test
    @DisplayName("주어진 역이 포함된 섹션들을 가져온다.")
    void findSectionsContainStationTest() {
        // given
        Sections sections = new Sections(List.of(SECTION_대림역_TO_잠실역, SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역, SECTION_건대역_TO_성수역));
        Station station = STATION_강변역;
        List<Section> expectSections = List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역);

        // when
        List<Section> findSections = sections.findSectionsContainStation(station);

        // then
        assertThat(findSections).isEqualTo(expectSections);
    }

    @Test
    @DisplayName("Sections에 포함된 모든 역을 가져온다.")
    void getContainingStationNamesTest() {
        // given
        Sections sections = new Sections(List.of(SECTION_대림역_TO_잠실역, SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역, SECTION_건대역_TO_성수역));
        Set<String> expect = Set.of(STATION_대림역_NAME, STATION_잠실역_NAME, STATION_강변역_NAME, STATION_건대역_NAME, STATION_성수역_NAME);

        // when
        Set<String> containingStationNames = sections.getContainingStationNames();

        // then
        assertThat(containingStationNames).isEqualTo(expect);
    }
}