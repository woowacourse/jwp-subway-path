package subway.domain.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidDistanceException;

class DistanceBillingPolicyTest {

    private static final DistanceBillingPolicy billingPolicy = new DistanceBillingPolicy();

    @Nested
    @DisplayName("calculateFee 메서드는 ")
    class CalculateFee {

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -1})
        @DisplayName("거리가 0보다 작은 경우 예외를 던진다.")
        void calculateFeeWithNegativeDistance(final int distance) {
            assertThatThrownBy(() -> billingPolicy.calculateFee(distance))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessage("거리가 0보다 작은 경우 요금을 계산할 수 없습니다.");
        }

        @Test
        @DisplayName("거리가 0인 경우 요금은 0원이다.")
        void calculateFeeWithZeroDistance() {
            final int distance = 0;

            final int result = billingPolicy.calculateFee(distance);

            assertThat(result).isEqualTo(0);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10})
        @DisplayName("거리가 기본운임 거리인 10km보다 작거나 같은 경우 요금은 기본요금인 1250원이다.")
        void calculateFeeWithBaselineDistance(final int distance) {
            final int result = billingPolicy.calculateFee(distance);

            assertThat(result).isEqualTo(1250);
        }

        @ParameterizedTest
        @CsvSource(value = {"12,1350", "16,1450", "58,2250"})
        @DisplayName("거리가 10km보다 클 때 5km마다 100원이 추가된 요금이 적용된다.")
        void calculateFeeWithExtraDistance(final int distance, final int fee) {
            final int result = billingPolicy.calculateFee(distance);

            assertThat(result).isEqualTo(fee);
        }
    }
}
