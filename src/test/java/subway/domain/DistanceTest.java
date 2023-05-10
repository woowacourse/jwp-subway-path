package subway.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Distance는 ")
class DistanceTest {

    @Test
    @DisplayName("거리 정보를 갖는다.")
    void distanceTest() {
        assertDoesNotThrow(() -> Distance.from(3));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("거리 정보가 양수가 아닐 경우 예외처리한다.")
    void invalidDistanceExceptionTest(int distance) {
        assertThatThrownBy(() -> Distance.from(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 거리는 양의 정수여야 합니다.");
    }
}
