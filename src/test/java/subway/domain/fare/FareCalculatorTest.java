package subway.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.fare.distance.BasicFarePolicy;
import subway.domain.fare.distance.EightUnitFarePolicy;
import subway.domain.fare.distance.FiveUnitFarePolicy;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FareCalculatorTest {

    @ParameterizedTest(name = "거리가 {0}km 이면 요금은 {1}원이다.")
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "16:1450",
            "49:2050", "50:2050", "51:2150", "57:2150", "58:2150", "59:2250"}, delimiter = ':')
    void test(final int distance, final int expectedFare) {
        final FareCalculator fareCalculator = new FareCalculator(new DiscountChain(List.of(new BasicFarePolicy(), new EightUnitFarePolicy(), new FiveUnitFarePolicy())));
        final int fare = fareCalculator.calculate(distance);

        assertThat(fare).isEqualTo(expectedFare);
    }
}