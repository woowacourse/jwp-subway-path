package subway.application.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.fare.Fare;

@SuppressWarnings("NonAsciiCharacters")
class AgeDiscountPolicyTest {

    private AgeDiscountPolicy ageDiscountPolicy;

    @BeforeEach
    void setUp() {
        ageDiscountPolicy = new AgeDiscountPolicy();
    }

    @Test
    void 어린이_요금_할인_테스트() {
        final Fare fare = new Fare(1_250);

        final Fare discount = ageDiscountPolicy.discount(fare, 7);

        assertThat(discount.getFare()).isEqualTo(450); //(1250 - 350) * 0.5 = 450
    }

    @Test
    void 청소년_요금_할인_테스트() {
        final Fare fare = new Fare(1_250);

        final Fare discount = ageDiscountPolicy.discount(fare, 15);

        assertThat(discount.getFare()).isEqualTo(720); //(1250 - 350) * 0.8 = 720
    }

    @Test
    void 일반_요금_할인_테스트() {
        final Fare fare = new Fare(1_250);

        final Fare discount = ageDiscountPolicy.discount(fare, 25);

        assertThat(discount.getFare()).isEqualTo(1_250);
    }
}
