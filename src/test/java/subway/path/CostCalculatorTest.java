package subway.path;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CostCalculatorTest {

    CostCalculator costCalculator = new CostCalculator();

    @ParameterizedTest(name = "경로에 따른 비용을 계산한다.")
    @CsvSource(value = {"0,1250", "9,1250", "10,1250", "15,1350", "16,1450", "49,2050", "50,2050", "58,2150", "59,2250"})
    void calculate(final int distance, final int cost) {
        assertThat(costCalculator.calculateAdult(distance)).isEqualTo(cost);
    }

}
