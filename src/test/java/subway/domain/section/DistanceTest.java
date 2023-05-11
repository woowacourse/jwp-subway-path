package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @Test
    @DisplayName("역 사이의 거리가 100km 초과면 예외가 발생한다.")
    void validateDistanceTest() {
        // given
        int invalidDistance = 101;

        // when, then
        assertThatThrownBy(() -> new Distance(invalidDistance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 사이 거리는 100km 이하여야 합니다.");
    }
}