package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 100})
    void 객체_정상_생성(final int value) {
        assertThatNoException().isThrownBy(() -> new Distance(value));
    }

    @Test
    void 값이_음수로_들어오면_예외를_발생한다() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Distance(-1)
        );
    }

    @Test
    void 거리를_뺀다() {
        // given
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);

        // when
        final Distance result = distance1.subtract(distance2);

        // then
        assertThat(result.getValue()).isEqualTo(5);
    }

    @Test
    void 뺐을_때_음수가_되면_예외를_발생한다() {
        // given
        Distance distance1 = new Distance(5);
        Distance distance2 = new Distance(10);

        // expect
        assertThatIllegalArgumentException().isThrownBy(
                () -> distance1.subtract(distance2)
        );
    }

    @Test
    void 거리를_합한다() {
        // given
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);

        // when
        final Distance result = distance1.add(distance2);

        // then
        assertThat(result.getValue()).isEqualTo(15);
    }

    @Test
    void 값이_같으면_같은_객체이다() {
        // given
        Distance distance1 = new Distance(5);
        Distance distance2 = new Distance(5);

        // expect
        assertThat(distance1.equals(distance2)).isTrue();
    }

    @Test
    void 값이_다르면_다른_객체이다() {
        // given
        Distance distance1 = new Distance(1);
        Distance distance2 = new Distance(5);

        // expect
        assertThat(distance1.equals(distance2)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"10, 15, true", "11, 15, false", "5, 11, true", "5, 10, true"})
    void 거리_값이_범위에_포함되는지_판별한다(final double minDistanceValue, final double maxDistanceValue, final boolean expect) {
        // given
        Distance minDistance = new Distance(minDistanceValue);
        Distance maxDistance = new Distance(maxDistanceValue);
        Distance distance = new Distance(10);

        // when
        boolean result = distance.isInclude(minDistance, maxDistance);

        // then
        assertThat(result).isEqualTo(expect);
    }
}
