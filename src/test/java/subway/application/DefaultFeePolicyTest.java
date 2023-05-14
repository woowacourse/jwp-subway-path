package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFeePolicyTest {

    @DisplayName("요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9, 1250", "10, 1250", "14, 1350", "15, 1350", "58, 2150"})
    void calculateFee(int distance, int fee) {
        final DefaultFeePolicy policy = new DefaultFeePolicy();

        final int result = policy.calculateFee(distance);

        assertThat(result).isEqualTo(fee);
    }

}
