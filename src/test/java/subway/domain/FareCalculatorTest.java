package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareCalculatorTest {

    @DisplayName("거리에 따라 운임 요금을 계산한다.")
    @ParameterizedTest(name = "{index} : {0}을 넣으면 {1}을 반환한다.")
    @CsvSource({"9, 1250", "12, 1350", "16, 1450", "58, 2150"})
    void calculate(Double distance, int fare) {
        //when
        int actual = FareCalculator.calculate(distance);
        //then
        Assertions.assertThat(actual).isEqualTo(fare);
    }
}