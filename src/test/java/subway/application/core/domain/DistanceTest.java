package subway.application.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.application.core.exception.DistanceNotPositiveException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("역 간의 거리는 0보다 작거나 같을 수 없다")
    void constructor(int distance) {
        //given, when, then
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(DistanceNotPositiveException.class);
    }
}
