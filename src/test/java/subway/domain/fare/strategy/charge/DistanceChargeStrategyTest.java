package subway.domain.fare.strategy.charge;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.fare.FareInfo;
import subway.domain.subway.Line;

class DistanceChargeStrategyTest {
    @Test
    @DisplayName("일반적인 정책의 요금을 계산한다.")
    void calculateTest() {
        DistanceChargeStrategy distanceFeeStrategy = new DistanceChargeStrategy();
        assertAll(
            () -> assertThat(distanceFeeStrategy.calculate(new FareInfo(7, Set.of(new Line()), 25))).isEqualTo(1250),
            () -> assertThat(distanceFeeStrategy.calculate(new FareInfo(12, Set.of(new Line()), 25))).isEqualTo(1350),
            () -> assertThat(distanceFeeStrategy.calculate(new FareInfo(17, Set.of(new Line()), 25))).isEqualTo(1450),
            () -> assertThat(distanceFeeStrategy.calculate(new FareInfo(67, Set.of(new Line()), 25))).isEqualTo(2350));
    }

}
