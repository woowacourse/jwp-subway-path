package subway.route.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.route.domain.DistanceFareCalculator.*;

class DistanceFareCalculatorTest {

    private static final int FAR_DISTANCE_MINIMUM_FARE = MINIMUM_FARE + MAXIMUM_MID_DISTANCE_INCREMENT_COUNT * INCREMENTAL_FARE;

    final DistanceFareCalculator fareCalculator = new DistanceFareCalculator();

    @Test
    @DisplayName("입력된 거리에 따라 총 요금을 계산한다")
    void calculate() {
        assertAll(
                () -> assertThat(fareCalculator.calculate(1)).isEqualTo(MINIMUM_FARE),
                () -> assertThat(fareCalculator.calculate(MID_DISTANCE_THRESHOLD)).isEqualTo(MINIMUM_FARE),
                () -> assertThat(fareCalculator.calculate(MID_DISTANCE_THRESHOLD + MID_DISTANCE_INCREMENT_UNIT - 1)).isEqualTo(MINIMUM_FARE),
                () -> assertThat(fareCalculator.calculate(MID_DISTANCE_THRESHOLD + MID_DISTANCE_INCREMENT_UNIT)).isEqualTo(MINIMUM_FARE + INCREMENTAL_FARE),
                () -> assertThat(fareCalculator.calculate(FAR_DISTANCE_THRESHOLD)).isEqualTo(FAR_DISTANCE_MINIMUM_FARE),
                () -> assertThat(fareCalculator.calculate(FAR_DISTANCE_THRESHOLD + FAR_DISTANCE_INCREMENT_UNIT - 1)).isEqualTo(FAR_DISTANCE_MINIMUM_FARE),
                () -> assertThat(fareCalculator.calculate(FAR_DISTANCE_THRESHOLD + FAR_DISTANCE_INCREMENT_UNIT)).isEqualTo(FAR_DISTANCE_MINIMUM_FARE + INCREMENTAL_FARE)
        );
    }

    @Test
    @DisplayName("입력된 거리가 0이하면 예외가 발생된다")
    void calculateFail1() {
        assertThatThrownBy(() -> fareCalculator.calculate(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리는");
    }
}
