package subway.route.domain.fare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceFarePolicyTest {

    final DistanceFarePolicy fareCalculator = new DistanceFarePolicy();
    FareFactors fareFactors;

    @BeforeEach
    void setUp() {
        fareFactors = new FareFactors();
    }

    @ParameterizedTest(name = "입력된 거리에 따라 총 요금을 계산한다")
    @CsvSource({"1,1250", "14,1250", "15,1350", "50,2050", "57,2050", "58,2150"})
    void calculate(int distance, int fare) {
        fareFactors.setFactor("total_distance", distance);

        assertThat(fareCalculator.calculate(fareFactors, 0)).isEqualTo(fare);
    }

    @Test
    @DisplayName("입력된 거리가 0이하면 예외가 발생된다")
    void calculateFail1() {
        fareFactors.setFactor("total_distance", 0);

        assertThatThrownBy(() -> fareCalculator.calculate(fareFactors, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리는");
    }
}
