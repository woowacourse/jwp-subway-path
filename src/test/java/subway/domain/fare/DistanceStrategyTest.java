package subway.domain.fare;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceStrategyTest {

    private final FareStrategy strategy = new DistanceStrategy();

    @ParameterizedTest(name = "이동 거리에 따라 요금을 계산한다.")
    @CsvSource(value = {"9,1250", "12,1350", "16,1450", "50,2050", "58,2150", "66,2250"})
    void calculate(int distance, int expectedFare) {
        Fare actual = strategy.calculate(distance);
        Fare expected = new Fare(expectedFare);

        assertThat(actual).isEqualTo(expected);
    }
}
