package subway.domain.subway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.Path;
import subway.domain.subway.billing_policy.BillingPolicyByDistance;
import subway.domain.subway.billing_policy.Fare;

class BillingPolicyByDistanceTest {

    @ParameterizedTest
    @DisplayName("거리에 맞는 요금을 계산한다.")
    @CsvSource(value = {"5:1250", "11:1250", "15:1350", "55:2050", "58:2150"}, delimiter = ':')
    void calculateFare(int distance, int expected) {
        //given
        Path path = new Path(Collections.emptyList(), distance);
        BillingPolicyByDistance billingPolicyByDistance = new BillingPolicyByDistance();

        //when
        final Fare actual = billingPolicyByDistance.calculateFare(path);

        //then
        assertThat(actual.getValue()).isEqualTo(expected);
    }
}
