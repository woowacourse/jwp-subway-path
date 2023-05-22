package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.DistanceValueValidateException;

class DistanceTest {

    @DisplayName("distance가 1미만이면 IllegalArgumentException을 발생시킨다.")
    @Test
    void whenDistanceUnderOneThrowException() {
        assertThatThrownBy(() -> new Distance(0))
                .isInstanceOf(DistanceValueValidateException.class);
    }

    @DisplayName("distance에 값을 더히는 기능 테스트")
    @Test
    void plusValue() {
        final Distance distance1 = new Distance(10);
        final Distance distance2 = new Distance(5);

        final Distance result = distance1.plusValue(distance2);

        assertThat(result.getValue())
                .isEqualTo(distance1.getValue() + distance2.getValue());
    }

    @DisplayName("distance에 값을 빼는 기능 테스트")
    @Test
    void minusValue() {
        final Distance distance1 = new Distance(10);
        final Distance distance2 = new Distance(5);

        final Distance result = distance1.minusValue(distance2);

        assertThat(result.getValue())
                .isEqualTo(distance1.getValue() - distance2.getValue());
    }
}
