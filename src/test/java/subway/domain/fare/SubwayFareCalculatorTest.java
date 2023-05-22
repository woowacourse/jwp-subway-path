package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.strategy.DefaultFareStrategy;
import subway.domain.fare.strategy.InitialAdditionalFareStrategy;
import subway.domain.fare.strategy.SecondaryAdditionalFareStrategy;
import subway.domain.section.Distance;

class SubwayFareCalculatorTest {

    @DisplayName("거리 별로 요금을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10,1250", "11,1350", "15,1350", "16,1450", "20,1450", "50,2050", "51,2150", "58,2150", "59,2250", "66,2250", "67,2350"})
    void test(final int distance, final int fare) {
        final SubwayFareCalculator subwayFareCalculator = new SubwayFareCalculator(
                List.of(
                        new DefaultFareStrategy(),
                        new InitialAdditionalFareStrategy(),
                        new SecondaryAdditionalFareStrategy()
                )
        );
        final Fare result = subwayFareCalculator.calculate(new Distance(distance));
        assertThat(result).isEqualTo(new Fare(fare));
    }
}
