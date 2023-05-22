package subway.domain.fare.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.Distance;
import subway.domain.fare.Fare;

class DefaultFareStrategyTest {

    @ParameterizedTest
    @CsvSource({"1", "2", "10"})
    void isApplicableTrue(final int value) {
        final DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(defaultFareStrategy.isApplicable(distance)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"11", "12"})
    void isApplicableFalse(final int value) {
        final DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(defaultFareStrategy.isApplicable(distance)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "10"})
    void calculate(final int value) {
        final DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(defaultFareStrategy.calculate(distance)).isEqualTo(new Fare(1250));
    }
}
