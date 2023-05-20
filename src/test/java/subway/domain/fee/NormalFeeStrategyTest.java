package subway.domain.fee;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NormalFeeStrategyTest {
    @Test
    @DisplayName("일반적인 정책의 요금을 계산한다.")
    void calculateTest() {
        NormalFeeStrategy normalFeeStrategy = new NormalFeeStrategy();
        assertAll(
            () -> assertThat(normalFeeStrategy.calculate(7)).isEqualTo(1250),
            () -> assertThat(normalFeeStrategy.calculate(12)).isEqualTo(1350),
            () -> assertThat(normalFeeStrategy.calculate(17)).isEqualTo(1450),
            () -> assertThat(normalFeeStrategy.calculate(67)).isEqualTo(2350));
    }

}
