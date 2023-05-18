package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountFareTest {

    @Test
    @DisplayName("청소년 요금 할인 정책을 적용한 요금을 반환한다.")
    void calculateTeenagerFare() {
        // given
        final Fare fare = new Fare(1000);
        final DiscountFare discountFare = new DiscountFare(fare);

        // when
        final Fare teenagerFare = discountFare.calculateTeenagerFare();

        // then
        assertThat(teenagerFare.fare())
            .isEqualTo(520);
    }

    @Test
    @DisplayName("어린이 요금 할인 정책을 적용한 요금을 반환한다.")
    void calculateChildFare() {
        // given
        final Fare fare = new Fare(1000);
        final DiscountFare discountFare = new DiscountFare(fare);

        // when
        final Fare teenagerFare = discountFare.calculateChildFare();

        // then
        assertThat(teenagerFare.fare())
            .isEqualTo(325);
    }
}
