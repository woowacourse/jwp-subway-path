package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("요금 계산 기능")
class FareCalculatorTest {

    @DisplayName("이동거리가 10km 이하이고 성인일 때 운임요금을 반환한다.")
    @Test
    void calculate_under10_adult() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 10, 19);
        Assertions.assertThat(budget).isEqualTo(1350);
    }

    @DisplayName("이동거리가 10km 이하이고 청소년일 때 운임요금을 반환한다.")
    @Test
    void calculate_under_teenager() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 10, 18);
        Assertions.assertThat(budget).isEqualTo(1150);
    }

    @DisplayName("이동거리가 10km 이하이고 어린이일 때 운임요금을 반환한다.")
    @Test
    void calculate_under_child() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 10, 12);
        Assertions.assertThat(budget).isEqualTo(850);
    }

    @DisplayName("이동거리가 10km 초과하고 성인일 때 운임요금을 반환한다.")
    @Test
    void calculate_over10km_adult() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 31, 19);
        Assertions.assertThat(budget).isEqualTo(1850);
    }

    @DisplayName("이동거리가 10km 초과하고 청소년일 때 운임요금을 반환한다.")
    @Test
    void calculate_over10km_teenager() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 31, 18);
        Assertions.assertThat(budget).isEqualTo(1550);
    }

    @DisplayName("이동거리가 10km 초과하고 어린이일 때 운임요금을 반환한다.")
    @Test
    void calculate_over10km_child() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 31, 12);
        Assertions.assertThat(budget).isEqualTo(1100);
    }

    @DisplayName("이동거리가 50km 초과했을 때 운임요금을 반환한다.")
    @Test
    void calculate_over50km_adult() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 59, 19);
        Assertions.assertThat(budget).isEqualTo(2350);
    }

    @DisplayName("이동거리가 50km 초과했을 때 운임요금을 반환한다.")
    @Test
    void calculate_over50km_teeenager() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 59, 18);
        Assertions.assertThat(budget).isEqualTo(1950);
    }

    @DisplayName("이동거리가 50km 초과했을 때 운임요금을 반환한다.")
    @Test
    void calculate_over50km_child() {
        final int budget = FareCalculator.calculate(Line.withNullId("신분당선", "빨강", 100), 59, 12);
        Assertions.assertThat(budget).isEqualTo(1350);
    }
}
