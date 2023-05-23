package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeFarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"2000,3,2000", "2000,7,825", "2000,18,330", "2000,26,0"})
    @DisplayName("나이에 따라 할인 요금을 계산한다.")
    void calculateDiscountFare(int fare, int age, int discountFare) {
        assertThat(AgeFarePolicy.calculateDiscountFare(new Age(age), new Fare(fare))).isEqualTo(new Fare(discountFare));
    }
}
