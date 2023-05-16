package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Distance는 ")
class DistanceTest {

    @Test
    @DisplayName("거리 정보를 갖는다.")
    void distanceTest() {
        assertDoesNotThrow(() -> Distance.from(3));
    }

    @ParameterizedTest
    @DisplayName("거리 정보가 양수가 아닐 경우 예외처리한다.")
    @ValueSource(ints = {-1, 0})
    void invalidDistanceExceptionTest(int input) {
        assertThatThrownBy(() -> Distance.from(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 거리는 양의 정수만 가능합니다.");
    }
}
