package subway.line.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.line.domain.FareCalculator;

class FareCalculatorTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 9, 10})
    void 거리가_10KM_이내인_경우(int distance) {
        int calculate = FareCalculator.calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(1_250);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 12, 14, 15})
    void 거리가_11km_15km_사이인_경우(int distance) {
        int calculate = FareCalculator.calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(1_350);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 20})
    void 거리가_16km_20km_사이인_경우(int distance) {
        int calculate = FareCalculator.calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(1_450);
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 58})
    void 거리가_51km_58km_사이인_경우(int distance) {
        int calculate = FareCalculator.calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(2_150);
    }

    @ParameterizedTest
    @ValueSource(ints = {59, 66})
    void 거리가_58km_66km_사이인_경우(int distance) {
        int calculate = FareCalculator.calculate(distance);
        Assertions.assertThat(calculate).isEqualTo(2_250);
    }
}
