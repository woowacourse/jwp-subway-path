package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.exception.IllegalDistanceException;

class DistanceTest {

    @Test
    @DisplayName("0km 이하의 거리는 생성할 수 없다.")
    void create_distance_fail() {
        assertThatThrownBy(() -> Distance.from(0))
                .isInstanceOf(IllegalDistanceException.class);
    }

    @Test
    @DisplayName("두 거리의 값을 더할 수 있다.")
    void add() {
        // given
        Distance distance = Distance.from(1);
        Distance other = Distance.from(0.5);

        // when, then
        assertThat(distance.add(other)).isEqualTo(Distance.from(1.5));
    }

    @Test
    @DisplayName("두 거리의 값을 뺄 수 있다.")
    void subtract() {
        // given
        Distance distance = Distance.from(1);
        Distance other = Distance.from(0.5);

        // when, then
        assertThat(distance.subtract(other)).isEqualTo(Distance.from(0.5));
    }

    @Test
    @DisplayName("두 거리를 나누고, 올림할 수 있다.")
    void divideAndCeil() {
        // given
        Distance distance = Distance.from(6);
        Distance other = Distance.from(2.5);

        // when, then
        assertThat(distance.divideAndCeil(other)).isEqualTo(Distance.from(3));
    }

    @ParameterizedTest
    @CsvSource(value = {"6:true", "6.1:true", "5.9:false"}, delimiter = ':')
    void isLessThanOrEqualTo(double value, boolean expectedResult) {
        // given
        Distance distance = Distance.from(6);
        Distance other = Distance.from(value);

        // when, then
        assertThat(distance.isLessThanOrEqualTo(other)).isEqualTo(expectedResult);
    }
}