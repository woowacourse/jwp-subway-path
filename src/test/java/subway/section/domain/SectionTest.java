package subway.section.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    void distanceTest() {
        assertThatThrownBy(() -> new Section(1L, 2L, 3L, 4L, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양의 정수만 가능합니다.");
    }
}
