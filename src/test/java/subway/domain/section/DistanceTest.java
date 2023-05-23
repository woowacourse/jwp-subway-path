package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceTest {

    @DisplayName("양의 정수가 아닌 값이 들어오면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"-1", "0"})
    void distanceTest(final int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양의 정수만 가능합니다.");
    }

    @DisplayName("거리가 작거나 같으면 true를 반환한다.")
    @ParameterizedTest
    @CsvSource({"10", "11"})
    void isNotGreaterThanTrue(final int value) {
        final Distance distance = new Distance(10);
        final boolean result = distance.isNotGreaterThan(new Distance(value));
        assertThat(result).isTrue();
    }

    @DisplayName("거리가 크면 false를 반환한다.")
    @Test
    void isNotGreaterThanFalse() {
        final Distance distance = new Distance(10);
        final boolean result = distance.isNotGreaterThan(new Distance(9));
        assertThat(result).isFalse();
    }

    @DisplayName("두 거리의 차이를 반환한다.")
    @Test
    void subtract() {
        final Distance distance = new Distance(10);
        final Distance result = distance.subtract(new Distance(9));
        assertThat(result).isEqualTo(new Distance(1));
    }
}
