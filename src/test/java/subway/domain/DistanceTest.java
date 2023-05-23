package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.line.Distance;
import subway.exception.InvalidDistanceException;

class DistanceTest {

    @DisplayName("거리 객체를 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 99})
    void createDistance(int value) {
        // given
        Distance distance = new Distance(value);

        // when, then
        assertThat(distance.getValue()).isEqualTo(value);
    }

    @DisplayName("거리 값이 0~100 사이가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 100})
    void createDistanceFailWithWrongValue(int value) {
        // when, then
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessageContaining("역 간의 거리는 0~100 사이만 가능합니다.");
    }
}
