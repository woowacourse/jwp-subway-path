//package subway.domain;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import subway.domain.subway.Section;
//import subway.domain.subway.Sections;
//import subway.domain.subway.Station;
//import subway.exception.SectionInvalidException;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static subway.factory.SectionFactory.createSection;
//import static subway.factory.SectionsFactory.createSections;
//
//class SectionsTest {
//
//    @Test
//    @DisplayName("역이 존재하는지 확인한다.")
//    void returns_is_exist_station() {
//        // given
//        Sections sections = createSections();
//
//        // when
//        boolean existStation = sections.isExistStation(new Station("매봉역"));
//
//        // then
//        assertThat(existStation).isFalse();
//    }
//
//    @ParameterizedTest
//    @CsvSource({"false, false", "true, true"})
//    @DisplayName("역이 모두 존재하거나, 역이 모두 존재하지 않으면서 Sections가 비어있지 않을 경우 예외를 던진다.")
//    void throws_exception_when_invalid_stations(final boolean isExistUpStation, final boolean isExistDownStation) {
//        // given
//        Sections sections = new Sections(List.of(createSection()));
//
//        // when & then
//        assertThatThrownBy(() -> sections.validateSection(isExistUpStation, isExistDownStation))
//                .isInstanceOf(SectionInvalidException.class);
//    }
//
//    @Test
//    @DisplayName("역이 Sections 내부에 상행으로서 존재하는지 확인한다.")
//    void returns_is_exist_as_up_station() {
//        // given
//        Sections sections = createSections();
//
//        // when
//        boolean result = sections.isExistAsUpStation(new Station("잠실역"));
//
//        // then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    @DisplayName("상행으로 존재하는 section을 반환한다.")
//    void returns_section_when_section_with_up_station() {
//        // given
//        Sections sections = createSections();
//        Station station = new Station("잠실역");
//
//        // when
//        Section result = sections.findSectionWithUpStation(station);
//
//        // then
//        assertThat(result.getUpStation()).isEqualTo(station);
//    }
//
//    @Test
//    @DisplayName("하행으로 존재하는 section을 반환한다.")
//    void returns_section_when_section_with_down_station() {
//        // given
//        Sections sections = createSections();
//        Station station = new Station("잠실새내역");
//
//        // when
//        Section result = sections.findSectionWithDownStation(station);
//
//        // then
//        assertThat(result.getDownStation()).isEqualTo(station);
//    }
//}
