package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InValidDistanceException;

class DistanceTest {
    @ParameterizedTest(name = "두 역 사이 거리는 100km 이하 양의 정수만 가능하다.")
    @ValueSource(ints = {1, 100})
    void createDistanceSuccessTest(int distance) {
        assertDoesNotThrow(() -> new Distance(distance));
    }

    @ParameterizedTest(name = "두 역 사이 거리는 100km 이하 양의 정수만 가능하다.")
    @ValueSource(ints = {-1, 0, 101})
    void createDistanceFailTestByRange(int distance) {
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(InValidDistanceException.class);
    }
}
