package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.vo.Distance;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    @DisplayName("양의 정수가 아닌 값을 입력하면 예외가 발생한다.")
    void validateTest(int input) {
        Assertions.assertThatThrownBy(() -> new Distance(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 항상 양의 정수여야 합니다.");
    }

}
