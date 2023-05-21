package subway.domain.subway.discount_policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.passenger.Passenger;
import subway.domain.subway.billing_policy.Fare;

class DiscountPolicyByAgeTest {

    @ParameterizedTest
    @DisplayName("나이에 따른 할인 정책이 적용된 요금을 계산한다.")
    @CsvSource(value = {"5:0", "12:850", "13:1150", "19:1350"}, delimiter = ':')
    void calculateDiscountedFare(int age, int fare) {
        final DiscountPolicyByAge discountPolicyByAge = new DiscountPolicyByAge();
        final Fare actual = discountPolicyByAge.calculateDiscountedFare(new Fare(1350), new Passenger(age));
        assertThat(actual.getValue()).isEqualTo(fare);
    }
}
