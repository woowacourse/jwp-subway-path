package subway.section.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 추가할_역이_왼쪽인_구간_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 3L);
        final String leftAdditionalStation = "선릉역";
        final long additionalDistance = 5L;
        
        // when
        final Section createdLeftSection = section.createLeftSection(leftAdditionalStation, additionalDistance);
        
        // then
        assertThat(createdLeftSection).isEqualTo(new Section("선릉역", "강남역", additionalDistance));
    }
}
