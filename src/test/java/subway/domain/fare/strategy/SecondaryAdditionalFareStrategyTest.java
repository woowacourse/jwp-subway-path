package subway.domain.fare.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.Fare;
import subway.domain.section.Distance;

class SecondaryAdditionalFareStrategyTest {

    @ParameterizedTest
    @CsvSource({"51", "52", "100", "200"})
    void isApplicableTrue(final int value) {
        final SecondaryAdditionalFareStrategy secondaryAdditionalFareStrategy = new SecondaryAdditionalFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(secondaryAdditionalFareStrategy.isApplicable(distance)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"49", "50"})
    void isApplicableFalse(final int value) {
        final SecondaryAdditionalFareStrategy secondaryAdditionalFareStrategy = new SecondaryAdditionalFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(secondaryAdditionalFareStrategy.isApplicable(distance)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"51,2150", "58,2150", "59,2250", "66,2250", "67,2350"})
    void calculate(final int value, final int fare) {
        final SecondaryAdditionalFareStrategy secondaryAdditionalFareStrategy = new SecondaryAdditionalFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(secondaryAdditionalFareStrategy.calculate(distance)).isEqualTo(new Fare(fare));
    }
}
