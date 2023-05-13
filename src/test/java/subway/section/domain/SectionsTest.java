package subway.section.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
}
