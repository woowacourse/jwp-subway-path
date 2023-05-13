package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.DistanceForkedException;
import subway.exception.SectionNotConnectException;
import subway.exception.SectionDuplicatedException;
import subway.exception.SectionNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.factory.SectionFactory.createSection;
import static subway.factory.SectionsFactory.createSections;

class SectionsTest {

    @Test
    @DisplayName("선로를 추가시킨다.")
    void add_section_success() {
        // given
        Sections sections = createSections();
        Section section = createSection(new Station("종합운동장역"), new Station("삼성역"));

        // when
        sections.addSection(section);

        // then
        assertAll(
                () -> assertThat(sections.getSections().size()).isEqualTo(3),
                () -> assertThat(sections.getSections().get(2).getUpStation()).isEqualTo(section.getUpStation())
        );
    }

    @Test
    @DisplayName("라인에 해당하는 선로가 없다면 예외를 발생시킨다.")
    void throws_exception_when_line_not_matched() {
        // given
        Sections sections = createSections();
        Section section = new Section(new Station("매봉역"), new Station("선릉역"), 10L);

        // when & then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(SectionNotConnectException.class);
    }

    @Test
    @DisplayName("중복된 선로라면 예외를 발생시킨다.")
    void throws_exception_when_section_duplicated() {
        // given
        Sections sections = createSections();
        Section section = new Section(new Station("잠실역"), new Station("잠실새내역"), 10L);

        // when & then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(SectionDuplicatedException.class);
    }

    @Test
    @DisplayName("갈래길이면 예외를 발생시킨다.")
    void throws_exception_when_forked_road() {
        // given
        Sections sections = createSections();
        Section section = new Section(new Station("잠실새내역"), new Station("선릉역"), 10L);

        // when & then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(DistanceForkedException.class);
    }

    @Test
    @DisplayName("선로를 제거한다.")
    void delete_section_success() {
        // given
        Sections sections = createSections();
        Station targetStation = new Station("잠실새내역");

        // when
        sections.deleteSectionByStation(targetStation);

        // then
        assertAll(
                () -> assertThat(sections.getSections().size()).isEqualTo(1),
                () -> assertThat(sections.getSections().get(0).getUpStation().getName()).isEqualTo("잠실역")
        );
    }

    @Test
    @DisplayName("제거하고자 하는 선로가 없다면 예외를 발생시킨다.")
    void throws_exception_when_section_not_found() {
        // given
        Sections sections = createSections();
        Station targetStation = new Station("매봉역");

        // when & then
        assertThatThrownBy(() -> sections.deleteSectionByStation(targetStation))
                .isInstanceOf(SectionNotFoundException.class);
    }
}
