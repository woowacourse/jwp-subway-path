package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.factory.SectionsFactory.createSections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("역이 존재하는지 확인한다.")
    void returns_is_exist_station() {
        // given
        Sections sections = createSections();

        // when
        boolean existStation = sections.isExistStation(new Station("매봉역"));

        // then
        assertThat(existStation).isFalse();
    }

    @Test
    @DisplayName("역이 Sections 내부에 상행으로서 존재하는지 확인한다.")
    void returns_is_exist_as_up_station() {
        // given
        Sections sections = createSections();

        // when
        boolean result = sections.isExistAsUpStation(new Station("잠실역"));

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("상행으로 존재하는 section을 반환한다.")
    void returns_section_when_section_with_up_station() {
        // given
        Sections sections = createSections();
        Station station = new Station("잠실역");

        // when
        Section result = sections.findSectionWithUpStation(station);

        // then
        assertThat(result.getUpStation()).isEqualTo(station);
    }

    @Test
    @DisplayName("하행으로 존재하는 section을 반환한다.")
    void returns_section_when_section_with_down_station() {
        // given
        Sections sections = createSections();
        Station station = new Station("잠실새내역");

        // when
        Section result = sections.findSectionWithDownStation(station);

        // then
        assertThat(result.getDownStation()).isEqualTo(station);
    }
}
