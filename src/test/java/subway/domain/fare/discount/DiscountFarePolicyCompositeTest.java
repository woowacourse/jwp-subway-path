package subway.domain.fare.discount;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.fare.DiscountFare;
import subway.domain.fare.Fare;

class DiscountFarePolicyCompositeTest {

    private DiscountFarePolicyComposite discountFarePolicyComposite;

    @BeforeEach
    void setUp() {
        discountFarePolicyComposite = new DiscountFarePolicyComposite(
            List.of(new ChildFarePolicy(), new TeenagerFarePolicy())
        );
    }

    @Test
    @DisplayName("연령에 따라 할인된 금액을 반환한다.")
    void getDiscountFares() {
        // given
        final Fare fare = new Fare(1000);

        // when
        final DiscountFare discountFare = discountFarePolicyComposite.getDiscountFares(fare);

        // then
        assertThat(discountFare)
            .extracting(DiscountFare::getTeenagerFare, DiscountFare::getChildFare)
            .containsExactly(new Fare(520), new Fare(325));
    }
}
