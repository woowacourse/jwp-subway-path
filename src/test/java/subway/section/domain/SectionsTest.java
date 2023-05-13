package subway.section.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {
    
    @Test
    void 최초_역_등록_시_두_역을_동시에_등록한다() {
        // given
        final Sections sections = new Sections();
        final String left = "잠실역";
        final String right = "선릉역";
        final long distance = 3L;
        
        // when
        sections.initAddStation(left, right, distance);
        
        // then
        assertThat(sections.getSections()).contains(new Section(left, right, distance));
    }
    
    @Test
    void 가장_왼쪽에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        final Section secondSection = new Section(second, third, distance);
        final Section thirdSection = new Section(third, fourth, distance);
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(first, Direction.LEFT, additionalStation, additionalDistance);
        
        // then
        final Section additionalSection = new Section(additionalStation, first, additionalDistance);
        assertThat(sections.getSections()).contains(firstSection, secondSection, thirdSection, additionalSection);
    }
    
    @Test
    void 가장_오른쪽에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        final Section secondSection = new Section(second, third, distance);
        final Section thirdSection = new Section(third, fourth, distance);
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(fourth, Direction.RIGHT, additionalStation, additionalDistance);
        
        // then
        final Section additionalSection = new Section(fourth, additionalStation, additionalDistance);
        assertThat(sections.getSections()).contains(firstSection, secondSection, thirdSection, additionalSection);
    }
    
    @Test
    void 방향이_오른쪽일_때_base가_중간역이면_구간_사이에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        final Section secondSection = new Section(second, third, distance);
        final Section thirdSection = new Section(third, fourth, distance);
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(first, Direction.RIGHT, additionalStation, additionalDistance);
        
        // then
        final Section additionalFirstSection = new Section(first, additionalStation, additionalDistance);
        final Section additionalSecondSection = new Section(additionalStation, second, distance - additionalDistance);
        assertThat(sections.getSections()).contains(secondSection, thirdSection, additionalFirstSection, additionalSecondSection);
    }
    
    @Test
    void 방향이_왼쪽일_때_base가_중간역이면_구간_사이에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        final Section secondSection = new Section(second, third, distance);
        final Section thirdSection = new Section(third, fourth, distance);
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(third, Direction.LEFT, additionalStation, additionalDistance);
        
        // then
        final Section additionalFirstSection = new Section(second, additionalStation, additionalDistance);
        final Section additionalSecondSection = new Section(additionalStation, third, distance - additionalDistance);
        assertThat(sections.getSections()).contains(firstSection, thirdSection, additionalFirstSection, additionalSecondSection);
    }
    
    @Test
    void 역이_2개만_존재하는_경우_모두_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        
        final Set<Section> initSections = Set.of(firstSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(second);
        
        // then
        assertThat(sections.getSections()).isEmpty();
    }
    
    @Test
    void 가장_오른쪽_역을_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        final Section secondSection = new Section(second, third, distance);
        final Section thirdSection = new Section(third, fourth, distance);
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(fourth);
        
        // then
        assertThat(sections.getSections()).contains(firstSection, secondSection);
    }
    
    @Test
    void 가장_왼쪽_역을_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        final Section secondSection = new Section(second, third, distance);
        final Section thirdSection = new Section(third, fourth, distance);
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(first);
        
        // then
        assertThat(sections.getSections()).contains(secondSection, thirdSection);
    }
    
    @Test
    void 중간_역을_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance);
        final Section secondSection = new Section(second, third, distance);
        final Section thirdSection = new Section(third, fourth, distance);
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(second);
        
        // then
        final Section combinedSection = new Section(first, third, distance + distance);
        assertThat(sections.getSections()).contains(thirdSection, combinedSection);
    }
    
    @Test
    void 최초_등록_시_구간이_하나라도_존재하면_예외_발생() {
        // given
        final Set<Section> initSections = Set.of(new Section("강남역", "역삼역", 5L));
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.initAddStation("선릉역", "강남역", 3L));
    }
    
    @Test
    void 기준역이_존재하지_않으면_예외_발생() {
        // given
        final Set<Section> initSections = Set.of(new Section("강남역", "역삼역", 5L));
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addStation("선릉역", Direction.RIGHT, "가양역", 3L));
    }
    
    @Test
    void 추가하려는_역이_이미_존재하면_예외_발생() {
        // given
        final Set<Section> initSections = Set.of(new Section("강남역", "역삼역", 5L));
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addStation("역삼역", Direction.RIGHT, "강남역", 3L));
    }
}
