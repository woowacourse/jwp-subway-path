package subway.application.service.feecalculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.application.feecalculator.DefaultFeeCalculator;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFeeCalculatorTest {

    @DisplayName("요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9, 1250", "10, 1250", "14, 1350", "15, 1350", "50, 2050", "51, 2150", "58, 2150", "59, 2250"})
    void calculateFee(int distance, int fee) {
        final DefaultFeeCalculator calculator = new DefaultFeeCalculator();

        final int result = calculator.calculateFee(distance);

        assertThat(result).isEqualTo(fee);
    }

}
