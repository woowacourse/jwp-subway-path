package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    @DisplayName("Section을 생성할 수 있다.")
    void create() {
        // given
        final String upStationName = "용산역";
        final String downStationName = "죽전역";
        final int distance = 10;
        // when
        final Section section = new Section(new Station(upStationName), new Station(downStationName), distance);
        // then
        assertEquals(upStationName, section.getUpStation().getName());
        assertEquals(downStationName, section.getDownStation().getName());
        assertEquals(distance, section.getDistance());
    }

    @Test
    @DisplayName("거리가 음수일 경우 예외가 발생한다.")
    void validateNegativeDistance() {
        // given
        final String upStationName = "용산역";
        final String downStationName = "죽전역";
        final int distance = -1;
        // when & then
        assertThatThrownBy(() -> new Section(new Station(upStationName), new Station(downStationName), distance))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
