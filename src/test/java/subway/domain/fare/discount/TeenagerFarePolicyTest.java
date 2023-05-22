package subway.domain.fare.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.fare.Fare;

class TeenagerFarePolicyTest {

    @Test
    @DisplayName("청소년 요금 할인 정책을 적용한 요금을 반환한다.")
    void calculateTeenagerFare() {
        // given
        final TeenagerFarePolicy teenagerFarePolicy = new TeenagerFarePolicy();
        final Fare basicFare = new Fare(1000);

        // when
        final Fare teenagerFare = teenagerFarePolicy.calculateFare(basicFare);

        // then
        assertThat(teenagerFare.fare())
            .isEqualTo(520);
    }
}
