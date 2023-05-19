package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("요금 계산 기능")
class FareCalculatorTest {

    @DisplayName("이동거리가 10km 이하일 때 운임요금을 반환한다.")
    @Test
    void calculate_normal() {
        final int budget = FareCalculator.calculate(new Line("신분당선", "빨강", 100), 10);
        Assertions.assertThat(budget).isEqualTo(1350);
    }

    @DisplayName("이동거리가 10km 초과했을 때 운임요금을 반환한다.")
    @Test
    void calculate_over10km() {
        final int budget = FareCalculator.calculate(new Line("신분당선", "빨강", 100), 31);
        Assertions.assertThat(budget).isEqualTo(1850);
    }

    @DisplayName("이동거리가 50km 초과했을 때 운임요금을 반환한다.")
    @Test
    void calculate_over50km() {
        final int budget = FareCalculator.calculate(new Line("신분당선", "빨강", 100), 59);
        Assertions.assertThat(budget).isEqualTo(2350);
    }
}
