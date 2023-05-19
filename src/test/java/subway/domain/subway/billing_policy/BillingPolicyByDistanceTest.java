package subway.domain.subway.billing_policy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.Path;

class BillingPolicyByDistanceTest {

    @ParameterizedTest
    @DisplayName("거리에 맞는 요금을 계산한다.")
    @CsvSource(value = {"5:1250", "12:1350", "16:1450", "55:2150", "58:2150"}, delimiter = ':')
    void calculateFare(int distance, int expected) {
        //given
        Path path = new Path(Collections.emptyList(), Collections.emptyList(), distance);
        BillingPolicyByDistance billingPolicyByDistance = new BillingPolicyByDistance();

        //when
        final Fare actual = billingPolicyByDistance.calculateFare(path);

        //then
        assertThat(actual.getValue()).isEqualTo(expected);
    }
}
