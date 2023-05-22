package subway.domain.fare.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.Fare;
import subway.domain.section.Distance;

class InitialAdditionalFareStrategyTest {

    @ParameterizedTest
    @CsvSource({"11", "12", "49", "50"})
    void isApplicableTrue(final int value) {
        final InitialAdditionalFareStrategy initialAdditionalFareStrategy = new InitialAdditionalFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(initialAdditionalFareStrategy.isApplicable(distance)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"10", "51", "52"})
    void isApplicableFalse(final int value) {
        final InitialAdditionalFareStrategy initialAdditionalFareStrategy = new InitialAdditionalFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(initialAdditionalFareStrategy.isApplicable(distance)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"11,1350", "15,1350", "16,1450", "49,2050", "50,2050"})
    void calculate(final int value, final int fare) {
        final InitialAdditionalFareStrategy initialAdditionalFareStrategy = new InitialAdditionalFareStrategy();
        final Distance distance = new Distance(value);
        assertThat(initialAdditionalFareStrategy.calculate(distance)).isEqualTo(new Fare(fare));
    }
}
