package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.GlobalException;

class DistanceTest {
    @ParameterizedTest(name = "두 역 사이 거리는 양의 정수만 가능하다.")
    @ValueSource(ints = {1, 10,100})
    void createDistanceSuccessTest(int distance) {
        assertDoesNotThrow(() -> new Distance(distance));
    }

    @ParameterizedTest(name = "두 역 사이 거리는 양의 정수만 가능하다.")
    @ValueSource(ints = {-1, 0})
    void createDistanceFailTestByRange(int distance) {
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(GlobalException.class)
                .hasMessage("역간 거리는 10km이하 양의 정수만 가능합니다.");
    }
}
