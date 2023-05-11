package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Distance는 ")
class DistanceTest {

    @Test
    @DisplayName("거리 정보를 갖는다.")
    void distanceTest() {
        assertDoesNotThrow(() -> Distance.from(3));
    }

    @Test
    @DisplayName("거리 정보가 양수가 아닐 경우 예외처리한다.")
    void invalidDistanceExceptionTest() {
        assertThatThrownBy(() -> Distance.from(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 거리는 음수 일 수 없습니다.");
    }
}
