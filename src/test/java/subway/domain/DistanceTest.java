package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidDistanceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @Test
    @DisplayName("생성 시에 길이가 0이하인 length로 생성되면 예외가 발생한다.")
    void construct_fail_when_length_is_not_positive() {
        assertThatThrownBy(() -> new Distance(-1))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("덧셈")
    void add_test() {
        // given
        final Distance distance1 = new Distance(10);
        final Distance distance2 = new Distance(5);

        // when
        final Distance added = distance1.add(distance2);

        // then
        assertThat(added).isEqualTo(new Distance(15));
    }

    @Test
    @DisplayName("뺄셈")
    void subtract_test() {
        // given
        final Distance distance1 = new Distance(10);
        final Distance distance2 = new Distance(5);

        // when
        final Distance added = distance1.subtract(distance2);

        // then
        assertThat(added).isEqualTo(new Distance(5));
    }

    @Test
    @DisplayName("뺄셈을 해서 음수가 되면 예외가 발생한다.")
    void subtract_test_error_when_return_negative_number() {
        // given
        final Distance distance1 = new Distance(5);
        final Distance distance2 = new Distance(10);

        // when, then
        assertThatThrownBy(() -> distance1.subtract(distance2))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Nested
    @DisplayName("대소 비교 테스트")
    class Comparison {

        @Test
        @DisplayName("크다 테스트 - true")
        void biggerThan_true() {
            // given
            final Distance distance1 = new Distance(10);
            final Distance distance2 = new Distance(5);

            // when
            final boolean result = distance1.isGreaterThan(distance2);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("크다 테스트 - false")
        void biggerThan_false() {
            // given
            final Distance distance1 = new Distance(5);
            final Distance distance2 = new Distance(10);

            // when
            final boolean result = distance1.isGreaterThan(distance2);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("작거나 같다 테스트 - true - 작다")
        void lessThanOrEqualTo_true_less() {
            // given
            final Distance distance1 = new Distance(5);
            final Distance distance2 = new Distance(10);

            // when
            final boolean result = distance1.isLessThanOrEqualTo(distance2);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("작거나 같다 테스트 - true - 같다")
        void lessThanOrEqualTo_true_equal() {
            // given
            final Distance distance1 = new Distance(5);
            final Distance distance2 = new Distance(5);

            // when
            final boolean result = distance1.isLessThanOrEqualTo(distance2);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("작거나 같다 테스트 - false")
        void lessThanOrEqualTo_false() {
            // given
            final Distance distance1 = new Distance(10);
            final Distance distance2 = new Distance(5);

            // when
            final boolean result = distance1.isLessThanOrEqualTo(distance2);

            // then
            assertThat(result).isFalse();
        }
    }
}
