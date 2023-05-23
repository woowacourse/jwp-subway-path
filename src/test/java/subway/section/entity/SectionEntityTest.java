package subway.section.entity;

import org.junit.jupiter.api.Test;
import subway.entity.SectionEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionEntityTest {

    @Test
    void distanceTest() {
        assertThatThrownBy(() -> new SectionEntity(1L, 2L, 3L, 4L, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양의 정수만 가능합니다.");
    }
}
