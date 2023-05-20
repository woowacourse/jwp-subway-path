package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FeeCalculatorTest {

    private final FeeCalculator feeCalculator = new FeeCalculator();

    @ParameterizedTest
    @CsvSource({"1, 1250" , "10, 1250", "11, 1350", "15, 1350", "16, 1450", "21, 1550", "30, 1650", "33, 1750", "36, 1850", "45, 1950", "46, 2050", "50, 2050", "51, 2150", "58, 2150", "59, 2250", "67, 2350", "74, 2350"})
    void 거리_정보를_받아_요금을_계산한다(final double distance, final int expect) {
        var result = feeCalculator.calculate(distance);

        assertThat(result).isEqualTo(expect);
    }
}
