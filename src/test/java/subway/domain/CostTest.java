package subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.common.Cost;

import static org.assertj.core.api.Assertions.assertThat;

class CostTest {

    private Cost cost = new Cost();

    @ParameterizedTest(name = "경로에 따른 비용을 계산한다.")
    @CsvSource(value = {"0,1250", "9,1250", "10,1350", "15,1350", "16,1450", "49,2050", "50,2150", "58,2150", "59,2250"})
    void calculate(int distance, int costValue) {
        assertThat(cost.calculate(distance)).isEqualTo(costValue);
    }
}
