package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FareCalculatorTest {

    @ParameterizedTest
    @DisplayName("거리가 10km 이하일때")
    @ValueSource(ints = {1, 5, 10})
    void calculate1(int distance) {
        FareCalculator fareCalculator = new FareCalculator();
        Assertions.assertThat(fareCalculator.calculate(distance)).isEqualTo(1250);
    }

    @ParameterizedTest
    @DisplayName("거리가 10km 초과 ~ 50km 이하 일때")
    @CsvSource(value = {"11,1350", "15,1350", "16,1450", "20,1450", "50,2050"})
    void calculate2(int distance, int fare) {
        FareCalculator fareCalculator = new FareCalculator();
        Assertions.assertThat(fareCalculator.calculate(distance)).isEqualTo(fare);
    }

    @ParameterizedTest
    @DisplayName("거리가 50km 초과 일때")
    @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
    void calculate3(int distance, int fare) {
        FareCalculator fareCalculator = new FareCalculator();
        Assertions.assertThat(fareCalculator.calculate(distance)).isEqualTo(fare);
    }


}