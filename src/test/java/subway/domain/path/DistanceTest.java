package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.distance.InvalidDistanceException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    @DisplayName("거리가 음수이면 에러를 발생한다.")
    void distance_is_not_below_zero(int distance) {
        // when + then
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(InvalidDistanceException.class);

    }

    @Test
    @DisplayName("주어진 거리에서 기존 거리의 차를 반환한다.")
    void return_difference_distance() {
        // given
        Distance distance = new Distance(10);

        // when
        int result = distance.calculateOverFare(5);

        // then
        assertThat(result).isEqualTo(5);

    }

    @ParameterizedTest(name = "{displayName}")
    @CsvSource(value = {"1,false", "11, true", "10,true"})
    @DisplayName("기준 거리가 입력값 이하이면 true를 그렇지 않으면 false를 반환한다.")
    void check_distance_is_under_input_value(int input, boolean expect) {
        // given
        Distance distance = new Distance(10);

        // when
        boolean result = distance.isLowerOrEqual(input);

        // then
        assertThat(result).isEqualTo(expect);
    }

}
