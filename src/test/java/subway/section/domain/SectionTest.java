package subway.section.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.station.domain.Station;

import static org.assertj.core.api.Assertions.*;

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
}
