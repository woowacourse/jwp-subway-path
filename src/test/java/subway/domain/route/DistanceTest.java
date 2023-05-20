package subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.route.Distance;

class DistanceTest {

    @Test
    @DisplayName("거리의 합을 구한다.")
    void add() {
        // given
        final Distance distance = new Distance(0);

        // when
        final Distance result = distance.add(new Distance(10));

        // then
        assertThat(result.distance())
            .isEqualTo(10);
    }

    @Test
    @DisplayName("거리의 차를 구한다.")
    void subtract() {
        // given
        final Distance distance = new Distance(7);

        // when
        final Distance result = distance.subtract(new Distance(3));

        // then
        assertThat(result.distance())
            .isEqualTo(4);
    }

    @Test
    @DisplayName("거리에 대해 나눈 뒤, 올림한 값을 반환한다.")
    void divideAndCeil() {
        // given
        final Distance distance = new Distance(11);

        // when
        final Distance result = distance.divideAndCeil(new Distance(3));

        // then
        assertThat(result.distance())
            .isEqualTo(4);
    }

    @ParameterizedTest(name = "대상 거리가 주어진 거리보다 크면 true, 작거나 같으면 false를 반환한다.")
    @CsvSource(value = {"3:true", "5:false", "10:false"}, delimiter = ':')
    void moreThan(final int distance, final boolean expected) {
        // given
        final Distance target = new Distance(5);

        // expected
        assertThat(target.moreThan(new Distance(distance)))
            .isSameAs(expected);
    }

    @ParameterizedTest(name = "대상 거리가 주어진 거리보다 더 작거나 같으면 true, 크면 false를 반환한다.")
    @CsvSource(value = {"5:true", "10:true", "3:false"}, delimiter = ':')
    void lessAndEqualsThan(final int distance, final boolean expected) {
        // given
        final Distance target = new Distance(5);

        // expected
        assertThat(target.lessAndEqualsThan(new Distance(distance)))
            .isSameAs(expected);
    }
}
