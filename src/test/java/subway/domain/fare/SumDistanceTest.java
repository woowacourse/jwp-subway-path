package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SumDistanceTest {

    @Test
    @DisplayName("거리의 합을 구한다.")
    void add() {
        // given
        final SumDistance distance = new SumDistance(0);

        // when
        final SumDistance result = distance.add(new SumDistance(10));

        // then
        assertThat(result.distance())
            .isEqualTo(10);
    }

    @Test
    @DisplayName("거리의 차를 구한다.")
    void subtract() {
        // given
        final SumDistance distance = new SumDistance(7);

        // when
        final SumDistance result = distance.subtract(new SumDistance(3));

        // then
        assertThat(result.distance())
            .isEqualTo(4);
    }

    @Test
    @DisplayName("거리에 대해 나눈 뒤, 올림한 값을 반환한다.")
    void divideAndCeil() {
        // given
        final SumDistance distance = new SumDistance(11);

        // when
        final SumDistance result = distance.divideAndCeil(new SumDistance(3));

        // then
        assertThat(result.distance())
            .isEqualTo(4);
    }

    @ParameterizedTest(name = "대상 거리가 주어진 거리보다 작으면 true, 크거나 같으면 false를 반환한다.")
    @CsvSource(value = {"10:true", "3:false", "5:false"}, delimiter = ':')
    void lessThan(final int distance, final boolean expected) {
        // given
        final SumDistance target = new SumDistance(5);

        // expected
        assertThat(target.lessThan(new SumDistance(distance)))
            .isSameAs(expected);
    }

    @ParameterizedTest(name = "대상 거리가 주어진 거리보다 더 작거나 같으면 true, 크면 true를 반환한다.")
    @CsvSource(value = {"5:true", "10:true", "3:false"}, delimiter = ':')
    void lessAndEqualsThan(final int distance, final boolean expected) {
        // given
        final SumDistance target = new SumDistance(5);

        // expected
        assertThat(target.lessAndEqualsThan(new SumDistance(distance)))
            .isSameAs(expected);
    }
}
