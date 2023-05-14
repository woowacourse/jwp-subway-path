package subway.section.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {
    @Test
    void Section_생성() {
        // given
        final String leftStationName = "강남역";
        final String rightStationName = "역삼역";
        final long distance = 3L;
        
        // when
        final Section section = new Section(leftStationName, rightStationName, distance);
        
        // then
        assertThat(section).isNotNull();
    }
    
    @Test
    void 자신의_구간과_추가할_역이_왼쪽인_구간_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 3L);
        final String base = "강남역";
        final String additional = "선릉역";
        final long distance = 5L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.LEFT, additional, distance);
        
        // then
        final Section additionalSection = new Section(additional, base, 5L);
        assertThat(createdLeftSection).contains(section, additionalSection);
    }
    
    @Test
    void 자신의_구간과_추가할_역이_오른쪽인_구간_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 3L);
        final String base = "역삼역";
        final String additional = "선릉역";
        final long distance = 5L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.RIGHT, additional, distance);
        
        // then
        final Section additionalSection = new Section(base, additional, 5L);
        assertThat(createdLeftSection).contains(section, additionalSection);
    }
    
    @Test
    void 추가할_방향이_오른쪽일_때_분리된_구간을_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 5L);
        final String base = "강남역";
        final String additional = "선릉역";
        final long distance = 2L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.RIGHT, additional, distance);
        
        // then
        final Section leftAdditionalSection = new Section(base, additional, 2L);
        final Section rightAdditionalSection = new Section(additional, "역삼역", 3L);
        assertThat(createdLeftSection).contains(leftAdditionalSection, rightAdditionalSection);
    }
    
    @Test
    void 추가할_방향이_왼쪽일_때_분리된_구간을_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 5L);
        final String base = "역삼역";
        final String additional = "선릉역";
        final long distance = 2L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.LEFT, additional, distance);
        
        // then
        final Section leftAdditionalSection = new Section("강남역", additional, 2L);
        final Section rightAdditionalSection = new Section(additional, base, 3L);
        assertThat(createdLeftSection).contains(leftAdditionalSection, rightAdditionalSection);
    }
    
    @ParameterizedTest(name = "{displayName} : distance = {0}")
    @ValueSource(longs = {5, 6})
    void 분리될_Section이_추가될_Section보다_거리가_짧으면_예외_발생(final Long distance) {
        // given
        final Section section = new Section("강남역", "역삼역", 5L);
        final String base = "역삼역";
        final String additional = "선릉역";
        
        // when
        assertThatIllegalArgumentException()
                .isThrownBy(() -> section.getAdditionalSectionsWithOneself(base, Direction.LEFT, additional, distance));
    }
}
