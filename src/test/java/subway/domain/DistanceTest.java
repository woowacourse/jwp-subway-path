package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exceptions.IllegalDistanceException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest(name = "거리가 {0}(양의 정수가 아니면) 예외가 발생한다")
    @ValueSource(ints = {0, -1})
    void init_throw(final int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalDistanceException.class);
    }

    @ParameterizedTest(name = "거리가 {0}(양의 정수이면) 예외가 발생하지 않는다")
    @ValueSource(ints = {1, 2, 3, 100000000})
    void init(final int value) {
        assertThatCode(() -> new Distance(value))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("10 - 6은 4이다.")
    void sub() {
        final Distance ten = new Distance(10);
        final Distance six = new Distance(6);

        assertThat(ten.sub(six).getValue()).isEqualTo(4);
    }

    @Test
    @DisplayName("결과가 음수이면 예외를 발생한다.")
    void sub_throw() {
        final Distance six = new Distance(6);
        final Distance ten = new Distance(10);

        assertThatThrownBy(() -> six.sub(ten))
                .isInstanceOf(IllegalDistanceException.class);
    }
}