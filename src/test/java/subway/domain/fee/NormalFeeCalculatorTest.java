package subway.domain.fee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;

import static org.assertj.core.api.Assertions.assertThat;

class NormalFeeCalculatorTest {

    @Test
    @DisplayName("10km 이내는 1250원이다.")
    void under_10km() {
        // given
        final FeeCalculator feeCalculator = new NormalFeeCalculator();
        final Distance distance = new Distance(5);

        // when
        final Fee fee = feeCalculator.calculate(distance);

        // then
        assertThat(fee.getAmount()).isEqualTo(1250);
    }

    @Test
    @DisplayName("31km는 1750원이다.")
    void between_10km_and_50km() {
        // given
        final FeeCalculator feeCalculator = new NormalFeeCalculator();
        final Distance distance = new Distance(31);

        // when
        final Fee fee = feeCalculator.calculate(distance);

        // then
        assertThat(fee.getAmount()).isEqualTo(1750);
    }

    @Test
    @DisplayName("58km는 2150원이다.")
    void over_50km() {
        // given
        final FeeCalculator feeCalculator = new NormalFeeCalculator();
        final Distance distance = new Distance(58);

        // when
        final Fee fee = feeCalculator.calculate(distance);

        // then
        assertThat(fee.getAmount()).isEqualTo(2150);
    }
}
